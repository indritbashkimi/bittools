package com.ibashkimi.provider.provider

import android.content.Context
import android.location.Location
import com.ibashkimi.provider.providerdata.UnidimensionalData

class SpeedMeterProvider(
    context: Context,
    minTime: Int,
    private val data: UnidimensionalData = UnidimensionalData()
) : GpsBasedProvider(context, minTime) {

    override fun onLocationChanged(location: Location) {
        data.values[0] = location.speed.toDouble()
        onDataChanged(data)
    }
}