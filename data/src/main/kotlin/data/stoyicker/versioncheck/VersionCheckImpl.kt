package data.stoyicker.versioncheck

import domain.versioncheck.DomainVersionCheckResponse
import domain.versioncheck.VersionCheck
import io.reactivex.Single
import reporter.CrashReporter

internal class VersionCheckImpl(
    private val versionCheckFacade: VersionCheckFacade,
    private val crashReporter: CrashReporter) : VersionCheck {
  override fun versionCheck(): Single<DomainVersionCheckResponse> =
      versionCheckFacade.fetch(Unit).doOnError { crashReporter.report(it) }
}
