package data.tinder

import data.tinder.like.LikeRatingRequest
import data.tinder.login.LoginRequestParameters
import io.reactivex.Single
import okhttp3.ResponseBody
import org.stoyicker.dinger.data.BuildConfig
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface TinderApi {
  @POST("/v2/auth/login/facebook")
  fun login(@Body requestParameters: LoginRequestParameters): Single<ResponseBody>

  @GET("/recs/core?locale=${BuildConfig.TINDER_API_LOCALE}")
  fun getRecommendations(): Single<ResponseBody>

  @POST("/like/{targetId}")
  fun like(@Path("targetId") targetId: String, @Body likeRatingRequest: LikeRatingRequest): Single<ResponseBody>

  @GET("/pass/{targetId}")
  fun dislike(@Path("targetId") targetId: String): Single<ResponseBody>

  @GET("/profile")
  fun profile(): Single<ResponseBody>

  companion object {
    const val BASE_URL = "https://api.gotinder.com"
    const val CONTENT_TYPE_JSON = "application/json"
    const val HEADER_AUTH = "X-Auth-Token"
    const val HEADER_CONTENT_TYPE = "Content-Type"
    const val HEADER_PLATFORM = "platform"
  }
}
