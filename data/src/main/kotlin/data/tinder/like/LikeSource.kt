package data.tinder.like

import com.nytimes.android.external.store3.base.impl.Store
import dagger.Lazy
import data.network.RequestSource
import reporter.CrashReporter

internal class LikeSource(
    storeAccessor: Lazy<Store<LikeResponse, String>>,
    crashReporter: CrashReporter)
  : RequestSource<String, LikeResponse>(storeAccessor.get(), crashReporter)
