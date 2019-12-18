package com.ibashkimi.provider.providerdata

import java.util.*


abstract class SensorData(val values: DoubleArray) {

    abstract val module: Double

    val valueLength: Int = values.size

    fun valueAt(position: Int): Double {
        return values[position]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SensorData

        if (!Arrays.equals(values, other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(values)
        result = 31 * result + module.hashCode()
        result = 31 * result + valueLength
        return result
    }
}