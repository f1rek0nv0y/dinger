package data.tinder.login

import com.squareup.moshi.Json

internal class LoginResponse private constructor(
    @field:Json(name = "meta")
    val meta: LoginResponseMeta,
    @field:Json(name = "data")
    val data: LoginResponseData)
