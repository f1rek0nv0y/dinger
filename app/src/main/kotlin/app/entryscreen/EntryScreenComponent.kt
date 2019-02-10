package app.entryscreen

import app.entryscreen.alarmbanner.AlarmBannerComponent
import app.common.di.SchedulerModule
import app.crash.CrashReporterModule
import app.entryscreen.splash.SplashComponent
import app.entryscreen.login.TinderLoginComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [CrashReporterModule::class, SchedulerModule::class])
@Singleton
internal interface EntryScreenComponent {
  fun newSplashComponent(): SplashComponent.Builder
  fun newTinderLoginComponent(): TinderLoginComponent.Builder
  fun newAlarmBannerComponent(): AlarmBannerComponent.Builder
}
