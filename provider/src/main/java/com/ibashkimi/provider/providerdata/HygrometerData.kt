package com.ibashkimi.provider.providerdata


class HygrometerData(value: Double = 0.0) : UnidimensionalData(value) {

    val relativeHumidity: Double
        get() = module
}