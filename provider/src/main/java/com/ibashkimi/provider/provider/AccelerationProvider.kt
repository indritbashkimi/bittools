package com.ibashkimi.provider.provider

import android.content.Context
import android.hardware.SensorEvent
import android.hardware.SensorManager
import com.ibashkimi.provider.providerdata.ThreeDimensionalData
import kotlin.math.abs
import kotlin.math.sqrt

class AccelerationProvider(
    context: Context,
    sensors: IntArray,
    val data: ThreeDimensionalData = ThreeDimensionalData(),
    samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_UI
) : AndroidProvider(context, sensors, samplingPeriodUs) {

    private var adaptiveAccelerationFilter = true

    private var lastAcceleration = FloatArray(3)

    private var accelerationFilter = FloatArray(3)

    override fun onSensorChanged(event: SensorEvent) {
        onAccelerometerChanged(event.values[0], event.values[1], event.values[2])
    }

    private fun onAccelerometerChanged(x: Float, y: Float, z: Float) {
        // high pass filter
        val updateFreq = 50f // match this to your update speed
        val cutOffFreq = 0.9f
        val rc = 1.0f / cutOffFreq
        val dt = 1.0f / updateFreq
        val filterConstant = rc / (dt + rc)
        var alpha = filterConstant
        val kAccelerometerMinStep = 0.033f
        val kAccelerometerNoiseAttenuation = 3.0f

        if (adaptiveAccelerationFilter) {
            val d = clamp(
                abs(
                    norm(
                        accelerationFilter[0].toDouble(),
                        accelerationFilter[1].toDouble(),
                        accelerationFilter[2].toDouble()
                    ) - norm(x.toDouble(), y.toDouble(), z.toDouble())
                ) / kAccelerometerMinStep - 1.0f, 0.0, 1.0
            ).toFloat()
            alpha =
                d * filterConstant / kAccelerometerNoiseAttenuation + (1.0f - d) * filterConstant
        }

        accelerationFilter[0] = (alpha * (accelerationFilter[0] + x - lastAcceleration[0]))
        accelerationFilter[1] = (alpha * (accelerationFilter[1] + y - lastAcceleration[1]))
        accelerationFilter[2] = (alpha * (accelerationFilter[2] + z - lastAcceleration[2]))

        lastAcceleration[0] = x
        lastAcceleration[1] = y
        lastAcceleration[2] = z
        onFilteredAccelerometerChanged(
            accelerationFilter[0],
            accelerationFilter[1],
            accelerationFilter[2]
        )
    }

    private fun clamp(v: Double, min: Double, max: Double): Double {
        return when {
            v > max -> max
            v < min -> min
            else -> v
        }
    }

    private fun norm(x: Double, y: Double, z: Double): Double = sqrt(x * x + y * y + z * z)

    private fun onFilteredAccelerometerChanged(v1: Float, v2: Float, v3: Float) {
        data.values[0] = v1.toDouble()
        data.values[1] = v2.toDouble()
        data.values[2] = v3.toDouble()
        onDataChanged(data)
    }
}
