package data.crash

import dagger.Module
import dagger.Provides
import reporter.CrashReporters
import javax.inject.Named
import javax.inject.Singleton

@Module
internal class CrashReporterModule {
    @Provides
    @Singleton
    @Named("void")
    fun void() = CrashReporters.void()

    @Provides
    @Singleton
    @Named("bugsnag")
    fun bugsnag() = CrashReporters.bugsnag()
}
