package domain.versioncheck

data class DomainVersionCheckResponse(
    val dialogTitle: CharSequence,
    val dialogBody: CharSequence,
    val positiveButtonText: CharSequence,
    val negativeButtonText: CharSequence,
    val downloadUrl: String,
    val changelogUrl: String,
    val newVersion: Int)
