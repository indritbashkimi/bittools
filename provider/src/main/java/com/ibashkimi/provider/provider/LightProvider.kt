package com.ibashkimi.provider.provider

import com.ibashkimi.provider.implementor.SensorImplementor

class LightProvider(implementor: SensorImplementor) : BridgeProvider(implementor)