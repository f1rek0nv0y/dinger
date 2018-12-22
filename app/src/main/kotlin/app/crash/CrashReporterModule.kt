package app.crash

import dagger.Module
import dagger.Provides
import reporter.CrashReporter
import reporter.CrashReporters
import javax.inject.Singleton

@Module
internal class CrashReporterModule {
  @Provides
  @Singleton
  fun bugsnag(): CrashReporter = CrashReporters.bugsnag()
}
