package data.tinder.recommendation

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

internal class RecommendationUserCommonFriendWithRelatives(
    @Embedded
    var recommendationUserCommonFriend: RecommendationUserCommonFriendEntity,
    @Relation(parentColumn = "id",
        entityColumn = "recommendationUserCommonFriendEntityId",
        entity = RecommendationUserCommonFriendEntity_PhotoEntity::class,
        projection = ["recommendationUserCommonFriendPhotoEntitySmall"])
    var photos: Set<String>) {
  constructor() : this(RecommendationUserCommonFriendEntity.NONE, emptySet())
}
