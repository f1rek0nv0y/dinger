package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index("id")])
internal class RecommendationUserTeaserEntity(
    @PrimaryKey
    var id: String,
    var description: String,
    var type: String?) {
  companion object {
    fun createId(description: String, type: String?) = "${description}_$type"
  }
}
