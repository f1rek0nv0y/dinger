package data.tinder.login

import data.ObjectMapper
import domain.login.DomainAuthenticatedUser

internal class LoginResponseObjectMapper : ObjectMapper<LoginResponse, DomainAuthenticatedUser> {
  override fun from(source: LoginResponse) = source.data.let { DomainAuthenticatedUser(it.apiToken) }
}
