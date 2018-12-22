package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
internal interface RecommendationUserInstagramPhotoDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertInstagramPhoto(photo: RecommendationUserInstagramPhotoEntity)

  @Query("SELECT * from RecommendationUserInstagramPhotoEntity WHERE link=:link")
  fun selectInstagramPhotoByLink(link: String): List<RecommendationUserInstagramPhotoEntity>
}
