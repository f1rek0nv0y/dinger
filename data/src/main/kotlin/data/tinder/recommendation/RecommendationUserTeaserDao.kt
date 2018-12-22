package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
internal interface RecommendationUserTeaserDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertTeaser(teaser: RecommendationUserTeaserEntity)

  @Query("SELECT * from RecommendationUserTeaserEntity WHERE id=:id")
  fun selectTeaserById(id: String): List<RecommendationUserTeaserEntity>
}
