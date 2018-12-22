package data.tinder.like

import com.nytimes.android.external.store3.base.Fetcher
import com.nytimes.android.external.store3.base.impl.FluentStoreBuilder
import com.nytimes.android.external.store3.base.impl.StalePolicy
import com.nytimes.android.external.store3.base.impl.Store
import com.nytimes.android.external.store3.middleware.moshi.MoshiParserFactory
import com.squareup.moshi.Moshi
import dagger.Lazy
import dagger.Module
import dagger.Provides
import data.crash.CrashReporterModule
import data.network.JsonObjectOrFalseAdapter
import data.network.JsonObjectOrFalseAdapterModule
import data.network.ParserModule
import data.tinder.TinderApi
import data.tinder.TinderApiModule
import okio.BufferedSource
import reporter.CrashReporter
import javax.inject.Singleton

@Module(includes = [
  JsonObjectOrFalseAdapterModule::class,
  ParserModule::class,
  TinderApiModule::class,
  CrashReporterModule::class])
internal class LikeSourceModule {
  @Provides
  @Singleton
  fun store(jsonObjectOrFalseAdapterFactory: JsonObjectOrFalseAdapter.Factory,
            moshiBuilder: Moshi.Builder,
            api: TinderApi) =
      FluentStoreBuilder.parsedWithKey<String, BufferedSource, LikeResponse>(
          Fetcher { fetch(it, api) }) {
        parsers = listOf(MoshiParserFactory.createSourceParser(
            moshiBuilder
                .add(jsonObjectOrFalseAdapterFactory)
                .build(),
            LikeResponse::class.java))
        stalePolicy = StalePolicy.NETWORK_BEFORE_STALE
      }

  @Provides
  @Singleton
  fun source(store: Lazy<Store<LikeResponse, String>>,
             crashReporter: CrashReporter) = LikeSource(store, crashReporter)

  private fun fetch(requestParameters: String, api: TinderApi) =
      api.like(requestParameters).map { it.source() }
}
