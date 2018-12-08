package data.autoswipe

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.annotation.IntDef
import data.notification.NotificationManager
import domain.like.DomainLikedRecommendationAnswer
import org.stoyicker.dinger.data.R
import java.util.Date

internal class AutoSwipeReportHandler(
        private val notificationManager: NotificationManager) {
    private var likeCounter = 0
    private var matchCounter = 0

    fun addLikeAnswer(answer: DomainLikedRecommendationAnswer) {
        // The last like done comes with rateLimited too
        if (answer.rateLimitedUntilMillis == null || likeCounter == 0) {
            addLike()
            if (answer.matched) {
                addMatch()
            }
        }
    }

    private fun addMatch() {
        ++matchCounter
    }

    private fun addLike() {
        ++likeCounter
    }

    fun show(
            context: Context,
            scheduledFor: Long?,
            errorMessage: String?,
            @AutoSwipeResult result: Int) =
            notificationManager.show(build(context, scheduledFor, errorMessage, result))

    fun buildPlaceHolder(context: Context) = build(context, null, null, RESULT_PLACEHOLDER)

    private fun build(
            context: Context,
            scheduledFor: Long?,
            errorMessage: String?,
            @AutoSwipeResult result: Int) =
            notificationManager.build(
                    channelName = R.string.autoswipe_notification_channel_name,
                    title = generateTitle(context, likeCounter, matchCounter),
                    body = generateBody(
                            context,
                            if (scheduledFor == null) null else Date(scheduledFor).toString(),
                            errorMessage,
                            result),
                    category = NotificationManager.CATEGORY_SERVICE,
                    priority = NotificationManager.PRIORITY_LOW,
                    clickHandler = PendingIntent.getActivity(
                            context,
                            0,
                            Intent().setComponent(
                                    ComponentName(context, "app.splash.SplashActivity")),
                            FLAG_UPDATE_CURRENT),
                    actions = if (result !in arrayOf(RESULT_MORE_AVAILABLE, RESULT_PLACEHOLDER)) {
                        arrayOf(Notification.Action.Builder(
                                null,
                                context.getText(R.string.autoswipe_notification_action_run_now),
                                PendingIntent.getBroadcast(
                                        context,
                                        0,
                                        AutoSwipeLauncherBroadcastReceiver.getCallingIntent(context),
                                        FLAG_UPDATE_CURRENT))
                                .build())
                    } else {
                        emptyArray()
                    })

    companion object {
        const val RESULT_RATE_LIMITED = 1
        const val RESULT_MORE_AVAILABLE = 2
        const val RESULT_ERROR = 3
        const val RESULT_BATCH_CLOSED = 4
        private const val RESULT_PLACEHOLDER = -1


        @Retention(AnnotationRetention.SOURCE)
        @IntDef(RESULT_RATE_LIMITED,
                RESULT_MORE_AVAILABLE,
                RESULT_ERROR,
                RESULT_BATCH_CLOSED,
                RESULT_PLACEHOLDER)
        internal annotation class AutoSwipeResult

        private fun generateBody(
                context: Context,
                scheduledFor: String?,
                errorMessage: String?,
                @AutoSwipeResult result: Int) = when (result) {
            RESULT_RATE_LIMITED -> context.getString(
                    R.string.autoswipe_notification_body_capped, scheduledFor)
            RESULT_MORE_AVAILABLE -> context.getString(R.string.autoswipe_notification_body_more_available)
            RESULT_ERROR -> context.getString(
                    R.string.autoswipe_notification_body_error, scheduledFor, errorMessage)
            RESULT_BATCH_CLOSED -> context.getString(
                    R.string.autoswipe_notification_body_batch_closed, scheduledFor)
            RESULT_PLACEHOLDER -> context.getString(R.string.autoswipe_notification_body_placeholder)
            else -> throw IllegalStateException("Unexpected result $result in the autoswipe report.")
        }
    }
}

private fun generateTitle(context: Context, likes: Int, matches: Int) = StringBuilder().apply {
    append(context.resources.getQuantityString(
            R.plurals.autoswipe_notification_title_swept, likes, likes))
    append(context.resources.getQuantityString(
            R.plurals.autoswipe_notification_title_matches, matches, matches))
}.toString()
