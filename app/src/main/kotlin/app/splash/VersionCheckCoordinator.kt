package app.splash

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
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
    private val updateDownloadedBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            activityWeakRef.get()?.let { activity ->
                activity.unregisterReceiver(this)
                if (intent.`package`?.contentEquals(activity.packageName ?: "") == true) {
                    activity.startActivity(Intent(DownloadManager.ACTION_VIEW_DOWNLOADS))
                }
            }
        }
    }
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
            activityWeakRef.get()?.let { activity ->
                if (!activity.isFinishing) {
                    activity.window.setFlags(
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                    dialog = AlertDialog.Builder(activity)
                            .setTitle(checkDescription.dialogTitle)
                            .setMessage(checkDescription.dialogBody)
                            .setCancelable(false)
                            .setPositiveButton(checkDescription.positiveButtonText, null)
                            .create()
                            .apply {
                                setOnShowListener {
                                    activity.registerReceiver(
                                            updateDownloadedBroadcastReceiver,
                                            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
                                    getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                                        (activity.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?)
                                                ?.enqueue(DownloadManager.Request(Uri.parse(checkDescription.downloadUrl)).apply {
                                                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                                })
                                    }
                                }
                                show()
                            }
                }
            }

    interface ResultCallback {
        fun onVersionCheckCompleted()
    }
}
