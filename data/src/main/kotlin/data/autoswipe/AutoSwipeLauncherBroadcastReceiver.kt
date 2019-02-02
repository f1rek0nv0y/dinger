package data.autoswipe

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

internal class AutoSwipeLauncherBroadcastReceiver : BroadcastReceiver() {
  @SuppressLint("UnsafeProtectedBroadcastReceiver")
  override fun onReceive(context: Context?, intent: Intent?) {
    context?.apply {
      AutoSwipeIntentService.callingIntent(context).let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          startForegroundService(it)
        } else {
          startService(it)
        }
      }
    }
  }

  companion object {
    fun getCallingIntent(context: Context) =
        Intent(context, AutoSwipeLauncherBroadcastReceiver::class.java)
  }
}
