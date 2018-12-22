package data.tinder.like

import data.ObjectMapper
import domain.recommendation.DomainRecommendationUser

internal class LikeRequestObjectMapper
  : ObjectMapper<DomainRecommendationUser, String> {
  override fun from(source: DomainRecommendationUser) = source.id
}
