package app.alarmbanner

import android.content.Context
import app.EntryScreenScope
import dagger.Module
import dagger.Provides
import reporter.CrashReporter

@Module
internal class AutoSwipeTriggerModule {
  @Provides
  @EntryScreenScope
  fun coordinator(
      context: Context,
      crashReporter: CrashReporter) = AlarmBannerCoordinator(context, crashReporter)
}
