package com.ibashkimi.bittools

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.ibashkimi.providerstools.data.ToolPreferenceHelper
import com.ibashkimi.providerstools.data.isProviderSupported
import com.ibashkimi.shared.BitToolsActivity
import com.ibashkimi.shared.PreferenceHelper
import com.ibashkimi.shared.Tool
import com.ibashkimi.theme.activity.applyNavBarColor
import com.ibashkimi.theme.activity.applyNightMode
import com.ibashkimi.theme.theme.Theme
import com.ibashkimi.theme.utils.StyleUtils
import java.util.Locale

class MainActivity : BitToolsActivity(), SharedPreferences.OnSharedPreferenceChangeListener,
    NavController.OnDestinationChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        checkFirstRun()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onSharedPreferenceChanged(preferences.sharedPreferences, "keep_screen_on")
        onSharedPreferenceChanged(preferences.sharedPreferences, "screen_rotation")
    }

    override fun onStart() {
        super.onStart()
        preferences.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        preferences.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            "keep_screen_on" -> {
                val keepOn = sharedPreferences.getBoolean(key, true)
                if (keepOn) {
                    // The current destination, if any, will be immediately sent to your listener.
                    navigationController.addOnDestinationChangedListener(this)
                } else {
                    navigationController.removeOnDestinationChangedListener(this)
                    setKeepScreenOn(false)
                }
            }

            "screen_rotation" -> {
                setScreenOrientation(sharedPreferences.getString("screen_rotation", "auto")!!)
            }

            PreferenceHelper.KEY_THEME -> {
                recreate()
            }

            PreferenceHelper.KEY_NIGHT_MODE -> {
                applyNightMode(preferences.nightMode)
            }

            PreferenceHelper.KEY_NAV_BAR_COLOR -> {
                applyNavBarColor(preferences.navBarColor)
            }
        }
    }

    private fun setKeepScreenOn(keepOn: Boolean) {
        if (keepOn)
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        else
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        setKeepScreenOn(
            when (destination.id) {
                R.id.providerFragment, R.id.ruler -> true
                else -> false
            }
        )
    }

    private fun setScreenOrientation(orientation: String) {
        requestedOrientation = when (orientation) {
            "portrait" -> {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }

            "landscape" -> {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            else -> ActivityInfo.SCREEN_ORIENTATION_USER
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.my_nav_host_fragment).navigateUp()

    private fun checkFirstRun() {
        val prefs = preferences.sharedPreferences
        val isFirstRun = prefs.getBoolean("first_run_${BuildConfig.VERSION_CODE}", true)
        if (isFirstRun) {
            var setDefaultTheme = false
            var checkNavBar = false
            var checkSupportedTools = false
            var setDefaultLayout = false
            var setDefaultSamplingRate = false
            var showUnsupportedTools = false

            val editor = prefs.edit()

            val previousVersion = prefs.getInt("previous_version", VERSION_UNKNOWN)
            if (previousVersion == VERSION_UNKNOWN) {
                // Check if upgrade from version 1.2(4) where first_run flag was used
                if (!prefs.getBoolean("first_run", true)) {
                    checkSupportedTools = true
                    setDefaultLayout = true
                    showUnsupportedTools = true
                    setDefaultSamplingRate = true
                    editor.remove("first_run")
                } else {
                    setDefaultTheme = true
                    checkNavBar = true
                    checkSupportedTools = true
                    setDefaultLayout = true
                    setDefaultSamplingRate = true
                    showUnsupportedTools = true
                }
            } else if (previousVersion == 12) {
                checkSupportedTools = true
            }
            if (previousVersion < 16) {
                setDefaultLayout = true
            }
            if (previousVersion < 18) {
                if (preferences.theme == Theme.MATERIAL) {
                    preferences.theme = Theme.BLACK_WHITE_TEAL
                }
            }

            if (checkSupportedTools) {
                checkAvailableTools(editor)
            }

            initToolPreferences(setDefaultLayout, setDefaultSamplingRate)

            if (checkNavBar) {
                checkNavBar(editor)
            }
            if (showUnsupportedTools) {
                editor.putBoolean("show_invalid_tools", true)
            }
            if (setDefaultTheme) {
                preferences.setTheme(PreferenceHelper.DEFAULT_THEME, editor)
            }

            editor.putBoolean("first_run_${BuildConfig.VERSION_CODE}", false)
            editor.putInt("previous_version", BuildConfig.VERSION_CODE) // May turn helpful someday
            editor.apply()
        }
    }

    private val navigationController: NavController
        get() = findNavController(R.id.my_nav_host_fragment)

    private fun checkAvailableTools(editor: SharedPreferences.Editor) {
        val supportedTools = ArrayList<String>()
        val unsupportedTools = ArrayList<String>()

        activeTools().forEach {
            if (it.isSupported())
                supportedTools.add(it.name)
            else
                unsupportedTools.add(it.name)
        }

        editor.putStringSet("supported_tools", supportedTools.toSet())
            .putStringSet("unsupported_tools", unsupportedTools.toSet())
    }

    private fun initToolPreferences(setLayout: Boolean, setSamplingRate: Boolean) {
        for (tool in activeTools()) {
            if (tool == Tool.RULER || tool == Tool.PROTRACTOR)
                continue
            ToolPreferenceHelper(
                tool,
                getSharedPreferences(tool.name.lowercase(Locale.ENGLISH), Context.MODE_PRIVATE)
            )
                .apply {
                    if (setLayout) {
                        setDefaultLayout()
                    }
                    if (setSamplingRate) {
                        setDefaultSamplingRate()
                    }
                }
        }
    }

    private fun checkNavBar(editor: SharedPreferences.Editor) {
        editor.putBoolean("has_soft_keys", StyleUtils.hasSoftwareKeys(this))
    }

    private fun activeTools(): Array<Tool> = arrayOf(
        Tool.ACCELEROMETER,
        Tool.BAROMETER,
        Tool.COMPASS,
        Tool.HYGROMETER,
        Tool.LEVEL,
        Tool.LIGHT,
        Tool.MAGNETOMETER,
        Tool.RULER,
        Tool.PROTRACTOR,
        Tool.THERMOMETER
    )

    private fun Tool.isSupported() = when (this) {
        Tool.PROTRACTOR, Tool.RULER -> true
        else -> isProviderSupported(applicationContext)
    }

    companion object {
        const val VERSION_UNKNOWN = -1
    }
}
