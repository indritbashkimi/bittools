package com.ibashkimi.provider.providerdata

class ThermometerData(value: Double = .0) : UnidimensionalData(value) {

    val temperature: Double
        get() = module
}
