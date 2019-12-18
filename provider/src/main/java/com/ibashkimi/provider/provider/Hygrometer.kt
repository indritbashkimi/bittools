package com.ibashkimi.provider.provider

import com.ibashkimi.provider.implementor.SensorImplementor

class Hygrometer(implementor: SensorImplementor) : BridgeProvider(implementor)