package com.ibashkimi.provider.provider

import com.ibashkimi.provider.implementor.SensorImplementor

class OrientationProvider(implementor: SensorImplementor) : BridgeProvider(implementor)
