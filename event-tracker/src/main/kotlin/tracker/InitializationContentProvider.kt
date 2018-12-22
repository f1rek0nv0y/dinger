package tracker

import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri

internal class InitializationContentProvider : ContentProvider() {
  override fun onCreate(): Boolean {
    EventTrackerImpl.Void.init(context!!.applicationContext)
    return true
  }

  override fun insert(uri: Uri?, values: ContentValues?) = null
  override fun query(uri: Uri?, projection: Array<out String>?, selection: String?,
                     selectionArgs: Array<out String>?, sortOrder: String?) = null

  override fun update(uri: Uri?, values: ContentValues?, selection: String?,
                      selectionArgs: Array<out String>?) = 0

  override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?) = 0
  override fun getType(uri: Uri?) = "vnd.android.cursor.item.none"
}
