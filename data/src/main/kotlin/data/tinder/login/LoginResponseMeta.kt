package data.tinder.login

import com.squareup.moshi.Json

internal class LoginResponseMeta private constructor(
    @field:Json(name = "status")
    val status: Int)
