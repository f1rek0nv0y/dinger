package app.splash

import android.app.Activity
import android.content.Intent
import android.content.startIntent
import android.content.versionCode
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.view.WindowManager
import domain.versioncheck.DomainVersionCheckDescription
import domain.versioncheck.VersionCheckUseCase
import io.reactivex.Scheduler
import io.reactivex.observers.DisposableSingleObserver
import java.lang.ref.WeakReference

internal class VersionCheckCoordinator(
        activity: Activity,
        private val asyncExecutionScheduler: Scheduler,
        private val postExecutionScheduler: Scheduler,
        resultCallback: ResultCallback) {
    private val activityWeakRef by lazy { WeakReference(activity) }
    private val resultCallbackWeakRef by lazy { WeakReference(resultCallback) }
    private var dialog: AlertDialog? = null
    private var wasShowing = false
    private var useCase: VersionCheckUseCase? = null

    fun actionRun() {
        activityWeakRef.get()?.let {
            useCase = VersionCheckUseCase(
                    it.versionCode(), asyncExecutionScheduler, postExecutionScheduler)
            useCase?.execute(object : DisposableSingleObserver<DomainVersionCheckDescription>() {
                override fun onSuccess(versionCheckDescription: DomainVersionCheckDescription) {
                    when (versionCheckDescription.isUpToDate) {
                        true -> resultCallbackWeakRef.get()?.onVersionCheckCompleted()
                        false -> showDialog(versionCheckDescription)
                    }
                }

                override fun onError(e: Throwable) {
                    resultCallbackWeakRef.get()?.onVersionCheckCompleted()
                }
            })
        }
    }

    fun resume() {
        if (wasShowing) {
            dialog?.show()
        }
    }

    fun pause() {
        wasShowing = dialog?.isShowing ?: false
        dialog?.dismiss()
    }

    fun actionCancel() {
        useCase?.dispose()
        dialog?.dismiss()
        wasShowing = false
    }

    private fun showDialog(checkDescription: DomainVersionCheckDescription) =
            activityWeakRef.get()?.let {
                if (!it.isFinishing) {
                    it.window.setFlags(
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                    dialog = AlertDialog.Builder(it)
                            .setTitle(checkDescription.dialogTitle)
                            .setMessage(checkDescription.dialogBody)
                            .setPositiveButton(checkDescription.positiveButtonText) { _, _ ->
                                it.startIntent(Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(checkDescription.downloadUrl)))
                            }
                            .setOnDismissListener {
                                wasShowing = false
                                resultCallbackWeakRef.get()?.onVersionCheckCompleted()
                            }
                            .show()
            }
    }

    interface ResultCallback {
        fun onVersionCheckCompleted()
    }
}
