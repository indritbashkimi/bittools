package com.ibashkimi.provider.providerdata


class SpeedMeterData(value: Double = .0) : UnidimensionalData(value) {

    val speed: Double
        get() = module
}
