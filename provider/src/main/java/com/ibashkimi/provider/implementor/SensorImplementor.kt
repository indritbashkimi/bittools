package com.ibashkimi.provider.implementor

import com.ibashkimi.provider.provider.BridgeProvider


interface SensorImplementor {

    val isSimulated: Boolean

    val isSupported: Boolean

    fun bind(provider: BridgeProvider)

    fun startListening()

    fun stopListening()
}
