package com.ibashkimi.providerstools.settings

import android.app.Application
import androidx.annotation.LayoutRes
import androidx.lifecycle.*
import com.ibashkimi.providerstools.data.Section
import com.ibashkimi.providerstools.data.WidgetDataManager
import com.ibashkimi.providerstools.data.WidgetItem
import com.ibashkimi.shared.ext.getStringFlow
import com.ibashkimi.providerstools.data.preferences
import com.ibashkimi.shared.Tool
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class ProviderSettingsViewModel(app: Application, tool: Tool) : AndroidViewModel(app) {

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

class ProviderSettingsViewModelFactory(private val application: Application, val tool: Tool) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProviderSettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProviderSettingsViewModel(application, tool) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}