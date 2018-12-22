package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index("recommendationUserSpotifyThemeTrackEntityId")],
    primaryKeys = [
      "recommendationUserSpotifyThemeTrackEntityId",
      "recommendationUserSpotifyThemeTrackArtistEntityId"])
internal class
RecommendationUserSpotifyThemeTrackEntity_RecommendationUserSpotifyThemeTrackArtistEntity(
    var recommendationUserSpotifyThemeTrackEntityId: String,
    var recommendationUserSpotifyThemeTrackArtistEntityId: String)
