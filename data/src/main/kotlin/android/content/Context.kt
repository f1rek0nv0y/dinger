package android.content

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import org.stoyicker.dinger.data.R

fun Context.startIntent(intent: Intent, noHandlersFallback: (Intent) -> Unit = {
  Toast.makeText(
      this,
      getString(R.string.no_intent_handlers, intent),
      Toast.LENGTH_LONG)
      .show()
}) = if (packageManager.queryIntentActivities(intent, 0).size > 0) {
  startActivity(intent)
} else {
  noHandlersFallback(intent)
}

fun Context.versionCode() = with(packageManager.getPackageInfo(packageName, 0)) {
  if (Build.VERSION.SDK_INT >= 28) {
    longVersionCode
  } else {
    @Suppress("DEPRECATION") // Required on API < 28
    versionCode.toLong()
  }
}

fun Context.isOnNotMeteredInternet() = getSystemService(ConnectivityManager::class.java)?.let {
  val activeNetworkCapabilities = it.getNetworkCapabilities(it.activeNetwork)
  when (activeNetworkCapabilities) {
    null -> false
    else -> with(activeNetworkCapabilities) {
      hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) &&
          hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
  }
} ?: false
