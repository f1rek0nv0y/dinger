package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index("id")])
internal class RecommendationUserPhotoEntity(
    @PrimaryKey
    var id: String,
    var url: String) {
  companion object {
    val NONE = RecommendationUserPhotoEntity(id = "", url = "")
  }
}
