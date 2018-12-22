package data.autoswipe

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class AutoSwipeLauncherBroadcastReceiver : BroadcastReceiver() {
  @SuppressLint("UnsafeProtectedBroadcastReceiver")
  override fun onReceive(context: Context?, intent: Intent?) {
    context?.startService(AutoSwipeIntentService.callingIntent(context))
  }

  companion object {
    fun getCallingIntent(context: Context) =
        Intent(context, AutoSwipeLauncherBroadcastReceiver::class.java)
  }
}
