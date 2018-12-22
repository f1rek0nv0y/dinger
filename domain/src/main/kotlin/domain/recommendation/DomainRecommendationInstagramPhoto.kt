package domain.recommendation

data class DomainRecommendationInstagramPhoto(
    val link: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val ts: String) {
  companion object {
    val NONE = DomainRecommendationInstagramPhoto(
        link = "",
        imageUrl = "",
        thumbnailUrl = "",
        ts = "")
  }
}
