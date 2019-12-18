package com.ibashkimi.provider.provider

import com.ibashkimi.provider.implementor.SensorImplementor
import com.ibashkimi.provider.providerdata.DataProcessor
import com.ibashkimi.provider.providerdata.SensorData


open class BridgeProvider(
    private val implementor: SensorImplementor,
    private val dataProcessor: DataProcessor? = null
) : AbstractProvider() {

    override val isSupported: Boolean = implementor.isSupported

    init {
        implementor.bind(this)
    }

    open fun onDataChanged(data: SensorData) {
        val processedData = dataProcessor?.process(data) ?: data //postProcessData(data)
        for (listener in listeners) {
            listener.onDataChanged(processedData)
        }
    }

    override fun startListening() {
        if (!isListening) { //  && listeners.size > 0
            implementor.startListening()
        }
        super.startListening()
    }

    override fun stopListening() {
        implementor.stopListening()
        super.stopListening()
    }

    /*protected open fun postProcessData(data: SensorData): SensorData {
        return dataProcessor?.process(data) ?: data
    }*/
}
