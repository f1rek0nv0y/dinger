package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index("recommendationUserEntityId")],
    primaryKeys = ["recommendationUserEntityId", "recommendationLikeEntityId"])
internal class RecommendationUserEntity_RecommendationLikeEntity(
    var recommendationUserEntityId: String,
    var recommendationLikeEntityId: String)
