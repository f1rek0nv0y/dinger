package data.tinder

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.facebook.AccessToken
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import dagger.Module
import dagger.Provides
import data.RootModule
import data.account.AccountModule
import data.account.AppAccountAuthenticator
import data.crash.CrashReporterModule
import data.network.NetworkClientModule
import data.network.NetworkModule
import data.notification.NotificationManager
import data.notification.NotificationManagerModule
import domain.login.TinderLoginUseCase
import io.reactivex.Single
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import org.stoyicker.dinger.data.BuildConfig
import org.stoyicker.dinger.data.R
import reporter.CrashReporter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [
    AccountModule::class,
    CrashReporterModule::class,
    NetworkClientModule::class,
    NetworkModule::class,
    NotificationManagerModule::class,
    RootModule::class])
internal class TinderApiModule {
    @Provides
    @Singleton
    fun tinderApi(
            context: Context,
            clientBuilder: OkHttpClient.Builder,
            retrofitBuilder: Retrofit.Builder,
            appAccountManager: AppAccountAuthenticator,
            notificationManager: NotificationManager,
            crashReporter: CrashReporter): TinderApi = retrofitBuilder
            .client(clientBuilder.addInterceptor {
                it.proceed(it.request().newBuilder().addHeaders(appAccountManager).build())
            }.authenticator { _, response ->
                when {
                    !response.request().header(HEADER_AUTH_TRIED).isNullOrBlank() -> null
                    response.code() == 401 -> appAccountManager.let {
                        val facebookToken: AccessToken? = AccessToken.getCurrentAccessToken()
                        if (facebookToken != null) {
                            Single.create<AccessToken> { emitter ->
                                if (facebookToken.isExpired) {
                                    AccessToken.refreshCurrentAccessTokenAsync(
                                            object : AccessToken.AccessTokenRefreshCallback {
                                                @Suppress("FunctionName")
                                                override fun OnTokenRefreshed(
                                                        accessToken: AccessToken) {
                                                    emitter.onSuccess(accessToken)
                                                }

                                                @Suppress("FunctionName")
                                                override fun OnTokenRefreshFailed(
                                                        exception: FacebookException) {
                                                    emitter.onError(exception)
                                                }
                                            })
                                } else {
                                    emitter.onSuccess(facebookToken)
                                }
                            }.run {
                                try {
                                    blockingGet().run {
                                        AccessToken.setCurrentAccessToken(this)
                                        var request: Request? = null
                                        TinderLoginUseCase(
                                                facebookId = userId,
                                                facebookToken = token,
                                                postExecutionScheduler = Schedulers.trampoline())
                                                .execute(object : DisposableCompletableObserver() {
                                                    override fun onComplete() {
                                                        request = response.request().newBuilder()
                                                                .addHeader(
                                                                        HEADER_AUTH_TRIED, "true")
                                                                .addHeaders(appAccountManager)
                                                                .build()
                                                    }

                                                    override fun onError(error: Throwable) {
                                                        crashReporter.report(error)
                                                    }
                                                })
                                        request
                                    }
                                } catch (exception: FacebookException) {
                                    finishAccount(context, appAccountManager, notificationManager)
                                    null
                                }
                            }
                        } else {
                            finishAccount(context, appAccountManager, notificationManager)
                            null
                        }
                    }
                    else -> {
                        finishAccount(context, appAccountManager, notificationManager)
                        null
                    }
                }
            }.dispatcher(Dispatcher().apply {
                maxRequestsPerHost = 30
            }).build())
            .baseUrl(TinderApi.BASE_URL)
            .build()
            .create(TinderApi::class.java)

    private companion object {
        const val HEADER_AUTH_TRIED = "auth-tried"
    }
}

private fun Request.Builder.addHeaders(appAccountManager: AppAccountAuthenticator) = apply {
    appAccountManager.getTinderAccountToken()?.let {
        addHeader(TinderApi.HEADER_AUTH, it)
        addHeader(TinderApi.HEADER_CONTENT_TYPE, TinderApi.CONTENT_TYPE_JSON)
        addHeader(TinderApi.HEADER_PLATFORM, BuildConfig.PLATFORM_ANDROID)
    }
}

private fun finishAccount(
        context: Context,
        appAccountAuthenticator: AppAccountAuthenticator,
        notificationManager: NotificationManager) {
    LoginManager.getInstance().logOut()
    appAccountAuthenticator.removeAccount()
    notificationManager.build(
            channelName = R.string.authentication_notification_channel_name,
            title = R.string.authentication_notification_title,
            body = R.string.authentication_notification_body,
            category = NotificationManager.CATEGORY_RECOMMENDATION,
            priority = NotificationManager.PRIORITY_HIGH,
            clickHandler = PendingIntent.getActivity(
                    context,
                    0,
                    Intent().setComponent(
                            ComponentName(context, "app.splash.SplashActivity")),
                    PendingIntent.FLAG_UPDATE_CURRENT))
}
