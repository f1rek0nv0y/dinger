package app.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import app.MainApplication
import app.home.HomeActivity
import app.tinder.login.TinderLoginActivity
import javax.inject.Inject

/**
 * A simple activity that acts as a splash screen.
 *
 * Note how, instead of using the content view to set the splash, we just set the splash as
 * background in the theme. This allows it to be shown without having to wait for the content view
 * to be drawn.
 */
internal class SplashActivity :
        LoggedInCheckCoordinator.ResultCallback,
        VersionCheckCoordinator.ResultCallback,
        AppCompatActivity() {
    @Inject
    lateinit var loggedInCheckCoordinator: LoggedInCheckCoordinator
    @Inject
    lateinit var versionCheckCoordinator: VersionCheckCoordinator
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        scheduleContentOpening()
    }

    override fun onDestroy() {
        loggedInCheckCoordinator.actionCancel()
        versionCheckCoordinator.actionCancel()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        versionCheckCoordinator.resume()
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        versionCheckCoordinator.pause()
        super.onPause()
    }

    override fun onVersionCheckCompleted() { loggedInCheckCoordinator.actionRun() }

    override fun onLoggedInUserFound() = continueLoggedIn()

    override fun onLoggedInUserNotFound() = requestToken()

    /**
     * Schedules the app content to be shown.
     */
    private fun scheduleContentOpening() {
        handler = Handler()
        handler.postDelayed({ versionCheckCoordinator.actionRun() }, SHOW_TIME_MILLIS)
    }

    private fun inject() = (application as MainApplication).applicationComponent
            .newSplashComponent(SplashModule(
                    activity = this,
                    loggedInCheckResultCallback = this,
                    versionCheckCoordinatorResultCallback = this))
            .inject(this)

    private fun requestToken() {
        TinderLoginActivity.getCallingIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
        supportFinishAfterTransition()
    }

    private fun continueLoggedIn() {
        HomeActivity.getCallingIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
        supportFinishAfterTransition()
    }

    private companion object {
        const val SHOW_TIME_MILLIS = 500L
    }
}

