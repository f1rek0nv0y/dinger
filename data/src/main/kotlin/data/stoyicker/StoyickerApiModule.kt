package data.stoyicker

import dagger.Module
import dagger.Provides
import data.network.NetworkClientModule
import data.network.NetworkModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkClientModule::class, NetworkModule::class])
internal class StoyickerApiModule {
  @Provides
  @Singleton
  fun tinderApi(
      clientBuilder: OkHttpClient.Builder,
      retrofitBuilder: Retrofit.Builder): StoyickerApi = retrofitBuilder
      .client(clientBuilder.build())
      .baseUrl(StoyickerApi.BASE_URL)
      .build()
      .create(StoyickerApi::class.java)
}
