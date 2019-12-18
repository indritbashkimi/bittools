package com.ibashkimi.provider.provider

import com.ibashkimi.provider.implementor.SensorImplementor

class Accelerometer(implementor: SensorImplementor) : BridgeProvider(implementor)