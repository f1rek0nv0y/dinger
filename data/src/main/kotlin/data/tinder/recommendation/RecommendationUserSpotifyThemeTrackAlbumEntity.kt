package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index("id")])
internal class RecommendationUserSpotifyThemeTrackAlbumEntity(
    var name: String,
    @PrimaryKey
    var id: String) {
  companion object {
    val NONE = RecommendationUserSpotifyThemeTrackAlbumEntity(name = "", id = "")
  }
}
