package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

@Dao
internal interface RecommendationSpotifyThemeTrack_ArtistDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertSpotifyThemeTrack_Artist(
      bond: RecommendationUserSpotifyThemeTrackEntity_RecommendationUserSpotifyThemeTrackArtistEntity)
}
