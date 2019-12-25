package com.ibashkimi.providerstools.theme

import androidx.annotation.LayoutRes
import com.ibashkimi.providerstools.R
import com.ibashkimi.providerstools.utils.layoutMap
import com.ibashkimi.shared.Tool

class WidgetProvider(val tool: Tool) {

    private val sections: Map<String, Array<Section>> by lazy { tool.layoutMap }

    val layouts: List<WidgetItem> by lazy {
        sections.map {
            WidgetItem(it.key, resolveLayoutFromId(it.key))
        }
    }

    fun getSections(layoutId: String): Array<Section> {
        return this.sections[layoutId]
            ?: throw IllegalArgumentException("Invalid layoutId $layoutId.")
    }

    fun getWidgets(section: Section): List<WidgetItem> {
        return section.displayStyles.map {
            WidgetItem(it, Gauges.previewStyle(it))
        }
    }

    @LayoutRes
    private fun resolveLayoutFromId(id: String): Int {
        return when (id) {
            Layouts.LAYOUT_SIMPLE -> R.layout.theme_item_layout_simple
            Layouts.LAYOUT_NORMAL -> R.layout.theme_item_layout_normal
            Layouts.LAYOUT_RICH -> R.layout.theme_item_layout_rich
            Layouts.LAYOUT_PAGER_TABS -> R.layout.theme_item_layout_pager
            Layouts.LAYOUT_PAGER -> R.layout.theme_item_layout_pager
            Layouts.LAYOUT_BOTTOM_SHEET -> R.layout.theme_item_layout_bottom_sheet
            else -> throw IllegalArgumentException()
        }
    }

    @LayoutRes
    private fun resolveLayout(displayStyle: String): Int {
        return Gauges.previewStyle(displayStyle)
    }
}