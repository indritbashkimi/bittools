package com.ibashkimi.provider.implementor

import android.content.Context
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.ibashkimi.provider.filter.Filter
import com.ibashkimi.provider.providerdata.UnidimensionalData


class FilteredSensorImplementor(
    context: Context,
    sensors: IntArray,
    val filter: Filter,
    val data: UnidimensionalData = UnidimensionalData(),
    samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_UI
) : AndroidSensorImplementor(context, sensors, samplingPeriodUs) {

    override fun onSensorChanged(event: SensorEvent) {
        val v = event.values.clone()
        data.values[0] =
            filter.doJob(Math.sqrt((v[0] * v[0] + v[1] * v[1] + v[2] * v[2]).toDouble()))
        listener.onDataChanged(data)
    }
}
