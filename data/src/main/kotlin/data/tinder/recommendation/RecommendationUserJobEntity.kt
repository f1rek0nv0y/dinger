package data.tinder.recommendation

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = [Index("id")])
internal class RecommendationUserJobEntity(
    @PrimaryKey
    var id: String,
    @Embedded(prefix = "company_")
    var company: RecommendationUserJobCompany?,
    @Embedded(prefix = "title_")
    var title: RecommendationUserJobTitle?)
