package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index("recommendationUserSpotifyThemeTrackAlbumEntityId")],
    primaryKeys = [
      "recommendationUserSpotifyThemeTrackAlbumEntityId",
      "recommendationUserPhotoProcessedFileEntityUrl"])
internal class
RecommendationUserSpotifyThemeTrackAlbumEntity_RecommendationUserPhotoProcessedFileEntity(
    var recommendationUserSpotifyThemeTrackAlbumEntityId: String,
    var recommendationUserPhotoProcessedFileEntityUrl: String)
