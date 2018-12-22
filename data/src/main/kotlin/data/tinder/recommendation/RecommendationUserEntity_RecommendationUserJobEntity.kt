package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index("recommendationUserEntityId")],
    primaryKeys = ["recommendationUserEntityId", "recommendationUserJobEntityId"])
internal class RecommendationUserEntity_RecommendationUserJobEntity(
    var recommendationUserEntityId: String,
    var recommendationUserJobEntityId: String)
