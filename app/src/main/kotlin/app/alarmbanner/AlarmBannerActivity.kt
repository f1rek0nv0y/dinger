package app.alarmbanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.MainApplication
import app.home.HomeActivity
import kotlinx.android.synthetic.main.activity_alarm_banner.continue_button
import org.stoyicker.dinger.R
import javax.inject.Inject

internal class AlarmBannerActivity : AppCompatActivity(), ContinueCoordinator.ResultCallback {
  @Inject
  lateinit var alarmBannerCoordinator: AlarmBannerCoordinator
  @Inject
  lateinit var continueCoordinator: ContinueCoordinator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_alarm_banner)
    inject()
    alarmBannerCoordinator.actionDoSchedule()
    continueCoordinator.enable()
  }

  private fun inject() = (application as MainApplication).applicationComponent
      .newAlarmBannerComponent(
          AutoSwipeTriggerModule(this),
          ContinueModule(this, continue_button))
      .inject(this)

  override fun onDestroy() {
    super.onDestroy()
    alarmBannerCoordinator.actionCancelSchedule()
  }

  override fun onContinueClicked() {
    HomeActivity.getCallingIntent(this).apply {
      flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
      startActivity(this)
    }
    supportFinishAfterTransition()
  }

  companion object {
    fun getCallingIntent(context: Context) = Intent(context, AlarmBannerActivity::class.java)
  }
}
