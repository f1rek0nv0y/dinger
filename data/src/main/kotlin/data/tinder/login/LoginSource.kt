package data.tinder.login

import com.nytimes.android.external.store3.base.impl.Store
import data.network.RequestSource
import reporter.CrashReporter
import dagger.Lazy as DaggerLazy

internal class LoginSource(
    storeAccessor: DaggerLazy<Store<LoginResponse, LoginRequestParameters>>,
    crashReporter: CrashReporter)
  : RequestSource<LoginRequestParameters, LoginResponse>(storeAccessor.get(), crashReporter)
