package domain.recommendation

import android.support.annotation.Px

data class DomainRecommendationProcessedFile(
    @Px
    val widthPx: Int,
    val url: String,
    @Px
    val heightPx: Int) {
  companion object {
    val NONE = DomainRecommendationProcessedFile(widthPx = 0, url = "", heightPx = 0)
  }
}
