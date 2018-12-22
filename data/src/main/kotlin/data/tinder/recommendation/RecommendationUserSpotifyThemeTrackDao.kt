package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction

@Dao
internal interface RecommendationUserSpotifyThemeTrackDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertSpotifyThemeTrack(track: RecommendationUserSpotifyThemeTrackEntity)

  @Query("SELECT * from RecommendationUserSpotifyThemeTrackEntity WHERE id=:id")
  @Transaction
  fun selectSpotifyThemeTrackById(id: String)
      : List<RecommendationUserSpotifyThemeTrackWithRelatives>
}
