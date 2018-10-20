package data.tinder.login

import com.nytimes.android.external.store3.base.Fetcher
import com.nytimes.android.external.store3.base.impl.FluentStoreBuilder
import com.nytimes.android.external.store3.base.impl.StalePolicy
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.middleware.moshi.MoshiParserFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import data.crash.CrashReporterModule
import data.network.ParserModule
import data.tinder.TinderApi
import data.tinder.TinderApiModule
import okio.BufferedSource
import reporter.CrashReporter
import javax.inject.Singleton
import dagger.Lazy as DaggerLazy

@Module(includes = [
    ParserModule::class, TinderApiModule::class, CrashReporterModule::class])
internal class LoginSourceModule {
    @Provides
    @Singleton
    fun store(moshiBuilder: Moshi.Builder, api: TinderApi) =
            FluentStoreBuilder.parsedWithKey<LoginRequestParameters, BufferedSource, LoginResponse>(
                    Fetcher { fetch(it, api) }) {
                parsers = listOf(MoshiParserFactory.createSourceParser(
                    moshiBuilder.build(), LoginResponse::class.java))
                stalePolicy = StalePolicy.NETWORK_BEFORE_STALE
            }

    @Provides
    @Singleton
    fun source(store: DaggerLazy<Store<LoginResponse, LoginRequestParameters>>,
               crashReporter: CrashReporter) = LoginSource(store, crashReporter)

    private fun fetch(requestParameters: LoginRequestParameters, api: TinderApi) =
            api.login(requestParameters).map { it.source() }
}
