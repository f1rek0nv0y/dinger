package domain.profile

enum class DomainProfileGender {
  GENDER_MALE, GENDER_FEMALE;

  companion object {
    fun fromGenderInt(from: Int) = when(from) {
      0 -> GENDER_MALE
      1 -> GENDER_FEMALE
      else -> throw IllegalArgumentException("Illegal gender int to map from: $from")
    }
  }
}
