package com.ibashkimi.provider.provider

import com.ibashkimi.provider.providerdata.SensorData


interface ProviderDataListener {
    fun onDataChanged(data: SensorData)
}
