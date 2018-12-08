package data.autoswipe

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import data.notification.NotificationManager
import data.notification.NotificationManagerModule
import data.preferences.DefaultSharedPreferencesModule
import javax.inject.Singleton

@Module(includes = [
    NotificationManagerModule::class,
    DefaultSharedPreferencesModule::class])
internal class AutoSwipeReportHandlerModule {
    @Provides
    @Singleton
    fun autoSwipeReportHandler(
            defaultSharedPreferences: SharedPreferences,
            notificationManager: NotificationManager) =
            AutoSwipeReportHandler(notificationManager)
}
