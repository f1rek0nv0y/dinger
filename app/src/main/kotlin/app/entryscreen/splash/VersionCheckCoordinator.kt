package app.entryscreen.splash

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.content.isOnNotMeteredInternet
import android.content.startIntent
import android.content.versionCode
import android.net.Uri
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.provider.Browser
import android.support.v7.app.AlertDialog
import android.view.WindowManager
import domain.versioncheck.DomainVersionCheckDescription
import domain.versioncheck.VersionCheckUseCase
import io.reactivex.Scheduler
import io.reactivex.observers.DisposableSingleObserver
import java.io.File
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
          val downloadId = intent.extras!!.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)
          val downloadManager = activity.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
          val cursor = downloadManager.query(
              DownloadManager.Query().apply {
                setFilterById(downloadId)
              })
          if (cursor.moveToFirst()) {
            val uri = Uri.parse(cursor.getString(
                cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)))
            context.grantUriPermission(
                context.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity.startIntent(Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, downloadManager.getMimeTypeForDownloadedFile(downloadId))
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_GRANT_READ_URI_PERMISSION))
          }
          cursor.close()
        }
      }
    }
  }
  private var dialog: AlertDialog? = null
  private var wasShowing = false
  private var useCase: VersionCheckUseCase? = null

  fun actionRun() {
    activityWeakRef.get()?.let {
      // Skip the version check on metered internet connections since we don't want to force the download
      if (!it.isOnNotMeteredInternet()) {
        resultCallbackWeakRef.get()?.onVersionCheckPassed()
        return
      }
      useCase = VersionCheckUseCase(
          it.versionCode(), asyncExecutionScheduler, postExecutionScheduler)
      useCase?.execute(object : DisposableSingleObserver<DomainVersionCheckDescription>() {
        override fun onSuccess(versionCheckDescription: DomainVersionCheckDescription) {
          when (versionCheckDescription.isUpToDate) {
            true -> resultCallbackWeakRef.get()?.onVersionCheckPassed()
            false -> {
              deletePreviousApk(it)
              showDialog(versionCheckDescription)
            }
          }
        }

        override fun onError(e: Throwable) {
          resultCallbackWeakRef.get()?.onVersionCheckPassed()
        }

        private fun deletePreviousApk(context: Context) {
          Thread(Runnable {
            File(
                Uri.withAppendedPath(
                    Uri.fromFile(
                        context.getExternalFilesDir(DIRECTORY_DOWNLOADS)), DOWNLOAD_SUBPATH)
                    .path)
                .delete()
          }).start()
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
              .setNegativeButton(checkDescription.negativeButtonText, null)
              .create()
              .apply {
                setOnShowListener {
                  resultCallbackWeakRef.get()?.onVersionCheckFailed()
                  activity.registerReceiver(
                      updateDownloadedBroadcastReceiver,
                      IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
                  getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    (activity.getSystemService(DOWNLOAD_SERVICE) as DownloadManager?)
                        ?.enqueue(DownloadManager.Request(Uri.parse(checkDescription.downloadUrl)).apply {
                          setDestinationInExternalFilesDir(
                              context,
                              DIRECTORY_DOWNLOADS,
                              DOWNLOAD_SUBPATH)
                          setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
                        })
                    dismiss()
                    resultCallbackWeakRef.get()?.onNewVersionDownloadStarted()
                  }
                  getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                    activity.startIntent(
                        Intent(Intent.ACTION_VIEW, Uri.parse(checkDescription.changelogUrl))
                            .putExtra(Browser.EXTRA_APPLICATION_ID, activity.packageName)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                  }
                }
                show()
              }
        }
      }

  private companion object {
    private val DOWNLOAD_SUBPATH = "${File.separator}updates${File.separator}update.apk"
  }

  interface ResultCallback {
    fun onVersionCheckPassed()
    fun onVersionCheckFailed()
    fun onNewVersionDownloadStarted()
  }
}
