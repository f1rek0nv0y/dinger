package app.home.me

import android.content.Context
import app.home.HomeScreenScope
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
internal class MeModule {
  @Provides
  @HomeScreenScope
  fun logoutCoordinator(
      context: Context,
      @Named("main") postExecutionScheduler: Scheduler) =
      LogoutCoordinator(context, postExecutionScheduler)
}
