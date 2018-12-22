package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction

@Dao
internal interface RecommendationUserInstagramDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertInstagram(instagram: RecommendationUserInstagramEntity)

  @Query("SELECT * from RecommendationUserInstagramEntity WHERE username=:username")
  @Transaction
  fun selectInstagramByUsername(username: String): List<RecommendationUserInstagramWithRelatives>
}
