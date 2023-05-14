package com.ibashkimi.theme.activity

import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import com.ibashkimi.theme.R
import com.ibashkimi.theme.theme.NavBarColor
import com.ibashkimi.theme.theme.NightMode
import com.ibashkimi.theme.theme.Theme
import com.ibashkimi.theme.utils.StyleUtils

fun FragmentActivity.applyNightMode(nightMode: NightMode) {
    AppCompatDelegate.setDefaultNightMode(
        when (nightMode) {
            NightMode.DAY -> AppCompatDelegate.MODE_NIGHT_NO
            NightMode.NIGHT -> AppCompatDelegate.MODE_NIGHT_YES
            NightMode.DAYNIGHT -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    )
}

fun FragmentActivity.applyTheme(theme: Theme) = setTheme(theme.style)

fun FragmentActivity.applyNavBarColor(navBarColor: NavBarColor) {
    val color = when (navBarColor) {
        NavBarColor.SYSTEM -> {
            removeLightNavigationBar()
            StyleUtils.obtainColor(this, android.R.attr.navigationBarColor, Color.BLACK)
        }

        NavBarColor.THEME -> {
            removeLightNavigationBar()
            StyleUtils.obtainColor(this, R.attr.colorPrimaryDark, Color.BLACK)
        }

        NavBarColor.BLACK -> {
            removeLightNavigationBar()
            Color.BLACK
        }

        NavBarColor.WHITE -> {
            if (!setLightNavigationBar())
                return
            Color.WHITE
        }
    }
    window.navigationBarColor = color
}

fun FragmentActivity.applyNavBarColor(color: Int, isLightColor: Boolean = false) {
    if (isLightColor)
        setLightNavigationBar()
    else
        removeLightNavigationBar()
    window.navigationBarColor = color
}

fun FragmentActivity.setLightNavigationBar(): Boolean {
    if (Build.VERSION.SDK_INT >= 26) {
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        return true
    }
    return false
}

fun FragmentActivity.removeLightNavigationBar() {
    if (Build.VERSION.SDK_INT >= 26) {
        window.decorView.systemUiVisibility =
            window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    }
}