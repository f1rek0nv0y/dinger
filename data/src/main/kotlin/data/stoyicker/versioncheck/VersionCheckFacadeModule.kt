package data.stoyicker.versioncheck

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import dagger.Lazy as DaggerLazy

@Module(includes = [VersionCheckSourceModule::class])
internal class VersionCheckFacadeModule {
  @Provides
  @Singleton
  fun responseObjectMapper() = VersionCheckResponseObjectMapper()

  @Provides
  @Singleton
  fun facade(source: VersionCheckSource,
             responseObjectMapper: VersionCheckResponseObjectMapper) =
      VersionCheckFacade(source, responseObjectMapper)
}
