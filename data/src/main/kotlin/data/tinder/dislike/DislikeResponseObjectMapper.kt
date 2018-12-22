package data.tinder.dislike

import data.ObjectMapper
import domain.dislike.DomainDislikedRecommendationAnswer

internal class DislikeResponseObjectMapper
  : ObjectMapper<DislikeResponse, DomainDislikedRecommendationAnswer> {
  override fun from(source: DislikeResponse) = source.let {
    DomainDislikedRecommendationAnswer(source.status in 200..299)
  }
}
