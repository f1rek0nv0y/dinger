package data.tinder.recommendation

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

internal class RecommendationUserPhotoWithRelatives(
    @Embedded
    var recommendationUserPhotoEntity: RecommendationUserPhotoEntity,
    @Relation(parentColumn = "id", entityColumn = "recommendationUserPhotoEntityId",
        entity = RecommendationUserPhotoEntity_RecommendationUserPhotoProcessedFileEntity::class,
        projection = ["recommendationUserPhotoProcessedFileEntityUrl"])
    var processedFiles: Set<String>) {
  constructor() : this(RecommendationUserPhotoEntity.NONE, emptySet())
}
