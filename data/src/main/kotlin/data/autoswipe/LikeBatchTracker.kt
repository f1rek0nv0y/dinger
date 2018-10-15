package data.autoswipe

import android.content.Context
import android.content.SharedPreferences
import org.stoyicker.dinger.data.R

internal class LikeBatchTracker(
        context: Context,
        private val preferences: SharedPreferences) {
    private companion object {
        lateinit var PREFERENCE_KEY_AUTOSWIPE_BATCH_LIKES: String
        const val DEFAULT_LIKE_AMOUNT = 0
        const val MAX_BATCH_SIZE = 1700
    }

    init {
        PREFERENCE_KEY_AUTOSWIPE_BATCH_LIKES = context.getString(
                R.string.preference_key_autoswipe_batch_likes)
    }

    fun addLike() = writePref(getPref() + 1)

    fun closeBatch() = writePref(DEFAULT_LIKE_AMOUNT)

    fun isBatchOpen() = getPref() <= MAX_BATCH_SIZE

    private fun writePref(value: Int) = preferences.edit()
            .putInt(PREFERENCE_KEY_AUTOSWIPE_BATCH_LIKES, value)
            .apply()

    private fun getPref() = preferences.getInt(
            PREFERENCE_KEY_AUTOSWIPE_BATCH_LIKES, DEFAULT_LIKE_AMOUNT)
}
