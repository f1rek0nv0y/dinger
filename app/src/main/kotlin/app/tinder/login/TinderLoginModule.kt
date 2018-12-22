package app.tinder.login

import android.support.v4.widget.ContentLoadingProgressBar
import app.di.PerActivity
import com.facebook.login.widget.LoginButton
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import reporter.CrashReporter
import javax.inject.Named

@Module
@PerActivity
internal class TinderLoginModule(
    private val activity: TinderLoginActivity,
    private val loginButton: LoginButton,
    private val contentLoadingProgressBar: ContentLoadingProgressBar,
    private val tinderFacebookLoginResultCallback: TinderFacebookLoginFeature.ResultCallback,
    private val tinderLoginCoordinatorResultCallback: TinderLoginCoordinator.ResultCallback) {
  @Provides
  fun feature(crashReporter: CrashReporter) =
      TinderFacebookLoginFeature(
          loginButton,
          tinderFacebookLoginResultCallback,
          crashReporter)

  @Provides
  fun view(): TinderLoginView =
      TinderLoginViewImpl(activity, loginButton, contentLoadingProgressBar)

  @Provides
  fun coordinator(
      view: TinderLoginView,
      @Named("io") asyncExecutionScheduler: Scheduler,
      @Named("main") postExecutionScheduler: Scheduler,
      crashReporter: CrashReporter) =
      TinderLoginCoordinator(
          view,
          asyncExecutionScheduler,
          postExecutionScheduler,
          tinderLoginCoordinatorResultCallback,
          crashReporter)
}
