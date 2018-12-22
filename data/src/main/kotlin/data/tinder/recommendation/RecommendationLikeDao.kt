package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
internal interface RecommendationLikeDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertLike(like: RecommendationLikeEntity)

  @Query("SELECT * from RecommendationLikeEntity WHERE id=:id")
  fun selectLikeById(id: String): List<RecommendationLikeEntity>
}
