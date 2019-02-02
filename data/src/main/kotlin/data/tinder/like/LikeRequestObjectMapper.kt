package data.tinder.like

import data.ObjectMapper
import domain.recommendation.DomainRecommendationUser

internal class LikeRequestObjectMapper
  : ObjectMapper<DomainRecommendationUser, LikeRequestParameters> {
  override fun from(source: DomainRecommendationUser) = LikeRequestParameters(
      source.id,
      LikeRatingRequest(
          contentHash = source.contentHash,
          photoId = source.photos.firstOrNull()?.id ?: "",
          placeId = source.jobs.firstOrNull()?.id ?: source.schools.firstOrNull()?.id ?: "",
          sNumber = source.sNumber)
      )
}
