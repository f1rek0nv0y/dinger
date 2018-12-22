package app.tinder.login

import android.view.View
import android.widget.Toast
import org.stoyicker.dinger.R

internal class TinderLoginViewImpl(
    private val activity: TinderLoginActivity,
    private val loginButton: View,
    private val progress: View)
  : TinderLoginView {
  override fun setRunning() {
    loginButton.visibility = View.INVISIBLE
    progress.visibility = View.VISIBLE
  }

  override fun setStale() {
    loginButton.visibility = View.VISIBLE
    progress.visibility = View.INVISIBLE
  }

  override fun setError() {
    setStale()
    Toast.makeText(activity, R.string.login_failed, Toast.LENGTH_LONG).show()
  }
}
