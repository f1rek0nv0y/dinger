package data.tinder.recommendation

import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.persistence.room.*

@Dao
internal interface RecommendationUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: RecommendationUserEntity)

    @Query("SELECT * from RecommendationUserEntity WHERE id=:id")
    @Transaction
    fun selectUserById(id: String): LiveData<List<RecommendationUserWithRelatives>>

    @Query("SELECT * from RecommendationUserEntity WHERE instr(name, :filter) > 0")
    @Transaction
    fun selectUsersByFilterOnName(filter: String)
            : DataSource.Factory<Int, RecommendationUserWithRelatives>
}
