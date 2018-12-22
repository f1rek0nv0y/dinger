package data.tinder.recommendation

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.Date

@Entity(indices = [Index("username")])
internal open class RecommendationUserInstagramEntity(
    var profilePictureUrl: String,
    @Embedded
    var lastFetchTime: Date?,
    var mediaCount: Int,
    var completedInitialFetch: Boolean,
    @PrimaryKey
    var username: String) {
  companion object {
    val NONE = RecommendationUserInstagramEntity(
        profilePictureUrl = "",
        lastFetchTime = Date(),
        mediaCount = 0,
        completedInitialFetch = false,
        username = "")
  }
}
