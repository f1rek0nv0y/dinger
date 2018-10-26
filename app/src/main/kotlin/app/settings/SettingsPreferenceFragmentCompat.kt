package app.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.content.startIntent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import domain.autoswipe.ImmediatePostAutoSwipeUseCase
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers
import org.stoyicker.dinger.R

internal class SettingsPreferenceFragmentCompat : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefs_settings, rootKey)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initializeBypassDozePreference()
        }
        addTriggerToAutoswipeEnabledPreference()
        initializeSharePreference()
        initializeAboutTheAppPreference()
    }

    @SuppressLint("BatteryLife")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initializeBypassDozePreference() {
        findPreference(context?.getString(R.string.preference_key_bypass_doze))
                ?.takeIf { Build.VERSION.SDK_INT >= Build.VERSION_CODES.M }
                ?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val packageName = context?.packageName
            val intent = if (context?.getSystemService(PowerManager::class.java)
                            ?.isIgnoringBatteryOptimizations(packageName) == true) {
                Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            } else {
                Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                        .setData(Uri.parse("package:$packageName"))
            }
            context?.startIntent(intent)
            true
        }
    }


    private fun addTriggerToAutoswipeEnabledPreference() {
        findPreference(context?.getString(R.string.preference_key_autoswipe_enabled))
                ?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, value ->
            if (value is Boolean && value) {
                ImmediatePostAutoSwipeUseCase(context!!, Schedulers.trampoline()).execute(
                        object : DisposableCompletableObserver() {
                            override fun onComplete() {
                            }

                            override fun onError(error: Throwable) {
                            }
                        })
            }
            true
        }
    }

    private fun initializeSharePreference() {
        findPreference(context?.getString(R.string.preference_key_share))
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                @SuppressLint("InlinedApi")
                flags += Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                putExtra(Intent.EXTRA_TEXT, context?.getString(
                        R.string.dinger_website_download))
            }
            context?.startIntent(Intent.createChooser(
                    intent, getString(R.string.action_share_title)))
            true
        }
    }

    private fun initializeAboutTheAppPreference() {
        findPreference(context?.getString(R.string.preference_key_about_the_app))
                .onPreferenceClickListener = Preference.OnPreferenceClickListener {
            context?.startIntent(Intent(
                    Intent.ACTION_VIEW, Uri.parse(getString(R.string.dinger_website))))
            true
        }
    }

    companion object {
        const val FRAGMENT_TAG = "SettingsPreferenceFragmentCompat"
    }
}
