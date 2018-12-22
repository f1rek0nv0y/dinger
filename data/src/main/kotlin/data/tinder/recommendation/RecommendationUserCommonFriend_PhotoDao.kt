package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy

@Dao
internal interface RecommendationUserCommonFriend_PhotoDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertCommonFriend_Photo(bond: RecommendationUserCommonFriendEntity_PhotoEntity)
}
