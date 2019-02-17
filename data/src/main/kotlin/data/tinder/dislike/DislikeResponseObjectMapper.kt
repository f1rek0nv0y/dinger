package data.tinder.dislike

import data.ObjectMapper
import domain.dislike.DomainDislikedRecommendationAnswer

internal class DislikeResponseObjectMapper
  : ObjectMapper<DislikeResponse, DomainDislikedRecommendationAnswer> {
  override fun from(source: DislikeResponse) =
      DomainDislikedRecommendationAnswer(source.status in 200..299)
}
