package data.tinder.login

import domain.login.DomainAuthRequestParameters
import domain.login.DomainAuthenticatedUser
import domain.login.Login
import io.reactivex.Single
import reporter.CrashReporter

internal class LoginImpl(
    private val loginFacade: LoginFacade,
    private val crashReporter: CrashReporter) : Login {
  override fun login(parameters: DomainAuthRequestParameters): Single<DomainAuthenticatedUser> =
      loginFacade.fetch(parameters).doOnError { crashReporter.report(it) }
}
