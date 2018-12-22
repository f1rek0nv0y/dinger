package data.stoyicker.versioncheck

import com.nytimes.android.external.store3.base.Fetcher
import com.nytimes.android.external.store3.base.impl.FluentMemoryPolicyBuilder
import com.nytimes.android.external.store3.base.impl.FluentStoreBuilder
import com.nytimes.android.external.store3.base.impl.StalePolicy
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.middleware.moshi.MoshiParserFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import data.crash.CrashReporterModule
import data.network.ParserModule
import data.stoyicker.StoyickerApi
import data.stoyicker.StoyickerApiModule
import okio.BufferedSource
import reporter.CrashReporter
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import dagger.Lazy as DaggerLazy

@Module(includes = [
  ParserModule::class, StoyickerApiModule::class, CrashReporterModule::class])
internal class VersionCheckSourceModule {
  @Provides
  @Singleton
  fun store(moshiBuilder: Moshi.Builder, api: StoyickerApi) =
      FluentStoreBuilder.parsedWithKey<Unit, BufferedSource, VersionCheckResponse>(
          Fetcher { fetch(api) }) {
        parsers = listOf(MoshiParserFactory.createSourceParser(
            moshiBuilder.build(), VersionCheckResponse::class.java))
        memoryPolicy = FluentMemoryPolicyBuilder.build {
          expireAfterWrite = 1
          expireAfterTimeUnit = TimeUnit.SECONDS
          memorySize = 0
        }
        stalePolicy = StalePolicy.REFRESH_ON_STALE
      }

  @Provides
  @Singleton
  fun source(store: DaggerLazy<Store<VersionCheckResponse, Unit>>,
             crashReporter: CrashReporter) = VersionCheckSource(store, crashReporter)

  private fun fetch(api: StoyickerApi) = api.versionCheck().map { it.source() }
}
