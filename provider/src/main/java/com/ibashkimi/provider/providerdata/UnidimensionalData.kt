package com.ibashkimi.provider.providerdata


open class UnidimensionalData(value: Double = 0.0) : SensorData(doubleArrayOf(value)) {

    override val module: Double
        get() = values[0]
}
