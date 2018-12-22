package data.tinder.recommendation

import data.ObjectMapper

internal class RecommendationRequestObjectMapper : ObjectMapper<Unit, Unit> {
  override fun from(source: Unit) = Unit
}
