package app.tinder.me

import android.content.Context
import app.di.PerActivity
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Named

@Module
@PerActivity
internal class MeModule(private val context: Context) {
  @Provides
  fun logoutCoordinator(@Named("main") postExecutionScheduler: Scheduler) =
      LogoutCoordinator(context, postExecutionScheduler)
}
