package data.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import data.RootModule
import data.autoswipe.LikeBatchTracker
import javax.inject.Singleton

@Module(includes = [RootModule::class])
internal class DefaultSharedPreferencesModule {
  @Provides
  @Singleton
  fun defaultSharedPreferences(context: Context) =
      PreferenceManager.getDefaultSharedPreferences(context)!!

  @Provides
  @Singleton
  fun likeBatchTracker(context: Context, sharedPreferences: SharedPreferences) =
      LikeBatchTracker(context, sharedPreferences)
}
