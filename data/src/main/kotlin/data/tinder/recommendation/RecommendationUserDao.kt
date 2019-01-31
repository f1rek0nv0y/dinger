package data.tinder.recommendation

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction

@Dao
internal interface RecommendationUserDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertUser(user: RecommendationUserEntity)

  @Query("SELECT * from RecommendationUserEntity WHERE id=:id")
  @Transaction
  fun selectUserById(id: String): LiveData<List<RecommendationUserWithRelatives>>

  @Query("SELECT * from RecommendationUserEntity WHERE instr(name, :filter) > 0 ORDER BY distanceMiles ASC")
  @Transaction
  fun selectUsersByFilterOnName(filter: String)
      : DataSource.Factory<Int, RecommendationUserWithRelatives>
}
