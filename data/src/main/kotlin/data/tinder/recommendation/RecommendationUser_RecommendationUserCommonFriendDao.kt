package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

@Dao
internal interface RecommendationUser_RecommendationUserCommonFriendDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertUser_CommonFriend(
      bond: RecommendationUserEntity_RecommendationUserCommonFriendEntity)
}
