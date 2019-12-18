package com.ibashkimi.provider.providerdata

class BarometerData(value: Double = 0.0) : UnidimensionalData(value) {

    val pressure: Double
        get() = module
}
