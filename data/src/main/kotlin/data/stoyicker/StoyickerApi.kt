package data.stoyicker

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET

internal interface StoyickerApi {
  @GET("/dinger/version.json")
  fun versionCheck(): Single<ResponseBody>

  companion object {
    const val BASE_URL = "https://stoyicker.github.io"
  }
}
