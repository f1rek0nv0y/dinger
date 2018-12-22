package app.tinder.login

import android.content.Intent
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import org.stoyicker.dinger.R
import reporter.CrashReporter

/**
 * The difference between a 'feature' and a 'coordinator' is that a feature is just a wrapper around
 * an sdk call, whereas a coordinator _coordinates_ one action that is implemented deeper in the
 * architecture. Android-framework-facing though, they're the same, as they take weight off of it in
 * a way that it's only responsibility is flow routing.
 */
internal class TinderFacebookLoginFeature(
    loginButton: LoginButton,
    private val resultCallback: ResultCallback,
    private val crashReporter: CrashReporter) {
  private val callbackManager: CallbackManager = CallbackManager.Factory.create()

  init {
    loginButton.apply {
      // This 'hack' is required to pretend that we are Tinder
      loginBehavior = LoginBehavior.WEB_ONLY
      registerCallback(callbackManager,
          object : FacebookCallback<LoginResult> {
            override fun onCancel() = Toast.makeText(context, R.string.login_cancelled,
                Toast.LENGTH_LONG)
                .show()

            override fun onError(exception: FacebookException) {
              crashReporter.report(exception)
              Toast.makeText(context, R.string.login_failed,
                  Toast.LENGTH_LONG)
                  .show()
            }

            override fun onSuccess(loginResult: LoginResult) =
                reportSuccess(AccessToken.getCurrentAccessToken())
          })
    }
  }

  fun bind() = AccessToken.getCurrentAccessToken()?.also { reportSuccess(it) }

  /**
   * Call from Activity#onActivityResult(Int, Int, Intent?).
   */
  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    callbackManager.onActivityResult(requestCode, resultCode, data)
  }

  fun release(loginButton: LoginButton) = loginButton.unregisterCallback(callbackManager)

  private fun reportSuccess(accessToken: AccessToken) = accessToken.let {
    resultCallback.onSuccess(it.userId, it.token)
  }

  internal interface ResultCallback {
    fun onSuccess(facebookId: String, facebookToken: String)
  }
}
