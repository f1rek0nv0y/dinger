package app.android.content

import android.content.Context
import app.MainApplication

internal fun Context.safeApplication() = applicationContext as MainApplication
