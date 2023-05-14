package com.ibashkimi.providerstools.settings

import android.app.Application
import androidx.annotation.LayoutRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.ibashkimi.providerstools.data.Section
import com.ibashkimi.providerstools.data.WidgetDataManager
import com.ibashkimi.providerstools.data.WidgetItem
import com.ibashkimi.providerstools.data.preferences
import com.ibashkimi.shared.Tool
import com.ibashkimi.shared.ext.getStringFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ProviderSettingsViewModel @Inject constructor(
    app: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {

    val tool = checkNotNull(savedStateHandle.get<Tool>("tool"))

    private val dataManager: WidgetDataManager = WidgetDataManager(tool)

    private val preferences = tool.preferences(app)

    val layouts = LayoutsData(dataManager.layouts, indexOf(preferences.getString("layout", null)))

    val layoutLiveData: LiveData<WidgetItem> = preferences.getStringFlow("layout")
        .filterNotNull()
        .map {
            WidgetItem(
                it,
                dataManager.resolveLayoutFromId(it)
            )
        }
        .asLiveData()

    private fun indexOf(layout: String?): Int {
        val layouts = dataManager.layouts
        return layouts.indices.firstOrNull { layouts[it].id == layout } ?: 0
    }

    fun onLayoutSelected(item: WidgetItem) {
        preferences.edit().putString("layout", item.id).apply()
    }

    fun onWidgetSelected(sectionId: String, displayStyle: WidgetItem) {
        preferences.edit().putString(sectionId, displayStyle.id).apply()
    }

    fun sectionsOf(layoutId: String): Array<Section> = dataManager.getSections(layoutId)

    fun selectedDisplayOf(section: Section) =
        preferences.getString(section.sectionId, section.displayStyles[0])

    @LayoutRes
    fun getGaugeLayoutRes(gaugeId: String) = dataManager.getGaugeLayoutRes(gaugeId, true)

    data class LayoutsData(val layout: List<WidgetItem>, val selectedIndex: Int)
}
