package com.ibashkimi.providerstools.data

import androidx.annotation.LayoutRes
import com.ibashkimi.providerstools.R
import com.ibashkimi.shared.Tool

class WidgetDataManager(val tool: Tool) {

    private val sections: Map<String, Array<Section>> by lazy { tool.layoutMap }

    val layouts: List<WidgetItem> = sections.map {
        WidgetItem(
            it.key,
            resolveLayoutFromId(it.key)
        )
    }

    fun getSections(layoutId: String): Array<Section> {
        return this.sections[layoutId]
            ?: throw IllegalArgumentException("Invalid layoutId $layoutId.")
    }

    fun getWidgets(section: Section): List<WidgetItem> {
        return section.displayStyles.map {
            WidgetItem(
                it,
                getGaugeLayoutPreview(it)
            )
        }
    }

    @LayoutRes
    fun resolveLayoutFromId(id: String): Int {
        return when (id) {
            Layouts.LAYOUT_SIMPLE -> R.layout.theme_item_layout_simple
            Layouts.LAYOUT_NORMAL -> R.layout.theme_item_layout_normal
            Layouts.LAYOUT_RICH -> R.layout.theme_item_layout_rich
            else -> throw IllegalArgumentException()
        }
    }

    @LayoutRes
    fun getGaugeLayoutRes(gaugeId: String, isForPreview: Boolean = false): Int =
        if (isForPreview) getGaugeLayoutPreview(
            gaugeId
        ) else getGaugeLayoutNormal(gaugeId)
}

fun getGaugeLayoutNormal(gaugeId: String) = when (gaugeId) {
    Gauges.STYLE_GAUGE_1 -> R.layout.gauge_style_1
    Gauges.STYLE_GAUGE_2 -> R.layout.gauge_style_2
    Gauges.STYLE_GAUGE_3 -> R.layout.gauge_style_3
    Gauges.STYLE_CHART_1 -> R.layout.chart_style_1
    Gauges.STYLE_CHART_2 -> R.layout.chart_style_2
    Gauges.STYLE_DIGITAL_1 -> R.layout.digital_style_1
    Gauges.STYLE_LEVEL_1 -> R.layout.gauge_level
    Gauges.STYLE_LEVEL_DIGITAL_1 -> R.layout.digital_level_1
    Gauges.STYLE_COMPASS_1 -> R.layout.compass_style_1
    Gauges.STYLE_COMPASS_2 -> R.layout.compass_style_2
    Gauges.STYLE_COMPASS_3 -> R.layout.compass_style_3
    Gauges.STYLE_COMPASS_4 -> R.layout.compass_style_4
    Gauges.STYLE_COMPASS_DIGITAL_1 -> R.layout.compass_digital_1
    else -> -1
}

fun getGaugeLayoutPreview(gaugeId: String) = when (gaugeId) {
    Gauges.STYLE_GAUGE_1 -> R.layout.gauge_style_1_preview
    Gauges.STYLE_GAUGE_2 -> R.layout.gauge_style_2_preview
    Gauges.STYLE_GAUGE_3 -> R.layout.gauge_style_3
    Gauges.STYLE_CHART_1 -> R.layout.chart_style_1_preview
    Gauges.STYLE_CHART_2 -> R.layout.chart_style_2_preview
    Gauges.STYLE_DIGITAL_1 -> R.layout.digital_style_1_preview
    Gauges.STYLE_LEVEL_1 -> R.layout.gauge_level
    Gauges.STYLE_LEVEL_DIGITAL_1 -> R.layout.digital_level_1_preview
    Gauges.STYLE_COMPASS_1 -> R.layout.compass_style_1_preview
    Gauges.STYLE_COMPASS_2 -> R.layout.compass_style_2_preview
    Gauges.STYLE_COMPASS_3 -> R.layout.compass_style_3_preview
    Gauges.STYLE_COMPASS_4 -> R.layout.compass_style_4_preview
    Gauges.STYLE_COMPASS_DIGITAL_1 -> R.layout.compass_digital_1_preview
    else -> -1
}