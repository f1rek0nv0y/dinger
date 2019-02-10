package app.entryscreen.login

import domain.login.TinderLoginUseCase
import io.reactivex.Scheduler
import io.reactivex.observers.DisposableCompletableObserver
import reporter.CrashReporter

internal class TinderLoginCoordinator(
    private val view: TinderLoginView,
    private val asyncExecutionScheduler: Scheduler,
    private val postExecutionScheduler: Scheduler,
    private val resultCallback: ResultCallback,
    private val crashReporter: CrashReporter) {
  private var useCase: TinderLoginUseCase? = null

  fun actionDoLogin(facebookId: String, facebookToken: String) {
    useCase?.dispose()
    view.setRunning()
    TinderLoginUseCase(
        facebookId, facebookToken, asyncExecutionScheduler, postExecutionScheduler).apply {
      useCase = this
      execute(object : DisposableCompletableObserver() {
        override fun onError(error: Throwable) {
          crashReporter.report(error)
          view.setError()
        }

        override fun onComplete() {
          resultCallback.onTinderLoginSuccess()
          view.setStale()
        }
      })
    }
  }

  fun actionCancel() {
    view.setStale()
    useCase?.dispose()
  }

  interface ResultCallback {
    fun onTinderLoginSuccess()
  }
}
