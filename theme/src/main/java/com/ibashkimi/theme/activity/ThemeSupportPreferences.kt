package com.ibashkimi.theme.activity

import android.content.SharedPreferences
import com.ibashkimi.theme.theme.NavBarColor
import com.ibashkimi.theme.theme.NightMode
import com.ibashkimi.theme.theme.Theme


interface ThemeSupportPreferences {

    fun getNightMode(defaultValue: NightMode): NightMode

    fun setNightMode(nightMode: NightMode)

    fun getTheme(defaultValue: Theme): Theme

    fun setTheme(theme: Theme)

    fun setTheme(theme: Theme, editor: SharedPreferences.Editor)

    fun setNavBarColor(navBarColor: NavBarColor)

    fun getNavBarColor(defaultNavBarColor: NavBarColor): NavBarColor
}