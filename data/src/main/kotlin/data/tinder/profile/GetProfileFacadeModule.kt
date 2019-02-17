package data.tinder.profile

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [GetProfileSourceModule::class])
internal class GetProfileFacadeModule {
  @Provides
  @Singleton
  fun requestObjectMapper() = GetProfileRequestObjectMapper()

  @Provides
  @Singleton
  fun responseObjectMapper() = GetProfileResponseObjectMapper()

  @Provides
  @Singleton
  fun facade(
      source: GetProfileSource,
      requestObjectMapper: GetProfileRequestObjectMapper,
      responseObjectMapper: GetProfileResponseObjectMapper) =
      GetProfileFacade(source, requestObjectMapper, responseObjectMapper)
}
