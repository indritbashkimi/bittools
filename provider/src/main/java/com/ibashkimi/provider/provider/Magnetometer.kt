package com.ibashkimi.provider.provider

import com.ibashkimi.provider.implementor.SensorImplementor

class Magnetometer(implementor: SensorImplementor) : BridgeProvider(implementor)
