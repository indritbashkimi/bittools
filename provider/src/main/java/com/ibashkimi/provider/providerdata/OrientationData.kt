package com.ibashkimi.provider.providerdata

class OrientationData(azimuth: Double = 0.0, pitch: Double = 0.0, roll: Double = 0.0) :
    SensorData(doubleArrayOf(azimuth, pitch, roll)) {

    override val module: Double = 0.0

    val azimuth: Double
        get() = values[0]

    val pitch: Double
        get() = values[1]

    val roll: Double
        get() = values[2]
}