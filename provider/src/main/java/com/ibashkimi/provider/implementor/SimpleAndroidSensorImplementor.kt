package com.ibashkimi.provider.implementor

import android.content.Context
import android.hardware.SensorEvent
import com.ibashkimi.provider.filter.Filter
import com.ibashkimi.provider.providerdata.UnidimensionalData


open class SimpleAndroidSensorImplementor(
    context: Context,
    sensors: IntArray,
    samplingRate: Int,
    protected val filter: Filter,
    protected val data: UnidimensionalData = UnidimensionalData()
) : AndroidSensorImplementor(context, sensors, samplingRate) {

    override fun onSensorChanged(event: SensorEvent) {
        data.values[0] = filter.doJob(event.values[0].toDouble())
        listener.onDataChanged(data)
    }
}
