package data.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import org.jetbrains.annotations.Contract
import org.stoyicker.dinger.data.R
import java.util.Date
import java.util.Locale

internal class NotificationManagerImpl(
        private val context: Context,
        private val notificationID: NotificationID) : NotificationManager {
    @Contract(value = "_, _, _, _, null, true, _, _, _ -> fail")
    override fun build(
            @StringRes channelName: Int,
            @StringRes title: Int,
            @StringRes body: Int,
            @NotificationCategory category: String,
            @NotificationPriority priority: Int,
            @NotificationVisibility visibility: Int,
            clickHandler: PendingIntent?) = build(
            channelName = channelName,
            title = context.getString(title),
            body = context.getString(body),
            category = category,
            priority = priority,
            visibility = visibility,
            clickHandler = clickHandler)

    override fun build(
            @StringRes channelName: Int,
            title: String,
            body: String,
            bigBody: String?,
            @NotificationCategory category: String,
            @NotificationPriority priority: Int,
            @NotificationVisibility visibility: Int,
            clickHandler: PendingIntent?): IdentifiedNotification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.getSystemService<android.app.NotificationManager>(
                    android.app.NotificationManager::class.java)
                    .createNotificationChannel(NotificationChannel(
                            getChannelId(context, channelName),
                            context.getString(channelName), getChannelImportance(priority)).apply {
                        enableVibration(true)
                        enableLights(true)
                        setSound(null, null) // No sound
                    })
        }
        @Suppress("DEPRECATION") // Deprecated from API 26 on, not before
        return IdentifiedNotification(notificationID.next(context), Notification.Builder(context)
                .setAutoCancel(false)
                .setContentIntent(clickHandler)
                .setContentText(body)
                .setContentTitle(title)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setOngoing(false)
                .setOnlyAlertOnce(true)
                .setPriority(priority)
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker(body)
                .setStyle(Notification.BigTextStyle()
                        .bigText(bigBody))
                .setShowWhen(true)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                        setLocalOnly(false)
                        setSortKey("${Date().time}")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            setCategory(NotificationCompat.CATEGORY_SERVICE)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                                setChannelId(getChannelId(context, channelName))
                                setGroupAlertBehavior(
                                        NotificationCompat.GROUP_ALERT_SUMMARY)
                            }
                            setVisibility(visibility)
                        }
                    }
                }
                .build())
    }

    override fun show(notification: IdentifiedNotification) {
        NotificationManagerCompat.from(context).notify(
                notification.id, notification.delegate)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getChannelImportance(@NotificationPriority priority: Int) = when (priority) {
    NotificationManager.PRIORITY_LOW -> android.app.NotificationManager.IMPORTANCE_LOW
    NotificationManager.PRIORITY_MEDIUM -> android.app.NotificationManager.IMPORTANCE_DEFAULT
    NotificationManager.PRIORITY_HIGH -> android.app.NotificationManager.IMPORTANCE_HIGH
    else -> throw IllegalArgumentException("Illegal priority $priority")
}

/**
 * Required because a channel name may (and should) be localized, but the id must not be.
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
private fun getChannelId(context: Context, @StringRes channelName: Int) =
        Configuration(context.resources.configuration).run {
            setLocale(Locale.ENGLISH)
            context.createConfigurationContext(this).resources.getString(channelName)
        }
