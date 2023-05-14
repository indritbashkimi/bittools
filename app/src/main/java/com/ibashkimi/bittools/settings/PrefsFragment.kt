package com.ibashkimi.bittools.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ibashkimi.bittools.R
import com.ibashkimi.shared.PreferenceHelper
import com.ibashkimi.shared.Tool
import java.util.Locale

class PrefsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        preferenceManager.sharedPreferencesName = PreferenceHelper.PREFERENCES_NAME
        addPreferencesFromResource(R.xml.pref_general)

        val hasSoftKeys = preferenceManager.sharedPreferences!!
            .getBoolean("has_soft_keys", true)
        if (!hasSoftKeys) {
            findPreference<Preference>("nav_bar_color")?.isVisible = false
        }

        val showUnsupportedTools = preferenceManager.sharedPreferences!!
            .getBoolean("show_unsupported_tools", true)
        if (!showUnsupportedTools) {
            onShowUnsupportedToolsChanged(false)
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            "show_unsupported_tools" -> {
                val show = sharedPreferences.getBoolean(key, true)
                onShowUnsupportedToolsChanged(show)
            }
        }
    }

    private fun onShowUnsupportedToolsChanged(show: Boolean) {
        val tools = preferenceManager.sharedPreferences!!
            .getStringSet("unsupported_tools", null) ?: return
        for (tool in tools) {
            findPreference<Preference>(tool.lowercase(Locale.ENGLISH))?.isVisible = show
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "theme" -> {
                findNavController().navigate(R.id.action_settings_to_themeSelector)
                return true
            }

            "accelerometer" -> {
                launchToolSettings(Tool.ACCELEROMETER)
                return true
            }

            "barometer" -> {
                launchToolSettings(Tool.BAROMETER)
                return true
            }

            "compass" -> {
                launchToolSettings(Tool.COMPASS)
                return true
            }

            "hygrometer" -> {
                launchToolSettings(Tool.HYGROMETER)
                return true
            }

            "level" -> {
                launchToolSettings(Tool.LEVEL)
                return true
            }

            "light" -> {
                launchToolSettings(Tool.LIGHT)
                return true
            }

            "magnetometer" -> {
                launchToolSettings(Tool.MAGNETOMETER)
                return true
            }

            "thermometer" -> {
                launchToolSettings(Tool.THERMOMETER)
                return true
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun launchToolSettings(tool: Tool) {
        findNavController()
            .navigate(SettingsFragmentDirections.actionSettingsToProviderSettings(tool))
    }
}
