package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
internal interface RecommendationProcessedFileDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertProcessedFile(file: RecommendationUserPhotoProcessedFileEntity)

  @Query("SELECT * from RecommendationUserPhotoProcessedFileEntity WHERE url=:url")
  fun selectProcessedFileByUrl(url: String): List<RecommendationUserPhotoProcessedFileEntity>
}
