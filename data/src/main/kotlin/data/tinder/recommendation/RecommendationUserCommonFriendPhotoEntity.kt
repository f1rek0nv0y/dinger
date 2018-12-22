package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index("small")])
internal class RecommendationUserCommonFriendPhotoEntity(
    @PrimaryKey
    var small: String,
    var medium: String,
    var large: String)
