package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index("url")])
internal class RecommendationUserPhotoProcessedFileEntity(
    var widthPx: Int,
    @PrimaryKey
    var url: String,
    var heightPx: Int)
