package app.splash

import android.app.Activity
import app.EntryScreenScope
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [SplashModule::class])
@EntryScreenScope
internal interface SplashComponent {
  fun inject(target: SplashActivity)
  @Subcomponent.Builder
  interface Builder {
    @BindsInstance
    fun activity(activity: Activity): Builder
    @BindsInstance
    fun loggedInCheckResultCallback(
        loggedInCheckResultCallback: LoggedInCheckCoordinator.ResultCallback): Builder
    @BindsInstance
    fun versionCheckCoordinatorResultCallback(
        versionCheckCoordinatorResultCallback: VersionCheckCoordinator.ResultCallback): Builder
    fun build(): SplashComponent
  }
}
