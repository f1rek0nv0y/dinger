package data.tinder.profile

import data.ObjectMapper
import data.network.OfflineEnabledRequestFacade
import domain.profile.DomainGetProfileAnswer

internal class GetProfileFacade(
    source: GetProfileSource,
    requestMapper: ObjectMapper<Unit, Unit>,
    responseMapper: ObjectMapper<GetProfileResponse, DomainGetProfileAnswer>)
  : OfflineEnabledRequestFacade<Unit, Unit, GetProfileResponse, DomainGetProfileAnswer>(
    source, requestMapper, responseMapper)
