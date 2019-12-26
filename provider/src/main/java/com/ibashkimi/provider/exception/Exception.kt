package com.ibashkimi.provider.exception

class UnsupportedSensorTypeException(type: Int) :
    IllegalArgumentException("Unsupported sensor type $type.")

class UnsupportedSensorForSimulationException(type: Int) :
    IllegalArgumentException("Unsupported sensor type $type for simulation.")