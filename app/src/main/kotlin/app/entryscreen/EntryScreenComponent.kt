package app.entryscreen

import app.common.di.SchedulerModule
import app.crash.CrashReporterModule
import app.entryscreen.alarmbanner.AlarmBannerComponent
import app.entryscreen.login.TinderLoginComponent
import app.entryscreen.splash.SplashComponent
import dagger.Component
import javax.inject.Singleton

@Component(modules = [CrashReporterModule::class, SchedulerModule::class])
@Singleton
internal interface EntryScreenComponent {
  fun newSplashComponentBuilder(): SplashComponent.Builder
  fun newTinderLoginComponentBuilder(): TinderLoginComponent.Builder
  fun newAlarmBannerComponentBuilder(): AlarmBannerComponent.Builder
}
