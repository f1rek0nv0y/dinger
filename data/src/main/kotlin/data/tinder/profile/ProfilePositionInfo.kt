package data.tinder.profile

import com.squareup.moshi.Json
import domain.profile.ProfileCountry

internal class ProfilePositionInfo private constructor(
    @field:Json(name = "city")
    val city: ProfileCity,
    @field:Json(name = "country")
    val country: ProfileCountry)
