package data.stoyicker.versioncheck

import dagger.Module
import dagger.Provides
import data.crash.CrashReporterModule
import domain.versioncheck.VersionCheck
import reporter.CrashReporter
import javax.inject.Singleton

@Module(includes = [VersionCheckFacadeModule::class, CrashReporterModule::class])
internal class VersionCheckModule {
    @Provides
    @Singleton
    fun versionCheck(versionCheckFacade: VersionCheckFacade,
                     crashReporter: CrashReporter): VersionCheck =
            VersionCheckImpl(versionCheckFacade, crashReporter)
}
