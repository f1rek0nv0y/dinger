package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index("recommendationUserEntityId")],
    primaryKeys = ["recommendationUserEntityId", "recommendationUserPhotoEntityId"])
internal class RecommendationUserEntity_RecommendationUserPhotoEntity(
    var recommendationUserEntityId: String,
    var recommendationUserPhotoEntityId: String)
