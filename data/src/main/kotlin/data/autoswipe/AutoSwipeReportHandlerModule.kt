package data.autoswipe

import dagger.Module
import dagger.Provides
import data.notification.NotificationManager
import data.notification.NotificationManagerModule
import javax.inject.Singleton

@Module(includes = [NotificationManagerModule::class])
internal class AutoSwipeReportHandlerModule {
    @Provides
    @Singleton
    fun autoSwipeReportHandler(notificationManager: NotificationManager) =
            AutoSwipeReportHandler(notificationManager)
}
