package com.ibashkimi.provider.provider

import com.ibashkimi.provider.implementor.SensorImplementor
import com.ibashkimi.provider.providerdata.DataProcessor

class Thermometer(implementor: SensorImplementor, dataProcessor: DataProcessor? = null) :
    BridgeProvider(implementor, dataProcessor)