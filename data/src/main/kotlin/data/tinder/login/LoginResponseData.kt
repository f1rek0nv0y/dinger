package data.tinder.login

import com.squareup.moshi.Json

internal class LoginResponseData private constructor(
    @field:Json(name = "is_new_user")
    val isNewUser: Boolean,
    @field:Json(name = "api_token")
    val apiToken: String)
