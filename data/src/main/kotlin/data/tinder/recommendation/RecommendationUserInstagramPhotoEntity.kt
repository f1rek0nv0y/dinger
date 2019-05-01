package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index("link")])
internal class RecommendationUserInstagramPhotoEntity constructor(
    @PrimaryKey
    var link: String,
    var imageUrl: String,
    var thumbnailUrl: String,
    var ts: String)
