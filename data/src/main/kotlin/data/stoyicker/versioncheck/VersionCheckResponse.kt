package data.stoyicker.versioncheck

import com.squareup.moshi.Json

internal class VersionCheckResponse private constructor(
        @field:Json(name = "title")
        val title: String,
        @field:Json(name = "body")
        val body: String,
        @field:Json(name = "positive_button")
        val positiveButtonText: String,
        @field:Json(name = "negative_button")
        val negativeButtonText: String,
        @field:Json(name = "download_url")
        val downloadUrl: String,
        @field:Json(name = "changelog_url")
        val changelogUrl: String,
        @field:Json(name = "version")
        val version: Int)
