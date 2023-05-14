package com.ibashkimi.bittools.home

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibashkimi.bittools.R
import com.ibashkimi.shared.Tool
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferences: SharedPreferences
) : ViewModel() {

    val tools = MutableLiveData<Pair<List<Item>, List<Item>>>()

    init {
        refresh(preferences.getBoolean("show_unsupported_tools", true))
    }

    private fun Set<String>.toItems(): List<Item> {
        val items = ArrayList<Item>()
        this.map { Tool.valueOf(it) }.forEach {
            items.add(
                when (it) {
                    Tool.ACCELEROMETER -> Item(
                        R.string.accelerometer_title,
                        R.drawable.ic_accelerometer,
                        Tool.ACCELEROMETER
                    )

                    Tool.BAROMETER -> Item(
                        R.string.barometer_title,
                        R.drawable.ic_gauge,
                        Tool.BAROMETER
                    )

                    Tool.COMPASS -> Item(
                        R.string.compass_title,
                        R.drawable.ic_compass,
                        Tool.COMPASS
                    )

                    Tool.HYGROMETER -> Item(
                        R.string.hygrometer_title,
                        R.drawable.ic_hygrometer,
                        Tool.HYGROMETER
                    )

                    Tool.LEVEL -> Item(R.string.level_title, R.drawable.ic_level, Tool.LEVEL)
                    Tool.LIGHT -> Item(
                        R.string.light_meter_title,
                        R.drawable.ic_light_meter,
                        Tool.LIGHT
                    )

                    Tool.MAGNETOMETER -> Item(
                        R.string.magnetometer_title,
                        R.drawable.ic_magnetometer,
                        Tool.MAGNETOMETER
                    )

                    Tool.PROTRACTOR -> Item(
                        R.string.protractor,
                        R.drawable.ic_protractor,
                        Tool.PROTRACTOR
                    )

                    Tool.RULER -> Item(R.string.ruler_title, R.drawable.ic_ruler, Tool.RULER)
                    Tool.THERMOMETER -> Item(
                        R.string.thermometer_title,
                        R.drawable.ic_thermometer,
                        Tool.THERMOMETER
                    )
                }
            )
        }
        items.sortBy { it.name }
        return items
    }

    private fun refresh(showUnsupportedTools: Boolean = true) {
        val supportedTools = preferences.getStringSet("supported_tools", emptySet())!!
        val unsupportedTools = if (showUnsupportedTools)
            preferences.getStringSet("unsupported_tools", emptySet())!! else emptySet()
        tools.value = Pair(supportedTools.toItems(), unsupportedTools.toItems())
    }

    fun showUnsupportedTools() {
        preferences.edit().putBoolean("show_unsupported_tools", true).apply()
        refresh(true)
    }

    fun hideUnsupportedTools() {
        preferences.edit().putBoolean("show_unsupported_tools", false).apply()
        val v = tools.value
        tools.value = Pair(v?.first ?: emptyList(), emptyList())
    }
}