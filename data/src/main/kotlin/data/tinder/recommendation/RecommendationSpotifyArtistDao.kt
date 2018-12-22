package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
internal interface RecommendationSpotifyArtistDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertArtist(artist: RecommendationUserSpotifyThemeTrackArtistEntity)

  @Query("SELECT * from RecommendationUserSpotifyThemeTrackArtistEntity WHERE id=:id")
  fun selectArtistById(id: String): List<RecommendationUserSpotifyThemeTrackArtistEntity>
}
