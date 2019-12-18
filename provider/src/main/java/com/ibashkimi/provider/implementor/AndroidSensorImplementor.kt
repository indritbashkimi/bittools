package com.ibashkimi.provider.implementor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

import com.ibashkimi.provider.provider.BridgeProvider


abstract class AndroidSensorImplementor(
    context: Context,
    sensors: IntArray,
    private val samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_UI
) : AbstractSensorImplementor(), SensorEventListener {

    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val sensors: IntArray = sensors.clone()

    override val isSimulated: Boolean = false

    override val isSupported: Boolean by lazy { checkSupport() }

    private fun checkSupport(): Boolean {
        for (sensor in this.sensors) {
            if (!sensorManager.registerListener(
                    this,
                    sensorManager.getDefaultSensor(sensor),
                    samplingPeriodUs
                )
            ) {
                sensorManager.unregisterListener(this)
                return false
            }
        }
        sensorManager.unregisterListener(this)
        return true
    }

    override fun bind(provider: BridgeProvider) {
        this.listener = provider
    }

    @Synchronized
    override fun startListening() {
        for (sensor in this.sensors) {
            val res = sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(sensor),
                samplingPeriodUs
            )
            if (res) {
                Log.d(TAG, "register to android system")
            } else {
                //throw Exception("Cannot register sensor $sensor to SensorManager. Check if sensor is supported.")
                Log.d(
                    TAG,
                    "Cannot register sensor $sensor to SensorManager. Check if sensor is supported."
                )
            }
        }
    }

    @Synchronized
    override fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    abstract override fun onSensorChanged(event: SensorEvent)

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    companion object {
        private val TAG = AndroidSensorImplementor::class.java.simpleName
    }
}
