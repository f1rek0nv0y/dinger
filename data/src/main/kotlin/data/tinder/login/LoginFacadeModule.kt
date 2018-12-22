package data.tinder.login

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import dagger.Lazy as DaggerLazy

@Module(includes = [LoginSourceModule::class])
internal class LoginFacadeModule {
  @Provides
  @Singleton
  fun requestObjectMapper() = LoginRequestObjectMapper()

  @Provides
  @Singleton
  fun responseObjectMapper() = LoginResponseObjectMapper()

  @Provides
  @Singleton
  fun facade(
      source: LoginSource,
      requestObjectMapper: LoginRequestObjectMapper,
      responseObjectMapper: LoginResponseObjectMapper) =
      LoginFacade(source, requestObjectMapper, responseObjectMapper)
}
