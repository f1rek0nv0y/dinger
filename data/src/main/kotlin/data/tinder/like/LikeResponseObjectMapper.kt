package data.tinder.like

import data.ObjectMapper
import domain.like.DomainLikedRecommendationAnswer

internal class LikeResponseObjectMapper(private val eventTracker: LikeEventTracker)
  : ObjectMapper<LikeResponse, DomainLikedRecommendationAnswer> {
  override fun from(source: LikeResponse) = source.let {
    eventTracker.track(it)
    DomainLikedRecommendationAnswer(it.match != null, it.rateLimitedUntil)
  }
}
