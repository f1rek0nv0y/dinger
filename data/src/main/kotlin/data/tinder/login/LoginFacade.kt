package data.tinder.login

import data.ObjectMapper
import data.network.RequestFacade
import domain.login.DomainAuthRequestParameters
import domain.login.DomainAuthenticatedUser

internal class LoginFacade(
    source: LoginSource,
    requestMapper: ObjectMapper<DomainAuthRequestParameters, LoginRequestParameters>,
    responseMapper: ObjectMapper<LoginResponse, DomainAuthenticatedUser>)
  : RequestFacade<
    DomainAuthRequestParameters, LoginRequestParameters, LoginResponse, DomainAuthenticatedUser>(
    source, requestMapper, responseMapper)
