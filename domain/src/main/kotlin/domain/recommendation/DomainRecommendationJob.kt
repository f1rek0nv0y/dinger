package domain.recommendation

data class DomainRecommendationJob(
    val id: String,
    val company: DomainRecommendationCompany?,
    val title: DomainRecommendationTitle?) {
  companion object {
    val NONE = DomainRecommendationJob(
        id = "",
        company = null,
        title = null)
  }
}
