package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index("recommendationUserInstagramEntityUsername")],
    primaryKeys = [
      "recommendationUserInstagramEntityUsername",
      "recommendationUserInstagramPhotoEntityLink"])
internal class RecommendationUserInstagramEntity_RecommendationUserInstagramPhotoEntity(
    var recommendationUserInstagramEntityUsername: String,
    var recommendationUserInstagramPhotoEntityLink: String)
