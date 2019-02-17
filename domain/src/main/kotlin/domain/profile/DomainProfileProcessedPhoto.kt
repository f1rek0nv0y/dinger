package domain.profile

import android.support.annotation.Px

data class DomainProfileProcessedPhoto(
    @Px
    val widthPx: Int,
    val url: String,
    @Px
    val heightPx: Int)
