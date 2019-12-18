package com.ibashkimi.provider.implementor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.view.Display
import android.view.Surface
import android.view.WindowManager
import com.ibashkimi.provider.filter.AngleLowPassFilter
import com.ibashkimi.provider.filter.Filter
import com.ibashkimi.provider.providerdata.OrientationData

/**
 * https://developer.android.com/guide/topics/sensors/sensors_position.html
 */
class OrientationSensorImplementor(
    context: Context,
    samplingPeriodUs: Int,
    val data: OrientationData = OrientationData()
) : AndroidSensorImplementor(
    context,
    intArrayOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_MAGNETIC_FIELD),
    samplingPeriodUs
) {

    private val filterAzimuth: Filter = AngleLowPassFilter(7)
    private val filterPitch: Filter = AngleLowPassFilter(7)
    private val filterRoll: Filter = AngleLowPassFilter(7)

    private var accelerometerReading = FloatArray(3)
    private var magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val mRout = FloatArray(9)
    private val mI = FloatArray(9) // TODO: is this necessary?
    private val orientationAngles = FloatArray(3)

    private var azimuth: Double = 0.0
    private var roll: Double = 0.0
    private var pitch: Double = 0.0

    private var display: Display =
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay

    private var axisX: Int = 0
    private var axisY: Int = 0

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(
                    event.values,
                    0,
                    accelerometerReading,
                    0,
                    accelerometerReading.size
                )
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
            }
            else -> return
        }

        computeData()
    }

    private fun computeData() {
        // http://stackoverflow.com/questions/18782829/android-sensormanager-strange-how-to-remapcoordinatesystem
        val success = SensorManager.getRotationMatrix(
            rotationMatrix,
            mI,
            accelerometerReading,
            magnetometerReading
        )

        if (success) {
            when (display.rotation) {
                Surface.ROTATION_0 -> {
                    axisX = SensorManager.AXIS_X
                    axisY = SensorManager.AXIS_Y
                }
                Surface.ROTATION_90 -> {
                    axisX = SensorManager.AXIS_Y
                    axisY = SensorManager.AXIS_MINUS_X
                }
                Surface.ROTATION_180 -> {
                    axisX = SensorManager.AXIS_MINUS_X
                    axisY = SensorManager.AXIS_MINUS_Y
                }
                Surface.ROTATION_270 -> {
                    axisX = SensorManager.AXIS_MINUS_Y
                    axisY = SensorManager.AXIS_X
                }
                else -> return
            }

            // NOTE: rotationMatrix e mRout should not be the same!
            val remapped = SensorManager.remapCoordinateSystem(rotationMatrix, axisX, axisY, mRout)
            if (remapped) {
                SensorManager.getOrientation(mRout, orientationAngles)
                // http://developer.android.com/guide/topics/sensors/sensors_motion.html

                //azimuth = orientationAngles[0].toDouble()
                //pitch = orientationAngles[1].toDouble()
                //roll = orientationAngles[2].toDouble()
                /* Azimuth Z */

                /*if (azimuth < 0)
                    // [-180 : 180] -> [0 : 360]
                    azimuth = (float) (2*Math.PI + azimuth);*/

                /* Azimuth Z */
                azimuth = Math.toDegrees(filterAzimuth.doJob(orientationAngles[0].toDouble()))
                /* Pitch Y*/
                pitch = Math.toDegrees(filterPitch.doJob(orientationAngles[1].toDouble()))
                /* Roll X */
                roll = Math.toDegrees(filterRoll.doJob(orientationAngles[2].toDouble()))

                if (pitch > 90) {
                    pitch = 90 - pitch % 90
                } else if (pitch < -90) {
                    pitch = -90 - pitch % 90
                }
                if (roll > 90)
                    roll = 90 - roll % 90
                else if (roll < -90)
                    roll = -90 - roll % 90
            }
        }
        data.values[0] = azimuth
        data.values[1] = pitch
        data.values[2] = roll
        // Don't setValue if the value hasn't change
        listener.onDataChanged(data)
    }
}
