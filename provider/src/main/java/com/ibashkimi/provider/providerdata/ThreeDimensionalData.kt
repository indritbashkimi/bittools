package com.ibashkimi.provider.providerdata

open class ThreeDimensionalData(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) :
    SensorData(doubleArrayOf(x, y, z)) {

    override val module: Double
        get() = Math.sqrt(x * x + y * y + z * z)

    val x: Double
        get() = values[0]

    val y: Double
        get() = values[1]

    val z: Double
        get() = values[2]
}
