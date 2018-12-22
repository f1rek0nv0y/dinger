package data.tinder.recommendation

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

internal class RecommendationUserSpotifyThemeTrackAlbumWithRelatives(
    @Embedded
    var recommendationUserSpotifyThemeTrackAlbum: RecommendationUserSpotifyThemeTrackAlbumEntity,
    @Relation(parentColumn = "id",
        entityColumn = "recommendationUserSpotifyThemeTrackAlbumEntityId",
        entity = RecommendationUserSpotifyThemeTrackAlbumEntity_RecommendationUserPhotoProcessedFileEntity::class,
        projection = ["recommendationUserPhotoProcessedFileEntityUrl"])
    var images: Set<String>) {
  constructor() : this(RecommendationUserSpotifyThemeTrackAlbumEntity.NONE, emptySet())
}
