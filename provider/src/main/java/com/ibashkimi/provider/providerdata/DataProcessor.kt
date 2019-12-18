package com.ibashkimi.provider.providerdata

interface DataProcessor {
    fun process(data: SensorData): SensorData
}