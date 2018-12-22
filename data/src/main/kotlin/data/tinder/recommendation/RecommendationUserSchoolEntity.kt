package data.tinder.recommendation

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
internal class RecommendationUserSchoolEntity(
    @PrimaryKey
    var name: String,
    var id: String?)
