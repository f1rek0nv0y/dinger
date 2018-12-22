package data.tinder.recommendation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
internal interface RecommendationUserJobDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertJob(job: RecommendationUserJobEntity)

  @Query("SELECT * from RecommendationUserJobEntity WHERE id=:id")
  fun selectJobById(id: String): List<RecommendationUserJobEntity>
}
