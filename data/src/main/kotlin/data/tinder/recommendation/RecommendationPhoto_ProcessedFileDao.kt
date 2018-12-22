package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

@Dao
internal interface RecommendationPhoto_ProcessedFileDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertPhoto_ProcessedFile(
      bond: RecommendationUserPhotoEntity_RecommendationUserPhotoProcessedFileEntity)
}
