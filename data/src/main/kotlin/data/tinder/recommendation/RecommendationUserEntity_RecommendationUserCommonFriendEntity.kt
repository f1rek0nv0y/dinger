package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index("recommendationUserEntityId")],
    primaryKeys = ["recommendationUserEntityId", "recommendationUserCommonFriendEntityId"])
internal class RecommendationUserEntity_RecommendationUserCommonFriendEntity(
    var recommendationUserEntityId: String,
    var recommendationUserCommonFriendEntityId: String)
