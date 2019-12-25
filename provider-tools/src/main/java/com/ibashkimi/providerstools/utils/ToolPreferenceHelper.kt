package com.ibashkimi.providerstools.utils

import android.content.Context
import android.content.SharedPreferences
import com.ibashkimi.provider.providerdata.DataProcessor
import com.ibashkimi.provider.providerdata.SensorData
import com.ibashkimi.provider.unitconverter.celsiusToFahrenheit
import com.ibashkimi.provider.unitconverter.celsiusToKelvin
import com.ibashkimi.provider.unitconverter.hpaToAtm
import com.ibashkimi.provider.unitconverter.hpaToTorr
import com.ibashkimi.providerstools.model.DisplayParams
import com.ibashkimi.providerstools.model.MeasureUnit
import com.ibashkimi.providerstools.R
import com.ibashkimi.providerstools.theme.Gauges
import com.ibashkimi.providerstools.theme.Layouts
import com.ibashkimi.providerstools.theme.Section
import com.ibashkimi.shared.Tool

class ToolPreferenceHelper(val tool: Tool, private val sharedPreferences: SharedPreferences) {

    fun setAllDefaults() {
        setDefaultLayout()
        setDefaultSamplingRate()
    }

    fun setDefaultLayout() {
        when (tool) {
            Tool.ACCELEROMETER -> {
                save(
                    Layouts.LAYOUT_NORMAL,
                    Gauges.STYLE_CHART_1,
                    Gauges.STYLE_DIGITAL_1,
                    Gauges.STYLE_GAUGE_3
                )
            }
            Tool.BAROMETER -> {
                save(
                    Layouts.LAYOUT_SIMPLE,
                    Gauges.STYLE_GAUGE_1,
                    Gauges.STYLE_DIGITAL_1,
                    Gauges.STYLE_CHART_2
                )
            }
            Tool.COMPASS -> {
                save(
                    Layouts.LAYOUT_SIMPLE,
                    Gauges.STYLE_COMPASS_1,
                    Gauges.STYLE_COMPASS_DIGITAL_1,
                    Gauges.STYLE_COMPASS_2
                )
            }
            Tool.HYGROMETER -> {
                save(
                    Layouts.LAYOUT_SIMPLE,
                    Gauges.STYLE_GAUGE_1,
                    Gauges.STYLE_DIGITAL_1,
                    Gauges.STYLE_CHART_2
                )
            }
            Tool.LEVEL -> {
                save(
                    Layouts.LAYOUT_NORMAL,
                    Gauges.STYLE_LEVEL_1,
                    Gauges.STYLE_LEVEL_DIGITAL_1,
                    Gauges.STYLE_LEVEL_1
                )
            }
            Tool.LIGHT -> {
                save(
                    Layouts.LAYOUT_SIMPLE,
                    Gauges.STYLE_GAUGE_1,
                    Gauges.STYLE_DIGITAL_1,
                    Gauges.STYLE_CHART_2
                )
            }
            Tool.MAGNETOMETER -> {
                save(
                    Layouts.LAYOUT_SIMPLE,
                    Gauges.STYLE_GAUGE_1,
                    Gauges.STYLE_DIGITAL_1,
                    Gauges.STYLE_CHART_2
                )
            }
            Tool.THERMOMETER -> {
                save(
                    Layouts.LAYOUT_SIMPLE,
                    Gauges.STYLE_GAUGE_1,
                    Gauges.STYLE_DIGITAL_1,
                    Gauges.STYLE_CHART_2
                )
            }
            else -> throw IllegalArgumentException("${tool.name} is not a provider tool")
        }
    }

    fun setDefaultSamplingRate() {
        providerSamplingRate = tool.defaultSamplingRate
    }

    var isFirstTime: Boolean
        get() = sharedPreferences.getBoolean("first_time", true)
        set(value) = sharedPreferences.edit().putBoolean("first_time", value).apply()

    var layout: String
        get() = sharedPreferences.getString("layout", null)
            ?: throw IllegalStateException("No layout saved.")
        set(value) = sharedPreferences.edit().putString("layout", value).apply()

    fun getWidgetLayout(
        widgetPosition: String,
        defaultWidgetStyle: String = Gauges.STYLE_GAUGE_1
    ): Int {
        return Gauges.style(sharedPreferences.getString(widgetPosition, defaultWidgetStyle)!!)
    }

    fun setWidget(widgetPosition: String, style: String) {
        sharedPreferences.edit().putString(widgetPosition, style).apply()
    }

    var providerSamplingRate: Int
        get() = sharedPreferences.getString(
            "sampling_rate",
            "1"
        )!!.toInt() // use enum or something similar
        set(value) = sharedPreferences.edit().putString("sampling_rate", value.toString()).apply()

    var measurementUnit: MeasureUnit
        get() = MeasureUnit.valueOf(sharedPreferences.getString("unit", tool.defaultUnit.name)!!)
        set(value) = sharedPreferences.edit().putString("unit", value.name).apply()

    fun getDisplayParams(context: Context): DisplayParams {
        return when (tool) {
            Tool.ACCELEROMETER -> DisplayParams(
                decimalFormat = "#"
            )
                .applyMeasurementUnit(context, measurementUnit)
            Tool.BAROMETER -> DisplayParams(
                decimalFormat = "#"
            )
                .applyMeasurementUnit(context, measurementUnit)
            Tool.COMPASS -> DisplayParams(
                decimalFormat = "#"
            )
                .applyMeasurementUnit(context, measurementUnit)
            Tool.HYGROMETER -> DisplayParams(
                decimalFormat = "#.#"
            )
                .applyMeasurementUnit(context, measurementUnit)
            Tool.LEVEL -> DisplayParams(
                decimalFormat = "#.#"
            )
                .applyMeasurementUnit(context, measurementUnit)
            Tool.LIGHT -> DisplayParams(
                decimalFormat = "#"
            )
                .applyMeasurementUnit(context, measurementUnit)
            Tool.MAGNETOMETER -> DisplayParams(
                decimalFormat = "#"
            )
                .applyMeasurementUnit(context, measurementUnit)
            Tool.THERMOMETER -> DisplayParams(
                decimalFormat = "#.#"
            )
                .applyMeasurementUnit(context, measurementUnit)
            else -> throw IllegalArgumentException("${tool.name} is not a provider tool")
        }
    }

    private fun DisplayParams.applyMeasurementUnit(
        context: Context,
        unit: MeasureUnit
    ): DisplayParams {
        when (unit) {
            MeasureUnit.CELSIUS -> {
                minValue = -20
                maxValue = 100
            }
            MeasureUnit.FAHRENHEIT -> {
                minValue = 0
                maxValue = 200
            }
            MeasureUnit.KELVIN -> {
                minValue = 250
                maxValue = 350
            }
            MeasureUnit.H_PASCAL -> {
                minValue = 950
                maxValue = 1050
            }
            MeasureUnit.ATM -> {
                minValue = 1
                maxValue = 10
            }
            MeasureUnit.TORR -> {
                minValue = 650
                maxValue = 800
            }
            MeasureUnit.U_TESLA -> {
                minValue = 0
                maxValue = 1000
            }
            MeasureUnit.DEGREE -> {
                minValue = 0
                maxValue = 360
            }
            MeasureUnit.M_S_2 -> {
                minValue = 0
                maxValue = 100
            }
            MeasureUnit.LX -> {
                minValue = 0
                maxValue = 20000
            }
            MeasureUnit.PERCENT -> {
                minValue = 0
                maxValue = 100
            }
        }
        measurementUnit = context.getString(unit.symbol)
        return this
    }

    private fun save(layout: String, display1: String, display2: String, display3: String) {
        sharedPreferences.edit()
            .putString("layout", layout)
            .putString(Gauges.PREF_KEY_WIDGET_1, display1)
            .putString(Gauges.PREF_KEY_WIDGET_2, display2)
            .putString(Gauges.PREF_KEY_WIDGET_3, display3)
            .apply()
    }
}

val Tool.title: Int
    get() = when (this) {
        Tool.ACCELEROMETER -> R.string.accelerometer_title
        Tool.BAROMETER -> R.string.barometer_title
        Tool.COMPASS -> R.string.compass_title
        Tool.HYGROMETER -> R.string.hygrometer_title
        Tool.LEVEL -> R.string.level_title
        Tool.LIGHT -> R.string.light_meter_title
        Tool.MAGNETOMETER -> R.string.magnetometer_title
        Tool.THERMOMETER -> R.string.thermometer_title
        else -> throw IllegalArgumentException("$name is not a provider tool")
    }

val Tool.layoutMap: Map<String, Array<Section>>
    get() = when (this) {
        Tool.ACCELEROMETER -> basicLayouts
        Tool.BAROMETER -> basicLayouts
        Tool.COMPASS -> {
            val section1 = Section(
                Gauges.PREF_KEY_WIDGET_1,
                arrayOf(
                    Gauges.STYLE_COMPASS_1,
                    Gauges.STYLE_COMPASS_4,
                    Gauges.STYLE_COMPASS_2,
                    Gauges.STYLE_COMPASS_3
                )
            )
            val section2 =
                Section(Gauges.PREF_KEY_WIDGET_2, arrayOf(Gauges.STYLE_COMPASS_DIGITAL_1))
            mapOf(
                Layouts.LAYOUT_SIMPLE to arrayOf(section1),
                Layouts.LAYOUT_NORMAL to arrayOf(section1, section2)
            )
        }
        Tool.HYGROMETER -> basicLayouts
        Tool.LEVEL -> {
            val section1 = Section(Gauges.PREF_KEY_WIDGET_1, arrayOf(Gauges.STYLE_LEVEL_1))
            val section2 = Section(Gauges.PREF_KEY_WIDGET_2, arrayOf(Gauges.STYLE_LEVEL_DIGITAL_1))
            mapOf(
                Layouts.LAYOUT_SIMPLE to arrayOf(section1),
                Layouts.LAYOUT_NORMAL to arrayOf(section1, section2)
            )
        }
        Tool.LIGHT -> basicLayouts
        Tool.MAGNETOMETER -> basicLayouts
        Tool.THERMOMETER -> basicLayouts
        else -> throw IllegalArgumentException("$name is not a provider tool")
    }

private val basicLayouts: Map<String, Array<Section>> by lazy {
    val s1 = Section(
        Gauges.PREF_KEY_WIDGET_1,
        arrayOf(
            Gauges.STYLE_GAUGE_1,
            Gauges.STYLE_GAUGE_3,
            Gauges.STYLE_CHART_1,
            Gauges.STYLE_CHART_2
        )
    )
    val s2 =
        Section(Gauges.PREF_KEY_WIDGET_2, arrayOf(Gauges.STYLE_DIGITAL_1, Gauges.STYLE_CHART_2))
    val s3 =
        Section(Gauges.PREF_KEY_WIDGET_3, arrayOf(Gauges.STYLE_DIGITAL_1, Gauges.STYLE_CHART_2))
    mapOf(
        Layouts.LAYOUT_SIMPLE to arrayOf(s1),
        Layouts.LAYOUT_NORMAL to arrayOf(s1, s2),
        Layouts.LAYOUT_RICH to arrayOf(s1, s2, s3)
    )
}

val Tool.defaultUnit: MeasureUnit
    get() = when (this) {
        Tool.ACCELEROMETER -> MeasureUnit.M_S_2
        Tool.BAROMETER -> MeasureUnit.H_PASCAL
        Tool.COMPASS -> MeasureUnit.DEGREE
        Tool.HYGROMETER -> MeasureUnit.PERCENT
        Tool.LEVEL -> MeasureUnit.DEGREE
        Tool.LIGHT -> MeasureUnit.LX
        Tool.MAGNETOMETER -> MeasureUnit.U_TESLA
        Tool.THERMOMETER -> MeasureUnit.CELSIUS
        else -> throw IllegalArgumentException("$name is not a provider tool")
    }

val Tool.allSupportedUnits: List<MeasureUnit>
    get() = when (this) {
        Tool.ACCELEROMETER -> listOf(MeasureUnit.M_S_2)
        Tool.BAROMETER -> listOf(MeasureUnit.H_PASCAL) // MeasureUnit.ATM, MeasureUnit.TORR
        Tool.COMPASS -> listOf(MeasureUnit.DEGREE)
        Tool.HYGROMETER -> listOf(MeasureUnit.PERCENT)
        Tool.LEVEL -> listOf(MeasureUnit.DEGREE)
        Tool.LIGHT -> listOf(MeasureUnit.LX)
        Tool.MAGNETOMETER -> listOf(MeasureUnit.U_TESLA)
        Tool.THERMOMETER -> listOf(MeasureUnit.CELSIUS, MeasureUnit.FAHRENHEIT, MeasureUnit.KELVIN)
        else -> throw IllegalArgumentException("$name is not a provider tool")
    }

val Tool.defaultSamplingRate: Int
    get() = when (this) {
        Tool.ACCELEROMETER -> 1
        Tool.BAROMETER -> 2
        Tool.COMPASS -> 1
        Tool.HYGROMETER -> 2
        Tool.LEVEL -> 1
        Tool.LIGHT -> 2
        Tool.MAGNETOMETER -> 1
        Tool.THERMOMETER -> 2
        else -> throw IllegalArgumentException("$name is not a provider tool")
    }

val MeasureUnit.dataProcessor: DataProcessor?
    get() = when (this) {
        MeasureUnit.CELSIUS -> null
        MeasureUnit.FAHRENHEIT -> {
            object : DataProcessor {
                override fun process(data: SensorData): SensorData {
                    data.values[0] = celsiusToFahrenheit(data.module)
                    return data
                }
            }
        }
        MeasureUnit.KELVIN -> {
            object : DataProcessor {
                override fun process(data: SensorData): SensorData {
                    data.values[0] = celsiusToKelvin(data.module)
                    return data
                }
            }
        }
        MeasureUnit.H_PASCAL -> null
        MeasureUnit.ATM -> {
            object : DataProcessor {
                override fun process(data: SensorData): SensorData {
                    data.values[0] = hpaToAtm(data.module)
                    return data
                }
            }
        }
        MeasureUnit.TORR -> {
            object : DataProcessor {
                override fun process(data: SensorData): SensorData {
                    data.values[0] = hpaToTorr(data.module)
                    return data
                }
            }
        }
        MeasureUnit.M_S_2 -> null
        MeasureUnit.LX -> null
        MeasureUnit.DEGREE -> null
        MeasureUnit.U_TESLA -> null
        MeasureUnit.PERCENT -> null
    }

fun Tool.getSharedPreferences(context: Context): SharedPreferences =
    PreferencesResolver.resolvePreferences(context, this)

fun Tool.helper(context: Context): ToolPreferenceHelper {
    return ToolPreferenceHelper(this, getSharedPreferences(context))
}
