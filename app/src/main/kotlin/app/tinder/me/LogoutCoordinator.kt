package app.tinder.me

import android.content.Context
import domain.logout.LogoutUseCase
import io.reactivex.Scheduler
import io.reactivex.observers.DisposableCompletableObserver

internal class LogoutCoordinator(
        private val context: Context,
        private val postExecutionScheduler: Scheduler) {
    private var useCase: LogoutUseCase? = null

    fun actionRun() {
        useCase?.dispose()
        LogoutUseCase(
                context = context,
                postExecutionScheduler = postExecutionScheduler).apply {
            useCase = this
            execute(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    throw IllegalStateException(
                            "The app reacted to a logout. The app should always close on logout. ")
                }

                override fun onError(e: Throwable) {
                    throw IllegalStateException("Logging out should always succeed, but failed", e)
                }
            })
        }
    }

    fun actionCancel() {
        useCase?.dispose()
    }
}
