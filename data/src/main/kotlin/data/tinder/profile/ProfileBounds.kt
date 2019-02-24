package data.tinder.profile

import com.squareup.moshi.Json
import domain.profile.ProfileCoordinates

internal class ProfileBounds private constructor(
    @field:Json(name = "ne")
    val ne: ProfileCoordinates,
    @field:Json(name = "sw")
    val sw: ProfileCoordinates)
