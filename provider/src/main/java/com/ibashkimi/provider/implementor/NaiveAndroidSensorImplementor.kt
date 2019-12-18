package com.ibashkimi.provider.implementor

import android.content.Context
import android.hardware.SensorEvent
import com.ibashkimi.provider.providerdata.ThreeDimensionalData


open class NaiveAndroidSensorImplementor(
    context: Context,
    sensors: IntArray,
    samplingRate: Int,
    val data: ThreeDimensionalData = ThreeDimensionalData()
) : AndroidSensorImplementor(context, sensors, samplingRate) {

    override fun onSensorChanged(event: SensorEvent) {
        val v = event.values.clone()
        data.values[0] = v[0].toDouble()
        data.values[1] = v[1].toDouble()
        data.values[2] = v[2].toDouble()
        listener.onDataChanged(data)
    }
}
