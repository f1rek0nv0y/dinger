package data.tinder.recommendation

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

internal class RecommendationUserSpotifyThemeTrackWithRelatives(
    @Embedded
    var recommendationUserSpotifyThemeTrackEntity: RecommendationUserSpotifyThemeTrackEntity,
    @Relation(parentColumn = "id", entityColumn = "recommendationUserSpotifyThemeTrackEntityId",
        entity =
        RecommendationUserSpotifyThemeTrackEntity_RecommendationUserSpotifyThemeTrackArtistEntity::class,
        projection = ["recommendationUserSpotifyThemeTrackArtistEntityId"])
    var artists: Set<String>) {
  constructor() : this(RecommendationUserSpotifyThemeTrackEntity.NONE, emptySet())
}
