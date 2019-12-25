package com.ibashkimi.provider.provider

import android.content.Context
import android.hardware.SensorEvent
import com.ibashkimi.provider.providerdata.ThreeDimensionalData


open class NaiveAndroidProvider(
    context: Context,
    sensors: IntArray,
    samplingRate: Int,
    val data: ThreeDimensionalData = ThreeDimensionalData()
) : AndroidProvider(context, sensors, samplingRate) {

    override fun onSensorChanged(event: SensorEvent) {
        val v = event.values.clone()
        data.values[0] = v[0].toDouble()
        data.values[1] = v[1].toDouble()
        data.values[2] = v[2].toDouble()
        onDataChanged(data)
    }
}
