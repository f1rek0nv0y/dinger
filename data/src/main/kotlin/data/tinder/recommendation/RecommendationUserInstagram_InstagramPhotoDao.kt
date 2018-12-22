package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

@Dao
internal interface RecommendationUserInstagram_InstagramPhotoDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertInstagram_Photo(
      bond: RecommendationUserInstagramEntity_RecommendationUserInstagramPhotoEntity)
}
