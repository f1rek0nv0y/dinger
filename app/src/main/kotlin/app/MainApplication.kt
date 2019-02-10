package app

import android.annotation.SuppressLint
import android.app.Application
import app.entryscreen.DaggerEntryScreenComponent
import app.entryscreen.EntryScreenComponent
import reporter.CrashReporter
import javax.inject.Inject

/**
 * Custom application.
 */
@SuppressLint("Registered") // It is registered in the buildtype-specific manifests
internal open class MainApplication : Application() {
  val entryScreenComponent: EntryScreenComponent by lazy { DaggerEntryScreenComponent.create() }
  @Inject
  lateinit var crashReporter: CrashReporter

  override fun onCreate() {
    super.onCreate()
    DaggerMainApplicationComponent.create().inject(this)
    crashReporter.init(this)
  }
}
