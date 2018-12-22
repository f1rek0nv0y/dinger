package data.tinder.dislike

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DislikeSourceModule::class])
internal class DislikeFacadeModule {
  @Provides
  @Singleton
  fun requestObjectMapper() = DislikeRequestObjectMapper()

  @Provides
  @Singleton
  fun responseObjectMapper() = DislikeResponseObjectMapper()

  @Provides
  @Singleton
  fun facade(
      source: DislikeSource,
      requestObjectMapper: DislikeRequestObjectMapper,
      responseObjectMapper: DislikeResponseObjectMapper) =
      DislikeFacade(source, requestObjectMapper, responseObjectMapper)
}
