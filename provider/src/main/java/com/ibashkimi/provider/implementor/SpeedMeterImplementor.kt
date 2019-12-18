package com.ibashkimi.provider.implementor

import android.content.Context
import android.location.Location
import com.ibashkimi.provider.providerdata.UnidimensionalData


class SpeedMeterImplementor(
    context: Context,
    minTime: Int,
    private val data: UnidimensionalData = UnidimensionalData()
) : GpsBasedImplementor(context, minTime) {

    override fun onLocationChanged(location: Location) {
        data.values[0] = location.speed.toDouble()
    }
}