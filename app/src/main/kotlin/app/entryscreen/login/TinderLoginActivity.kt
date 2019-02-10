package app.entryscreen.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.entryscreen.alarmbanner.AlarmBannerActivity
import app.android.content.safeApplication
import kotlinx.android.synthetic.main.activity_login.login_button
import kotlinx.android.synthetic.main.activity_login.progress
import org.stoyicker.dinger.R
import javax.inject.Inject

internal class TinderLoginActivity
  : TinderFacebookLoginFeature.ResultCallback,
    TinderLoginCoordinator.ResultCallback,
    AppCompatActivity() {
  @Inject
  lateinit var tinderFacebookLoginFeature: TinderFacebookLoginFeature
  @Inject
  lateinit var tinderLoginCoordinator: TinderLoginCoordinator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    inject()
    tinderFacebookLoginFeature.bind()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    tinderFacebookLoginFeature.onActivityResult(requestCode, resultCode, data)
  }

  override fun onDestroy() {
    tinderFacebookLoginFeature.release(login_button)
    tinderLoginCoordinator.actionCancel()
    super.onDestroy()
  }

  override fun onSuccess(facebookId: String, facebookToken: String) =
      tinderLoginCoordinator.actionDoLogin(facebookId, facebookToken)

  override fun onTinderLoginSuccess() {
    AlarmBannerActivity.getCallingIntent(this).apply {
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
      startActivity(this)
    }
    supportFinishAfterTransition()
  }

  private fun inject() = safeApplication().entryScreenComponent.newTinderLoginComponentBuilder()
      .activity(this)
      .contentLoadingProgressBar(progress)
      .loginButton(login_button)
      .tinderFacebookLoginResultCallback(this)
      .tinderLoginCoordinatorResultCallback(this)
      .build()
      .inject(this)

  companion object {
    fun getCallingIntent(context: Context) = Intent(context, TinderLoginActivity::class.java)
  }
}
