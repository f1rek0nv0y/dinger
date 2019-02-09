package app

import app.alarmbanner.AlarmBannerComponent
import app.common.di.SchedulerModule
import app.crash.CrashReporterModule
import app.splash.SplashComponent
import app.tinder.login.TinderLoginComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [SchedulerModule::class, CrashReporterModule::class])
@Singleton
internal interface ApplicationComponent {
  fun inject(mainApplication: MainApplication)
  fun newSplashComponent(): SplashComponent.Builder
  fun newTinderLoginComponent(): TinderLoginComponent.Builder
  fun newAlarmBannerComponent(): AlarmBannerComponent.Builder
}
