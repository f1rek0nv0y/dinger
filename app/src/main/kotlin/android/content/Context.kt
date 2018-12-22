package android.content

import android.os.Build
import android.widget.Toast
import org.stoyicker.dinger.R

internal fun Context.startIntent(intent: Intent, noHandlersFallback: (Intent) -> Unit? = {
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

internal fun Context.versionCode(): Long = with(packageManager.getPackageInfo(packageName, 0)) {
  if (Build.VERSION.SDK_INT >= 28) {
    longVersionCode
  } else {
    @Suppress("DEPRECATION") // Required on API < 28
    versionCode.toLong()
  }
}
