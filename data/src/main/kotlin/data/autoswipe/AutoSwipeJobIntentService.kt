package data.autoswipe

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v4.app.JobIntentService
import data.tinder.recommendation.GetRecommendationsAction
import data.tinder.recommendation.RecommendationUserResolver
import domain.autoswipe.FromErrorPostAutoSwipeUseCase
import domain.like.DomainLikedRecommendationAnswer
import domain.recommendation.DomainRecommendationUser
import org.stoyicker.dinger.data.R
import reporter.CrashReporter
import retrofit2.HttpException
import java.util.Date
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

internal class AutoSwipeJobIntentService : JobIntentService() {
    @Inject
    lateinit var crashReporter: CrashReporter
    @Inject
    lateinit var defaultSharedPreferences: SharedPreferences
    @Inject
    lateinit var getRecommendationsAction: GetRecommendationsAction
    @Inject
    lateinit var processRecommendationActionFactory: ProcessRecommendationActionFactoryWrapper
    @Inject
    lateinit var recommendationResolver: RecommendationUserResolver
    @Inject
    lateinit var reportHandler: AutoSwipeReportHandler
    @Inject
    lateinit var likeBatchTracker: LikeBatchTracker
    private var reScheduled = false
    private var ongoingActions = emptySet<Action<*>>()

    init {
        AutoSwipeComponentHolder.autoSwipeComponent.inject(this)
    }

    override fun onHandleWork(intent: Intent) {
        if (defaultSharedPreferences.getBoolean(
                        getString(R.string.preference_key_autoswipe_enabled), true)) {
            try {
                startAutoSwipe()
            } catch (e: Exception) {
                scheduleBecauseError(e)
            }
        } else {
            scheduleBecauseError()
        }
    }

    override fun onStopCurrentWork() = true.also { releaseResources() }

    override fun onDestroy() {
        super.onDestroy()
        releaseResources()
        if (!reScheduled) {
            likeBatchTracker.closeBatch()
            scheduleBecauseError()
        }
    }

    abstract class Action<in Callback> {
        protected val commonDelegate by lazy { CommonResultDelegate(this) }

        abstract fun execute(owner: AutoSwipeJobIntentService, callback: Callback)

        abstract fun dispose()
    }

    class CommonResultDelegate(private val action: Action<*>) {
        fun onComplete(autoSwipeJobIntentService: AutoSwipeJobIntentService) {
            autoSwipeJobIntentService.clearAction(action)
        }

        fun onError(error: Throwable, autoSwipeJobIntentService: AutoSwipeJobIntentService) {
            if (error is HttpException && error.code() == 401) {
                onComplete(autoSwipeJobIntentService)
            } else {
                autoSwipeJobIntentService.scheduleBecauseError(error)
                autoSwipeJobIntentService.clearAction(action)
            }
        }
    }

    private fun startAutoSwipe() = Unit.also {
        getRecommendationsAction.apply {
            ongoingActions += (this)
            execute(this@AutoSwipeJobIntentService,
                    object : GetRecommendationsAction.Callback {
                        override fun onRecommendationsReceived(
                                recommendations: List<DomainRecommendationUser>) {
                            if (recommendations.isEmpty()) {
                                scheduleBecauseError(IllegalStateException("Empty recommendation list"))
                            } else {
                                processRecommendations(recommendations)
                            }
                        }
                    })
        }
    }

    private fun processRecommendations(recommendations: List<DomainRecommendationUser>) {
        val latch = CountDownLatch(recommendations.size)
        recommendations.forEach { recommendation ->
            likeBatchTracker.addLike()
            processRecommendationActionFactory.delegate(recommendation).apply {
                ongoingActions += (this)
                execute(this@AutoSwipeJobIntentService,
                        object : ProcessRecommendationAction.Callback {
                            override fun onRecommendationProcessed(
                                    answer: DomainLikedRecommendationAnswer) =
                                    saveRecommendationToDatabase(
                                            recommendation = recommendation,
                                            liked = answer.rateLimitedUntilMillis != null,
                                            matched = answer.matched).also {
                                        reportHandler.addLikeAnswer(answer)
                                        latch.countDown()
                                        answer.rateLimitedUntilMillis?.let { limitedUntil ->
                                            scheduleBecauseLimited(limitedUntil)
                                            return
                                        }
                                    }

                            override fun onRecommendationProcessingFailed() =
                                    saveRecommendationToDatabase(
                                            recommendation,
                                            liked = false,
                                            matched = false).also {
                                        latch.countDown()
                                    }
                        })
            }
        }
        latch.await()
        if (likeBatchTracker.isBatchOpen()) {
            scheduleBecauseMoreAvailable()
        } else {
            scheduleBecauseBatchClosed()
        }
    }

    private fun saveRecommendationToDatabase(
            recommendation: DomainRecommendationUser, liked: Boolean, matched: Boolean) {
        recommendationResolver.insert(DomainRecommendationUser(
                bio = recommendation.bio,
                distanceMiles = recommendation.distanceMiles,
                commonFriends = recommendation.commonFriends,
                commonFriendCount = recommendation.commonFriendCount,
                commonLikes = recommendation.commonLikes,
                commonLikeCount = recommendation.commonLikeCount,
                id = recommendation.id,
                birthDate = recommendation.birthDate,
                name = recommendation.name,
                instagram = recommendation.instagram,
                teaser = recommendation.teaser,
                spotifyThemeTrack = recommendation.spotifyThemeTrack,
                gender = recommendation.gender,
                birthDateInfo = recommendation.birthDateInfo,
                contentHash = recommendation.contentHash,
                groupMatched = recommendation.groupMatched,
                sNumber = recommendation.sNumber,
                liked = liked,
                matched = matched,
                photos = recommendation.photos,
                jobs = recommendation.jobs,
                schools = recommendation.schools,
                teasers = recommendation.teasers))
    }

    private fun scheduleBecauseMoreAvailable() {
        reportHandler.show(
                this,
                null,
                null,
                AutoSwipeReportHandler.RESULT_MORE_AVAILABLE)
        ImmediatePostAutoSwipeAction().apply {
            ongoingActions += this
            execute(this@AutoSwipeJobIntentService, Unit)
            reScheduled = true
        }
    }

    private fun scheduleBecauseLimited(notBeforeMillis: Long) {
        FromRateLimitedPostAutoSwipeAction(notBeforeMillis).apply {
            ongoingActions += this
            execute(this@AutoSwipeJobIntentService, Unit)
        }
        reportHandler.show(
                this,
                notBeforeMillis,
                null,
                AutoSwipeReportHandler.RESULT_RATE_LIMITED)
        reScheduled = true
        likeBatchTracker.closeBatch()
    }

    private fun scheduleBecauseBatchClosed() {
        val notBeforeMillis = Date().time + 1000 * 60 * 60 * 2L //2h from now
        FromRateLimitedPostAutoSwipeAction(notBeforeMillis).apply {
            ongoingActions += this
            execute(this@AutoSwipeJobIntentService, Unit)
        }
        reportHandler.show(
                this,
                notBeforeMillis,
                null,
                AutoSwipeReportHandler.RESULT_BATCH_CLOSED)
        reScheduled = true
        likeBatchTracker.closeBatch()
    }

    private fun scheduleBecauseError(error: Throwable? = null) {
        if (error != null) {
            crashReporter.report(error)
            reportHandler.show(
                    this,
                    FromErrorPostAutoSwipeUseCase.interval(this),
                    error.localizedMessage,
                    AutoSwipeReportHandler.RESULT_ERROR)
        }
        FromErrorPostAutoSwipeAction().apply {
            ongoingActions += this
            execute(this@AutoSwipeJobIntentService, Unit)
        }
        reScheduled = true
        likeBatchTracker.closeBatch()
    }

    private fun releaseResources() {
        ongoingActions.forEach { it.dispose() }
        ongoingActions = emptySet()
    }

    private fun clearAction(action: Action<*>) = action.apply {
        dispose()
        ongoingActions -= this
    }

    companion object {
        private const val JOB_ID = 1000
        fun trigger(context: Context) = enqueueWork(
                context, AutoSwipeJobIntentService::class.java, JOB_ID, Intent())
    }
}
