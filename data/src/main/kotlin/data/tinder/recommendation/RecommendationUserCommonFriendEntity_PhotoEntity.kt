package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index

@Entity(indices = [Index("recommendationUserCommonFriendEntityId")],
    primaryKeys = [
      "recommendationUserCommonFriendEntityId",
      "recommendationUserCommonFriendPhotoEntitySmall"])
internal class RecommendationUserCommonFriendEntity_PhotoEntity(
    var recommendationUserCommonFriendEntityId: String,
    var recommendationUserCommonFriendPhotoEntitySmall: String)
