package com.ibashkimi.bittools.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ibashkimi.shared.PreferenceHelper
import com.ibashkimi.theme.preference.DecodedTheme
import com.ibashkimi.theme.preference.decodeTheme
import com.ibashkimi.theme.theme.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val preferences: PreferenceHelper
) : ViewModel() {

    private val themes: Array<Theme> = arrayOf(
        Theme.RED_BLUE, Theme.RED_TEAL, Theme.PINK_BLUE, Theme.PINK_LIME, Theme.PURPLE_RED,
        Theme.PURPLE_GREEN, Theme.DEEP_PURPLE_RED, Theme.INDIGO_PINK, Theme.BLUE_LIME,
        Theme.LIGHT_BLUE, Theme.CYAN_AMBER, Theme.TEAL, Theme.GREEN_RED, Theme.LIGHT_GREEN_CYAN,
        Theme.LIME_BLUE_PINK, Theme.AMBER_LIGHT_BLUE, Theme.YELLOW_RED, Theme.ORANGE_TEAL,
        Theme.DEEP_ORANGE_INDIGO, Theme.BROWN_RED, Theme.GRAY_PINK, Theme.BLUE_GREY_RED,
        //Theme.BLACK_WHITE_BLUE, simply not used ATM
        //Theme.MATERIAL, substituted with Theme.BLACK_WHITE_TEAL
        Theme.BLACK_WHITE_TEAL, Theme.FULL_CRANE_PURPLE, Theme.FULL_REPLY, Theme.FULL_INDIGO,
        Theme.FULL_CYAN, Theme.FULL_BROWN, Theme.FULL_GRAY
    )

    fun setTheme(theme: Theme) {
        preferences.theme = theme
    }

    fun themes(context: Context): LiveData<Pair<List<DecodedTheme>, Int>> =
        liveData(Dispatchers.IO) {
            val decodedThemes = themes.map { decodeTheme(context, it) }
            val selectedThemeIndex = themes.indexOf(preferences.theme)
            emit(Pair(decodedThemes, selectedThemeIndex))
        }
}