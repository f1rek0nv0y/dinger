package data.autoswipe

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.annotation.IntDef
import data.autoswipe.AutoSwipeReportHandler.Companion.RESULT_BATCH_CLOSED
import data.autoswipe.AutoSwipeReportHandler.Companion.RESULT_ERROR
import data.autoswipe.AutoSwipeReportHandler.Companion.RESULT_MORE_AVAILABLE
import data.autoswipe.AutoSwipeReportHandler.Companion.RESULT_RATE_LIMITED
import data.notification.NotificationManager
import domain.like.DomainLikedRecommendationAnswer
import org.stoyicker.dinger.data.R
import java.util.Date

internal class AutoSwipeReportHandler(
        private val defaultSharedPreferences: SharedPreferences,
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

    private fun addMatch() { ++matchCounter }

    private fun addLike() { ++likeCounter }

    fun show(context: Context, scheduledFor: Long?, @AutoSwipeResult result: Int) {
        if (!areNotificationsEnabled(context, defaultSharedPreferences)) return
        notificationManager.notify(
                channelName = R.string.autoswipe_notification_channel_name,
                title = generateTitle(context, likeCounter, matchCounter),
                body = generateBody(
                        context,
                        if (scheduledFor == null) null else Date(scheduledFor).toString(),
                        result),
                category = NotificationManager.CATEGORY_SERVICE,
                priority = NotificationManager.PRIORITY_LOW,
                clickHandler = PendingIntent.getActivity(
                        context,
                        1,
                        Intent("org.stoyicker.action.HOME"),
                        PendingIntent.FLAG_UPDATE_CURRENT))
    }

    companion object {
        const val RESULT_RATE_LIMITED = 1
        const val RESULT_MORE_AVAILABLE = 2
        const val RESULT_ERROR = 3
        const val RESULT_BATCH_CLOSED = 4
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(RESULT_RATE_LIMITED, RESULT_MORE_AVAILABLE, RESULT_ERROR, RESULT_BATCH_CLOSED)
internal annotation class AutoSwipeResult

private fun generateTitle(context: Context, likes: Int, matches: Int) = StringBuilder().apply {
    append(context.resources.getQuantityString(
            R.plurals.autoswipe_notification_title_swept, likes, likes))
    append(context.resources.getQuantityString(
            R.plurals.autoswipe_notification_title_matches, matches, matches))
}.toString()

private fun generateBody(
        context: Context,
        scheduledFor: String?,
        @AutoSwipeResult result: Int) = when (result) {
    RESULT_RATE_LIMITED -> context.getString(
            R.string.autoswipe_notification_body_capped, scheduledFor)
    RESULT_MORE_AVAILABLE -> context.getString(R.string.autoswipe_notification_body_more_available)
    RESULT_ERROR -> context.getString(
            R.string.autoswipe_notification_body_error, scheduledFor)
    RESULT_BATCH_CLOSED -> context.getString(
            R.string.autoswipe_notification_body_batch_closed, scheduledFor)
    else -> throw IllegalStateException("Unexpected result $result in the autoswipe report.")
}

private fun areNotificationsEnabled(context: Context, preferences: SharedPreferences) =
        preferences.getBoolean(
                context.getString(R.string.preference_key_autoswipe_notifications_enabled),
                false)
