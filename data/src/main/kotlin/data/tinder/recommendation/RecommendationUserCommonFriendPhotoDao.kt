package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

@Dao
internal interface RecommendationUserCommonFriendPhotoDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertPhoto(photo: RecommendationUserCommonFriendPhotoEntity)
}
