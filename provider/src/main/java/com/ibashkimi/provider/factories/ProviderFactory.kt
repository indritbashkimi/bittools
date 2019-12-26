package com.ibashkimi.provider.factories

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.ibashkimi.provider.filter.DummyFilter
import com.ibashkimi.provider.filter.LowPassFilter
import com.ibashkimi.provider.flow.*
import com.ibashkimi.provider.livedata.*
import com.ibashkimi.provider.provider.*
import com.ibashkimi.provider.provider.sound.SoundLevelProvider
import com.ibashkimi.provider.providerdata.*
import com.ibashkimi.provider.utils.PatternCreator
import kotlinx.coroutines.flow.Flow


object ProviderFactory {

    fun createHardwareProvider(
        context: Context,
        type: Int,
        samplingRate: Int
    ): Provider = when (type) {
        ProviderType.TYPE_ACCELEROMETER -> {
            AccelerationProvider(
                context,
                intArrayOf(Sensor.TYPE_ACCELEROMETER),
                AccelerometerData(),
                samplingRate
            )
        }
        ProviderType.TYPE_ALTIMETER -> {
            AltimeterProvider(context, 60000, AltimeterData())
        }
        ProviderType.TYPE_BAROMETER -> {
            SimpleAndroidProvider(
                context,
                intArrayOf(Sensor.TYPE_PRESSURE),
                samplingRate,
                LowPassFilter(0.15),
                BarometerData()
            )
        }
        ProviderType.TYPE_COMPASS -> {
            var sensor: Provider
            sensor = OrientationProvider(context, samplingRate)
            if (!sensor.isSupported) {
                sensor =
                    OrientationProvider2(
                        context,
                        samplingRate
                    )
                if (!sensor.isSupported) {
                    sensor =
                        OrientationProvider3(
                            context,
                            samplingRate
                        )
                    if (!sensor.isSupported) {
                        sensor =
                            OrientationProvider5(
                                context,
                                samplingRate
                            )
                    }
                }
            }
            sensor
        }
        ProviderType.TYPE_HYGROMETER -> {
            SimpleAndroidProvider(
                context,
                intArrayOf(Sensor.TYPE_RELATIVE_HUMIDITY),
                samplingRate,
                LowPassFilter(0.15),
                HygrometerData()
            )
        }
        ProviderType.TYPE_LEVEL -> {
            createHardwareProvider(context, ProviderType.TYPE_ORIENTATION, samplingRate)
        }
        ProviderType.TYPE_LIGHT_METER -> {
            SimpleAndroidProvider(
                context,
                intArrayOf(Sensor.TYPE_LIGHT),
                samplingRate,
                DummyFilter(),
                AmbientLightData()
            )
        }
        ProviderType.TYPE_MAGNETOMETER -> {
            NaiveAndroidProvider(
                context,
                intArrayOf(Sensor.TYPE_MAGNETIC_FIELD),
                samplingRate,
                MagnetometerData()
            )
        }
        ProviderType.TYPE_ORIENTATION -> {
            var sensor: Provider
            sensor = OrientationProvider(context, samplingRate)
            if (!sensor.isSupported) {
                sensor =
                    OrientationProvider2(
                        context,
                        samplingRate
                    )
                if (!sensor.isSupported) {
                    sensor =
                        OrientationProvider3(
                            context,
                            samplingRate
                        )
                    if (!sensor.isSupported) {
                        sensor =
                            OrientationProvider4(
                                context,
                                samplingRate
                            )
                        if (!sensor.isSupported) {
                            sensor =
                                OrientationProvider5(
                                    context,
                                    samplingRate
                                )
                        }
                    }
                }
            }
            sensor
        }
        ProviderType.TYPE_SOUND_LEVEL_METER -> {
            SoundLevelProvider(30, LowPassFilter(0.3))
        }
        ProviderType.TYPE_SPEED_METER -> {
            SpeedMeterProvider(context, 60000, SpeedMeterData())
        }
        ProviderType.TYPE_THERMOMETER -> {
            SimpleAndroidProvider(
                context,
                intArrayOf(Sensor.TYPE_AMBIENT_TEMPERATURE),
                samplingRate,
                LowPassFilter(0.15),
                ThermometerData()
            )
        }
        else -> throw Exception("Unknown sensor type $type.")
    }

    private fun createSimulatedProvider(type: Int, samplingRate: Int): Provider {
        val delay = when (samplingRate) {
            SensorManager.SENSOR_DELAY_FASTEST -> 15L
            SensorManager.SENSOR_DELAY_GAME -> 30L
            SensorManager.SENSOR_DELAY_UI -> 100L
            SensorManager.SENSOR_DELAY_NORMAL -> 200L
            else -> 200L
        }
        return when (type) {
            ProviderType.TYPE_ACCELEROMETER -> {
                throw Exception("Unsupported sensor type for simulation.")
            }
            ProviderType.TYPE_ALTIMETER -> {
                val values = PatternCreator.createFloat(15.0f, 25.0f, 0.1f)
                SimulatedProvider(values, delay)
            }
            ProviderType.TYPE_BAROMETER -> {
                val values = PatternCreator.createFloat(1000f, 1021f, 1)
                SimulatedProvider(values, delay)
            }
            ProviderType.TYPE_COMPASS -> {
                throw Exception("Unsupported sensor type for simulation.")
            }
            ProviderType.TYPE_HYGROMETER -> {
                val values = PatternCreator.createFloat(50f, 70f, 1)
                SimulatedProvider(values, delay)
            }
            ProviderType.TYPE_LEVEL -> {
                throw Exception("Unsupported sensor type for simulation.")
            }
            ProviderType.TYPE_LIGHT_METER -> {
                throw Exception("Unsupported sensor type for simulation.")
            }
            ProviderType.TYPE_MAGNETOMETER -> {
                val values = PatternCreator.createFloat(20f, 70f, 1)
                SimulatedProvider(values, delay)
            }
            ProviderType.TYPE_ORIENTATION -> {
                throw Exception("Unsupported sensor type for simulation.")
            }
            ProviderType.TYPE_SOUND_LEVEL_METER -> {
                val values = floatArrayOf(20f, 30f, 40f, 50f, 60f, 70f, 75f)
                SimulatedProvider(values, delay)
            }
            ProviderType.TYPE_SPEED_METER -> {
                val values = floatArrayOf(20f, 30f, 40f, 50f, 60f, 70f, 75f)
                SimulatedProvider(values, delay)
            }
            ProviderType.TYPE_THERMOMETER -> {
                val values = PatternCreator.createFloat(15.0f, 25.0f, 0.1f)
                SimulatedProvider(values, delay)
            }
            else -> throw Exception("Unknown sensor type $type.")
        }

    fun createProviderForDebugging(
        context: Context,
        type: Int,
        samplingRate: Int
    ): Provider {
        val hardwareProvider = createHardwareProvider(context, type, samplingRate)
        if (hardwareProvider.isSupported)
            return hardwareProvider
        return createSimulatedProvider(type, samplingRate)
    }

    fun getFlow(context: Context, type: Int, samplingRate: Int): Flow<SensorData> = when (type) {
        ProviderType.TYPE_ACCELEROMETER -> accelerometerFlow(context, samplingRate)
        ProviderType.TYPE_ALTIMETER -> altimeterFlow(context, samplingRate)
        ProviderType.TYPE_BAROMETER -> barometerFlow(context, samplingRate)
        ProviderType.TYPE_COMPASS -> compassFlow(context, samplingRate)
        ProviderType.TYPE_HYGROMETER -> hygrometerFlow(context, samplingRate)
        ProviderType.TYPE_LIGHT_METER -> lightMeterFlow(context, samplingRate)
        ProviderType.TYPE_LEVEL -> levelFlow(context, samplingRate)
        ProviderType.TYPE_MAGNETOMETER -> magnetometerFlow(context, samplingRate)
        ProviderType.TYPE_ORIENTATION -> orientationFlow(context, samplingRate)
        ProviderType.TYPE_SPEED_METER -> speedometerFlow(context, samplingRate)
        ProviderType.TYPE_SOUND_LEVEL_METER -> soundLevelMeterFlow(context, samplingRate)
        ProviderType.TYPE_THERMOMETER -> thermometerFlow(context, samplingRate)
        else -> throw IllegalStateException("Unknown sensor type $type.")
    }

    fun getLiveData(context: Context, type: Int, samplingRate: Int): SensorLiveData = when (type) {
        ProviderType.TYPE_ACCELEROMETER -> accelerometerLiveData(context, samplingRate)
        ProviderType.TYPE_ALTIMETER -> altimeterLiveData(context, samplingRate)
        ProviderType.TYPE_BAROMETER -> barometerLiveData(context, samplingRate)
        ProviderType.TYPE_COMPASS -> compassLiveData(context, samplingRate)
        ProviderType.TYPE_HYGROMETER -> hygrometerLiveData(context, samplingRate)
        ProviderType.TYPE_LIGHT_METER -> lightMeterLiveData(context, samplingRate)
        ProviderType.TYPE_LEVEL -> levelLiveData(context, samplingRate)
        ProviderType.TYPE_MAGNETOMETER -> magnetometerLiveData(context, samplingRate)
        ProviderType.TYPE_ORIENTATION -> orientationLiveData(context, samplingRate)
        ProviderType.TYPE_SPEED_METER -> speedometerLiveData(context, samplingRate)
        ProviderType.TYPE_SOUND_LEVEL_METER -> soundLevelLiveData(context, samplingRate)
        ProviderType.TYPE_THERMOMETER -> thermometerLiveData(context, samplingRate)
        else -> throw IllegalStateException("Unknown sensor type $type.")
    }

}
