package com.ibashkimi.provider.provider

import com.ibashkimi.provider.implementor.SensorImplementor


class SoundLevelProvider(implementor: SensorImplementor) : BridgeProvider(implementor)