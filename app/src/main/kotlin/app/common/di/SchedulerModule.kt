package app.common.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class SchedulerModule {
  @Provides
  @Named("io")
  @Singleton
  fun ioScheduler(): Scheduler = Schedulers.io()

  @Provides
  @Named("main")
  @Singleton
  fun mainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()
}
