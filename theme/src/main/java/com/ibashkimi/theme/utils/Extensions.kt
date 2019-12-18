package com.ibashkimi.theme.utils

import android.app.Activity
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate


fun applyGlobalNightMode(nightMode: String) {
    AppCompatDelegate.setDefaultNightMode(
        when (nightMode) {
            "dark" -> AppCompatDelegate.MODE_NIGHT_YES
            "system_default" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "battery_saver" -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            "light" -> AppCompatDelegate.MODE_NIGHT_NO
            else -> throw IllegalArgumentException("Invalid night mode $nightMode.")
        }
    )
}

fun Activity.applyNightMode(nightMode: String) {
    applyGlobalNightMode(nightMode)
    recreate()
}

@Suppress("unused")
fun AppCompatActivity.applyLocalNightMode(nightMode: String) {
    delegate.localNightMode = when (nightMode) {
        "dark" -> AppCompatDelegate.MODE_NIGHT_YES
        "system_default" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        "battery_saver" -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        "light" -> AppCompatDelegate.MODE_NIGHT_NO
        else -> throw IllegalArgumentException("Invalid night mode $nightMode.")
    }
    delegate.applyDayNight()
}

val Activity.isDarkTheme: Boolean
    get() {
        return this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }