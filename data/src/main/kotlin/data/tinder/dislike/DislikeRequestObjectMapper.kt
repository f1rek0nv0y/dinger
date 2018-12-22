package data.tinder.dislike

import data.ObjectMapper
import domain.recommendation.DomainRecommendationUser

internal class DislikeRequestObjectMapper
  : ObjectMapper<DomainRecommendationUser, String> {
  override fun from(source: DomainRecommendationUser) = source.id
}
