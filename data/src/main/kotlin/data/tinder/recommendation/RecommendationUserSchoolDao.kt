package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
internal interface RecommendationUserSchoolDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertSchool(school: RecommendationUserSchoolEntity)

  @Query("SELECT * from RecommendationUserSchoolEntity WHERE id=:id")
  fun selectSchoolById(id: String): List<RecommendationUserSchoolEntity>
}
