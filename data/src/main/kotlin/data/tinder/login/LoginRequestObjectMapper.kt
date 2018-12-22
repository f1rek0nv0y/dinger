package data.tinder.login

import data.ObjectMapper
import domain.login.DomainAuthRequestParameters

internal class LoginRequestObjectMapper
  : ObjectMapper<DomainAuthRequestParameters, LoginRequestParameters> {
  override fun from(source: DomainAuthRequestParameters) =
      source.let {
        LoginRequestParameters(it.facebookId, it.facebookToken)
      }
}
