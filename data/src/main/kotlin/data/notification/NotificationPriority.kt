package data.notification

import android.support.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(NotificationManager.PRIORITY_MEDIUM,
    NotificationManager.PRIORITY_HIGH,
    NotificationManager.PRIORITY_LOW)
internal annotation class NotificationPriority
