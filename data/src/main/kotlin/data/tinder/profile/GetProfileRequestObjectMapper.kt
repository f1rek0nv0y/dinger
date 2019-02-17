package data.tinder.profile

import data.ObjectMapper

internal class GetProfileRequestObjectMapper : ObjectMapper<Unit, Unit> {
  override fun from(source: Unit) = Unit
}
