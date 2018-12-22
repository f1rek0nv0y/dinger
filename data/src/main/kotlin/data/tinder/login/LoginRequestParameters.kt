package data.tinder.login

import com.squareup.moshi.Json
import org.stoyicker.dinger.data.BuildConfig

internal class LoginRequestParameters(
    @field:Json(name = "id")
    private val facebookId: String,
    @field:Json(name = "token")
    private val facebookToken: String,
    @field:Json(name = "client_version")
    private val clientVersion: String = BuildConfig.TINDER_VERSION_NAME)
