package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

@Dao
internal interface RecommendationSpotifyAlbum_ProcessedFileDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertSpotifyAlbum_ProcessedFile(
      bond: RecommendationUserSpotifyThemeTrackAlbumEntity_RecommendationUserPhotoProcessedFileEntity)
}
