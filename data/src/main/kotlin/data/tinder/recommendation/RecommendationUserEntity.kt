package data.tinder.recommendation

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import java.util.Date

@Entity(indices = [Index("id"),
  Index("name"),
  Index("instagram"),
  Index("teaser"),
  Index("spotifyThemeTrack")],
    foreignKeys = [
      ForeignKey(entity = RecommendationUserInstagramEntity::class,
          parentColumns = ["username"],
          childColumns = ["instagram"]),
      ForeignKey(entity = RecommendationUserTeaserEntity::class,
          parentColumns = ["id"],
          childColumns = ["teaser"]),
      ForeignKey(entity = RecommendationUserSpotifyThemeTrackEntity::class,
          parentColumns = ["id"],
          childColumns = ["spotifyThemeTrack"])])
internal open class RecommendationUserEntity(
    var bio: String?,
    var distanceMiles: Int,
    var commonFriendCount: Int,
    var commonLikeCount: Int,
    var contentHash: String,
    @PrimaryKey
    var id: String,
    @Embedded
    var birthDate: Date?,
    var name: String,
    var instagram: String?,
    var teaser: String,
    var sNumber: Int,
    var spotifyThemeTrack: String?,
    var gender: Int,
    var birthDateInfo: String,
    var groupMatched: Boolean,
    var liked: Boolean = false,
    var matched: Boolean = false) {
  companion object {
    val NONE = RecommendationUserEntity(
        bio = null,
        distanceMiles = 0,
        commonFriendCount = 0,
        commonLikeCount = 0,
        contentHash = "",
        id = "",
        birthDate = Date(),
        name = "",
        instagram = null,
        teaser = "",
        sNumber = 0,
        spotifyThemeTrack = null,
        gender = 0,
        birthDateInfo = "",
        groupMatched = false)
  }
}
