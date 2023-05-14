package com.ibashkimi.provider.providerdata

class SoundLevelData(value: Double = .0) : UnidimensionalData(value) {

    val decibel: Double
        get() = module
}