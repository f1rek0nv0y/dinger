package app.alarmbanner

import app.di.PerActivity
import dagger.Module
import dagger.Provides
import reporter.CrashReporter

@Module
@PerActivity
internal class AutoSwipeTriggerModule(private val activity: AlarmBannerActivity) {
  @Provides
  fun coordinator(crashReporter: CrashReporter) = AlarmBannerCoordinator(activity, crashReporter)
}
