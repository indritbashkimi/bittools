package com.ibashkimi.provider.providerdata

import android.location.Location

class GpsData(altitude: Double, speed: Double, latitude: Double, longitude: Double) :
    SensorData(doubleArrayOf(altitude, speed, latitude, longitude)) {

    override val module = 0.0

    val altitude: Double
        get() = values[0]

    val speed: Double
        get() = values[1]

    val latitude: Double
        get() = values[2]

    val longitude: Double
        get() = values[3]

    var location: Location? = null
}
