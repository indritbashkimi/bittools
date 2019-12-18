package com.ibashkimi.shared

import android.content.Context
import android.content.SharedPreferences
import com.ibashkimi.theme.theme.NavBarColor
import com.ibashkimi.theme.theme.NightMode
import com.ibashkimi.theme.theme.Theme

open class PreferenceHelper(context: Context) {

    open val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    var nightMode: NightMode
        get() = uiThemeFromPref(getString(KEY_NIGHT_MODE, DEFAULT_NIGHT_MODE.value))
        set(value) = putString(KEY_NIGHT_MODE, value.value)

    var theme: Theme
        get() = Theme.valueOf(getString(KEY_THEME, DEFAULT_THEME.name))
        set(value) = putString(KEY_THEME, value.name)

    fun setTheme(theme: Theme, editor: SharedPreferences.Editor) {
        editor.putString(KEY_THEME, theme.name)
    }

    var navBarColor: NavBarColor
        get() = navBarFromPref(getString(KEY_NAV_BAR_COLOR, DEFAULT_NAV_BAR_COLOR.value))
        set(value) = putString(KEY_NAV_BAR_COLOR, value.value)


    private fun navBarFromPref(value: String): NavBarColor {
        return NavBarColor.values().first { it.value == value }
    }

    private fun uiThemeFromPref(value: String): NightMode {
        return NightMode.values().firstOrNull { it.value == value } ?: DEFAULT_NIGHT_MODE
    }

    private fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue)!!
    }

    private fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    companion object {

        const val KEY_NIGHT_MODE = "night_mode"

        const val KEY_THEME = "theme"

        const val KEY_NAV_BAR_COLOR = "nav_bar_color"

        const val PREFERENCES_NAME = "bittools"

        val DEFAULT_THEME = Theme.DEEP_PURPLE_RED

        val DEFAULT_NIGHT_MODE = NightMode.DAYNIGHT

        val DEFAULT_NAV_BAR_COLOR = NavBarColor.SYSTEM
    }

}