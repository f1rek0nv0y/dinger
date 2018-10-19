package data.notification

import android.content.Context
import dagger.Module
import dagger.Provides
import data.RootModule
import javax.inject.Singleton

@Module(includes = [RootModule::class])
internal class NotificationManagerModule {
    @Provides
    @Singleton
    fun notificationID(): NotificationID = NotificationIDImplSingleNotification()

    @Provides
    @Singleton
    fun autoSwipeReportHandler(context: Context, notificationID: NotificationID):
            NotificationManager = NotificationManagerImpl(context, notificationID)
}
