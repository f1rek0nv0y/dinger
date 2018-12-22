package data

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import data.database.AppDatabase
import javax.inject.Singleton

@Module
internal class RootModule(private val context: Context) {
  @Provides
  @Singleton
  fun context() = context

  @Provides
  @Singleton
  fun database(context: Context): AppDatabase = Room.databaseBuilder(
      context,
      AppDatabase::class.java,
      "AppDatabase")
      .build()
}
