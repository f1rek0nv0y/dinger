package app.tinder.login

import android.support.v4.widget.ContentLoadingProgressBar
import app.EntryScreenScope
import com.facebook.login.widget.LoginButton
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import reporter.CrashReporter
import javax.inject.Named

@Module
internal class TinderLoginModule {
  @Provides
  @EntryScreenScope
  fun feature(
      loginButton: LoginButton,
      tinderFacebookLoginResultCallback: TinderFacebookLoginFeature.ResultCallback,
      crashReporter: CrashReporter) =
      TinderFacebookLoginFeature(
          loginButton,
          tinderFacebookLoginResultCallback,
          crashReporter)

  @Provides
  @EntryScreenScope
  fun view(
      activity: TinderLoginActivity,
      loginButton: LoginButton,
      contentLoadingProgressBar: ContentLoadingProgressBar): TinderLoginView =
      TinderLoginViewImpl(activity, loginButton, contentLoadingProgressBar)

  @Provides
  @EntryScreenScope
  fun coordinator(
      view: TinderLoginView,
      @Named("io") asyncExecutionScheduler: Scheduler,
      @Named("main") postExecutionScheduler: Scheduler,
      tinderLoginCoordinatorResultCallback: TinderLoginCoordinator.ResultCallback,
      crashReporter: CrashReporter) =
      TinderLoginCoordinator(
          view,
          asyncExecutionScheduler,
          postExecutionScheduler,
          tinderLoginCoordinatorResultCallback,
          crashReporter)
}
