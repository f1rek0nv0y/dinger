package data.notification

import android.support.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(NotificationManager.VISIBILITY_PRIVATE,
    NotificationManager.VISIBILITY_PUBLIC,
    NotificationManager.VISIBILITY_SECRET)
internal annotation class NotificationVisibility
