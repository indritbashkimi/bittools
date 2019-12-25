package com.ibashkimi.provider.provider

import android.content.Context
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.ibashkimi.provider.filter.Filter
import com.ibashkimi.provider.providerdata.UnidimensionalData
import kotlin.math.sqrt


class FilteredProvider(
    context: Context,
    sensors: IntArray,
    val filter: Filter,
    val data: UnidimensionalData = UnidimensionalData(),
    samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_UI
) : AndroidProvider(context, sensors, samplingPeriodUs) {

    override fun onSensorChanged(event: SensorEvent) {
        val v = event.values.clone()
        data.values[0] =
            filter.doJob(sqrt((v[0] * v[0] + v[1] * v[1] + v[2] * v[2]).toDouble()))
        onDataChanged(data)
    }
}
