package com.ibashkimi.theme.activity

import android.content.SharedPreferences
import com.ibashkimi.theme.theme.NavBarColor
import com.ibashkimi.theme.theme.NightMode
import com.ibashkimi.theme.theme.Theme


open class ThemePreferences(private val sharedPreferences: SharedPreferences) : ThemeSupportPreferences {

    companion object {

        const val KEY_NIGHT_MODE = "night_mode"

        const val KEY_THEME = "theme"

        const val KEY_NAV_BAR_COLOR = "nav_bar_color"
    }

    override fun getNightMode(defaultValue: NightMode): NightMode {
        return uiThemeFromPref(sharedPreferences.getString(KEY_NIGHT_MODE, defaultValue.value)!!)
    }

    override fun setNightMode(nightMode: NightMode) {
        sharedPreferences.edit().putString(KEY_NIGHT_MODE, nightMode.name).apply()
    }

    override fun setTheme(theme: Theme) {
        sharedPreferences.edit().putString(KEY_THEME, theme.name).apply()
    }

    override fun setTheme(theme: Theme, editor: SharedPreferences.Editor) {
        editor.putString(KEY_THEME, theme.name)
    }

    override fun getTheme(defaultValue: Theme): Theme {
        return Theme.valueOf(sharedPreferences.getString(KEY_THEME, defaultValue.name)!!)
    }

    override fun setNavBarColor(navBarColor: NavBarColor) {
        sharedPreferences.edit().putString(KEY_NAV_BAR_COLOR, navBarColor.value).apply()
    }

    override fun getNavBarColor(defaultNavBarColor: NavBarColor): NavBarColor {
        return navBarFromPref(
                sharedPreferences.getString(KEY_NAV_BAR_COLOR, defaultNavBarColor.value)!!)
    }

    private fun navBarFromPref(value: String): NavBarColor {
        return NavBarColor.values().first { it.value == value }
    }

    private fun uiThemeFromPref(value: String): NightMode {
        return NightMode.values().first { it.value == value }
    }
}