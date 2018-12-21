package domain.versioncheck

data class DomainVersionCheckDescription(
        val dialogTitle: CharSequence,
        val dialogBody: CharSequence,
        val positiveButtonText: CharSequence,
        val negativeButtonText: CharSequence,
        val downloadUrl: String,
        val changelogUrl: String,
        val isUpToDate: Boolean)
