package data.notification

import android.app.PendingIntent
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat

internal interface NotificationManager {
    fun notify(@StringRes channelName: Int,
               @StringRes title: Int,
               @StringRes body: Int,
               @NotificationCategory category: String,
               @NotificationPriority priority: Int = NotificationManager.PRIORITY_MEDIUM,
               @NotificationVisibility visibility: Int = NotificationManager.VISIBILITY_PUBLIC,
               clickHandler: PendingIntent? = null)

    fun notify(@StringRes channelName: Int,
               title: String,
               body: String,
               bigBody: String? = null,
               @NotificationCategory category: String,
               @NotificationPriority priority: Int = NotificationManager.PRIORITY_MEDIUM,
               @NotificationVisibility visibility: Int = NotificationManager.VISIBILITY_PUBLIC,
               clickHandler: PendingIntent? = null)

    companion object {
        const val CATEGORY_RECOMMENDATION = NotificationCompat.CATEGORY_RECOMMENDATION
        const val CATEGORY_SERVICE = NotificationCompat.CATEGORY_SERVICE
        const val PRIORITY_MEDIUM = NotificationCompat.PRIORITY_DEFAULT
        const val PRIORITY_LOW = NotificationCompat.PRIORITY_LOW
        const val PRIORITY_HIGH = NotificationCompat.PRIORITY_HIGH
        const val VISIBILITY_PRIVATE = NotificationCompat.VISIBILITY_PRIVATE
        const val VISIBILITY_PUBLIC = NotificationCompat.VISIBILITY_PUBLIC
        const val VISIBILITY_SECRET = NotificationCompat.VISIBILITY_SECRET
    }
}
