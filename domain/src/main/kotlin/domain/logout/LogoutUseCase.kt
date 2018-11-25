package domain.logout

import android.content.Context
import domain.autoswipe.AutoSwipeHolder
import domain.autoswipe.PostAutoSwipeUseCase
import domain.interactor.CompletableDisposableUseCase
import io.reactivex.Completable
import io.reactivex.Scheduler

class LogoutUseCase(
        private val context: Context,
        asyncExecutionScheduler: Scheduler? = null,
        postExecutionScheduler: Scheduler)
    : CompletableDisposableUseCase(asyncExecutionScheduler, postExecutionScheduler) {
    override fun buildUseCase(): Completable {
        return Completable.fromCallable {
            LogoutHolder.apply {
                autoswipeDestructor.stopService(context)
                alarmManager.cancelOneShotBroadcast(
                        PostAutoSwipeUseCase.REQUEST_CODE,
                        AutoSwipeHolder.autoSwipeLauncherFactory.newFromBroadcast(context))
                removeAccount.removeAccount()
                storageClear.clearStorage(context)
            }
        }
    }
}
