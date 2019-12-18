package com.ibashkimi.shared

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ibashkimi.theme.activity.applyNavBarColor
import com.ibashkimi.theme.activity.applyNightMode
import com.ibashkimi.theme.activity.applyTheme


abstract class BitToolsActivity : AppCompatActivity() {

    open val preferences: PreferenceHelper by lazy {
        PreferenceHelper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            applyNightMode(preferences.nightMode)
            recreate()
        }

        applyTheme(preferences.theme)
        applyNavBarColor(preferences.navBarColor)

        super.onCreate(savedInstanceState)
    }
}
