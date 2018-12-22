package data.stoyicker.versioncheck

import data.ObjectMapper
import domain.versioncheck.DomainVersionCheckResponse

internal class VersionCheckResponseObjectMapper : ObjectMapper<VersionCheckResponse, DomainVersionCheckResponse> {
  override fun from(source: VersionCheckResponse) = source.run {
    DomainVersionCheckResponse(
        dialogTitle = title,
        dialogBody = body,
        positiveButtonText = positiveButtonText,
        negativeButtonText = negativeButtonText,
        downloadUrl = downloadUrl,
        changelogUrl = changelogUrl,
        newVersion = version)
  }
}
