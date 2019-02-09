package data.autoswipe

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.isOnNotMeteredInternet
import android.os.Build
import data.tinder.recommendation.GetRecommendationsAction
import data.tinder.recommendation.RecommendationUserResolver
import domain.autoswipe.FromErrorPostAutoSwipeUseCase
import domain.like.DomainLikedRecommendationAnswer
import domain.recommendation.DomainRecommendationUser
import org.stoyicker.dinger.data.R
import reporter.CrashReporter
import retrofit2.HttpException
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

internal class AutoSwipeIntentService : IntentService("AutoSwipe") {
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

  override fun onHandleIntent(intent: Intent) {
    reportHandler.buildPlaceHolder(this).apply { startForeground(id, delegate) }
    if (defaultSharedPreferences.getBoolean(
            getString(R.string.preference_key_autoswipe_enabled), true) &&
        (!defaultSharedPreferences.getBoolean(getString(R.string.preference_key_wifi_only), true)
            || isOnNotMeteredInternet())) {
      try {
        startAutoSwipe()
      } catch (e: Exception) {
        scheduleBecauseError(e)
      }
    } else {
      silentReschedule()
    }
  }

  override fun onDestroy() {
    if (!reScheduled) {
      likeBatchTracker.closeBatch()
      silentReschedule()
    }
    super.onDestroy()
  }

  abstract class Action<in Callback> {
    protected val commonDelegate by lazy { CommonResultDelegate(this) }

    abstract fun execute(owner: AutoSwipeIntentService, callback: Callback)

    abstract fun dispose()
  }

  class CommonResultDelegate(private val action: Action<*>) {
    fun onComplete(autoSwipeIntentService: AutoSwipeIntentService) {
      autoSwipeIntentService.clearAction(action)
    }

    fun onError(error: Throwable, autoSwipeIntentService: AutoSwipeIntentService) {
      if (error is HttpException && error.code() == 401) {
        onComplete(autoSwipeIntentService)
      } else {
        autoSwipeIntentService.scheduleBecauseError(error)
        autoSwipeIntentService.clearAction(action)
      }
    }
  }

  private fun startAutoSwipe() = Unit.also {
    getRecommendationsAction.apply {
      ongoingActions += (this)
      execute(this@AutoSwipeIntentService,
          object : GetRecommendationsAction.Callback {
            override fun onRecommendationsReceived(
                recommendations: List<DomainRecommendationUser>) {
              if (recommendations.isEmpty()) {
                scheduleBecauseError(IllegalStateException("Empty recommendation filter"))
              } else {
                processRecommendations(recommendations)
              }
            }
          })
    }
  }

  private fun processRecommendations(recommendations: List<DomainRecommendationUser>) {
    val latch = CountDownLatch(recommendations.size)
    var processedThisRun = 0 // This will be used to avoid rescheduling immediately if no likes happened
    for (recommendation in recommendations) {
      likeBatchTracker.addLike()
      processRecommendationActionFactory.delegate(recommendation).apply {
        ongoingActions += (this)
        if (defaultSharedPreferences.getBoolean(
                getString(R.string.preference_key_swipe_at_human_speed),
                true)) {
          TimeUnit.SECONDS.sleep(Random.nextLong(1, 3))
        }
        execute(this@AutoSwipeIntentService,
            object : ProcessRecommendationAction.Callback {
              override fun onRecommendationProcessed(
                  answer: DomainLikedRecommendationAnswer, liked: Boolean) =
                  saveRecommendationToDatabase(
                      recommendation = recommendation,
                      liked = liked,
                      matched = answer.matched).also {
                    if (liked) {
                      reportHandler.addLikeAnswer(answer)
                    }
                    ++processedThisRun
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
                    // Assume the rest of the batch will fail too,
                    // is probably the most likely thing to happen
                    while (latch.count > 0) {
                      latch.countDown()
                    }
                  }
            })
      }
      // The latch may be done because either we swept all recommendations,
      // case in which this is not needed, or we failed to swipe one thus
      // assumed the next ones in the batch would fail and forcefully signalled
      // the latch
      if (latch.count == 0L) {
        break
      }
    }
    latch.await()
    if (likeBatchTracker.isBatchOpen()) {
      if (processedThisRun > 0) {
        scheduleBecauseMoreAvailable()
      } else {
        scheduleBecauseError(Error(getString(R.string.autoswipe_error_no_recs_processed)))
      }
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
      execute(this@AutoSwipeIntentService, Unit)
      reScheduled = true
    }
    releaseResourcesAndDetachNotification()
  }

  private fun scheduleBecauseLimited(notBeforeMillis: Long) {
    FromRateLimitedPostAutoSwipeAction(notBeforeMillis).apply {
      ongoingActions += this
      execute(this@AutoSwipeIntentService, Unit)
    }
    reportHandler.show(
        this,
        notBeforeMillis,
        null,
        AutoSwipeReportHandler.RESULT_RATE_LIMITED)
    reScheduled = true
    likeBatchTracker.closeBatch()
    releaseResourcesAndDetachNotification()
  }

  private fun scheduleBecauseBatchClosed() {
    val notBeforeMillis = Date().time + 1000 * 60 * 60 * 2L //2h from now
    FromRateLimitedPostAutoSwipeAction(notBeforeMillis).apply {
      ongoingActions += this
      execute(this@AutoSwipeIntentService, Unit)
    }
    reportHandler.show(
        this,
        notBeforeMillis,
        null,
        AutoSwipeReportHandler.RESULT_BATCH_CLOSED)
    reScheduled = true
    likeBatchTracker.closeBatch()
    releaseResourcesAndDetachNotification()
  }

  private fun silentReschedule() = scheduleBecauseError()

  private fun scheduleBecauseError(error: Throwable? = null) {
    FromErrorPostAutoSwipeAction().apply {
      ongoingActions += this
      execute(this@AutoSwipeIntentService, Unit)
    }
    if (error != null) {
      crashReporter.report(error)
      reportHandler.show(
          this,
          FromErrorPostAutoSwipeUseCase.interval(this),
          error.localizedMessage,
          AutoSwipeReportHandler.RESULT_ERROR)
      releaseResourcesAndDetachNotification()
    } else {
      releaseResourcesAndRemoveNotification()
    }
    reScheduled = true
    likeBatchTracker.closeBatch()
  }

  private fun releaseResourcesAndRemoveNotification() {
    dismissOngoingActions()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      stopForeground(Service.STOP_FOREGROUND_REMOVE)
    } else {
      stopForeground(true)
    }
  }

  private fun releaseResourcesAndDetachNotification() {
    dismissOngoingActions()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      stopForeground(Service.STOP_FOREGROUND_DETACH)
    } else {
      stopForeground(false)
    }
  }

  private fun dismissOngoingActions() {
    ongoingActions.forEach { it.dispose() }
    ongoingActions = emptySet()
  }

  private fun clearAction(action: Action<*>) = action.apply {
    dispose()
    ongoingActions -= this
  }

  companion object {
    fun callingIntent(context: Context) = Intent(context, AutoSwipeIntentService::class.java)
  }
}
