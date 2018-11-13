package data.notification

import android.content.Context

internal class NotificationIDImplSingleNotification : NotificationID {
    private companion object {
        const val ID = 1
    }

    override fun next(context: Context) = ID
}
