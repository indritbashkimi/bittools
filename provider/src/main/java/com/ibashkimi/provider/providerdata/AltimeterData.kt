package com.ibashkimi.provider.providerdata

class AltimeterData(value: Double = 0.0) : UnidimensionalData(value) {

    val altitude: Double
        get() = module
}
