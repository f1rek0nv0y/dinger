package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index("recommendationUserEntityId")],
    primaryKeys = ["recommendationUserEntityId", "recommendationUserTeaserEntityId"])
internal class RecommendationUserEntity_RecommendationUserTeaserEntity(
    var recommendationUserEntityId: String,
    var recommendationUserTeaserEntityId: String)
