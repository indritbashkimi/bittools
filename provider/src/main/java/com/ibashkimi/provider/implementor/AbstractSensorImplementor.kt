package com.ibashkimi.provider.implementor

import com.ibashkimi.provider.provider.BridgeProvider


abstract class AbstractSensorImplementor : SensorImplementor {

    protected lateinit var listener: BridgeProvider

    override fun bind(provider: BridgeProvider) {
        this.listener = provider
    }
}
