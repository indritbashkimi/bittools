package com.ibashkimi.provider.providerdata

class AmbientLightData(value: Double = .0) : UnidimensionalData(value) {

    val lightLevel: Double
        get() = module
}
