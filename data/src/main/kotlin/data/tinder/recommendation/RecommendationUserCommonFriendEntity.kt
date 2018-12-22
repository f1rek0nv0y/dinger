package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index("id")])
internal open class RecommendationUserCommonFriendEntity(
    @PrimaryKey
    var id: String,
    var name: String,
    var degree: String) {
  companion object {
    val NONE = RecommendationUserCommonFriendEntity(id = "", name = "", degree = "")
  }
}
