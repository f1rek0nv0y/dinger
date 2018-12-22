package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index("recommendationUserEntityId")],
    primaryKeys = ["recommendationUserEntityId", "recommendationUserSchoolEntityName"])
internal class RecommendationUserEntity_RecommendationUserSchoolEntity(
    var recommendationUserEntityId: String,
    var recommendationUserSchoolEntityName: String)
