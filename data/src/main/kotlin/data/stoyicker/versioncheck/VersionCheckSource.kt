package data.stoyicker.versioncheck

import com.nytimes.android.external.store3.base.impl.Store
import data.network.RequestSource
import reporter.CrashReporter
import dagger.Lazy as DaggerLazy

internal class VersionCheckSource(
    storeAccessor: DaggerLazy<Store<VersionCheckResponse, Unit>>,
    crashReporter: CrashReporter)
  : RequestSource<Unit, VersionCheckResponse>(storeAccessor.get(), crashReporter)
