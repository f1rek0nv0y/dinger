package data.tinder.like

import data.ObjectMapper
import data.network.RequestFacade
import domain.like.DomainLikedRecommendationAnswer
import domain.recommendation.DomainRecommendationUser

internal class LikeFacade(
    source: LikeSource,
    requestMapper: ObjectMapper<DomainRecommendationUser, String>,
    responseMapper: ObjectMapper<LikeResponse, DomainLikedRecommendationAnswer>)
  : RequestFacade<DomainRecommendationUser, String, LikeResponse, DomainLikedRecommendationAnswer>(
    source, requestMapper, responseMapper)
