package app.splash

import android.app.Activity
import app.di.PerActivity
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import reporter.CrashReporter
import javax.inject.Named

@Module
@PerActivity
internal class SplashModule(
    private val activity: Activity,
    private val loggedInCheckResultCallback: LoggedInCheckCoordinator.ResultCallback,
    private val versionCheckCoordinatorResultCallback: VersionCheckCoordinator.ResultCallback) {
  @Provides
  fun loggedInCheckCoordinator(
      @Named("io") asyncExecutionScheduler: Scheduler,
      @Named("main") postExecutionScheduler: Scheduler,
      crashReporter: CrashReporter) = LoggedInCheckCoordinator(
      asyncExecutionScheduler,
      postExecutionScheduler,
      loggedInCheckResultCallback,
      crashReporter)

  @Provides
  fun versionCheckCoordinator(
      @Named("io") asyncExecutionScheduler: Scheduler,
      @Named("main") postExecutionScheduler: Scheduler) = VersionCheckCoordinator(
      activity,
      asyncExecutionScheduler,
      postExecutionScheduler,
      versionCheckCoordinatorResultCallback)
}
