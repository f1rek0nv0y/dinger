package data.notification

import android.content.Context

internal interface NotificationID {
  fun next(context: Context): Int
}
