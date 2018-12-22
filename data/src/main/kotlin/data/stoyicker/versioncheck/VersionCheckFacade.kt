package data.stoyicker.versioncheck

import data.ObjectMapper
import data.network.RequestFacade
import domain.versioncheck.DomainVersionCheckResponse

internal class VersionCheckFacade(
    source: VersionCheckSource,
    responseMapper: ObjectMapper<VersionCheckResponse, DomainVersionCheckResponse>)
  : RequestFacade<Unit, Unit, VersionCheckResponse, DomainVersionCheckResponse>(
    source,
    object : ObjectMapper<Unit, Unit> {
      override fun from(source: Unit) = Unit
    },
    responseMapper)
