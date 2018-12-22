package data.tinder.like

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [LikeSourceModule::class, LikeEventTrackerModule::class])
internal class LikeFacadeModule {
  @Provides
  @Singleton
  fun requestObjectMapper() = LikeRequestObjectMapper()

  @Provides
  @Singleton
  fun responseObjectMapper(eventTracker: LikeEventTracker) =
      LikeResponseObjectMapper(eventTracker)

  @Provides
  @Singleton
  fun facade(
      source: LikeSource,
      requestObjectMapper: LikeRequestObjectMapper,
      responseObjectMapper: LikeResponseObjectMapper) =
      LikeFacade(source, requestObjectMapper, responseObjectMapper)
}
