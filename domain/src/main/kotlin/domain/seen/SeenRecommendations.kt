package domain.seen

import android.arch.paging.DataSource
import domain.recommendation.DomainRecommendationUser

interface SeenRecommendations {
  fun filter(filter: String): DataSource.Factory<Int, DomainRecommendationUser>
}
