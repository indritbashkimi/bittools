package com.ibashkimi.provider.factories

import android.content.Context
import android.hardware.Sensor
import com.ibashkimi.provider.filter.DummyFilter
import com.ibashkimi.provider.filter.LowPassFilter
import com.ibashkimi.provider.implementor.*
import com.ibashkimi.provider.implementor.sound.SoundSensorImplementor
import com.ibashkimi.provider.provider.*
import com.ibashkimi.provider.providerdata.*
import com.ibashkimi.provider.utils.PatternCreator


object ProviderFactory {

    fun createHardwareProvider(
        context: Context,
        type: Int,
        samplingRate: Int,
        processor: DataProcessor? = null
    ): Provider = when (type) {
        ProviderType.TYPE_ACCELEROMETER -> {
            val implementor = AccelerationSensorImplementor(
                context,
                intArrayOf(Sensor.TYPE_ACCELEROMETER),
                AccelerometerData(),
                samplingRate
            )
            Accelerometer(implementor)
        }
        ProviderType.TYPE_ALTIMETER -> {
            val implementor = AltimeterImplementor(context, 60000, AltimeterData())
            Altimeter(implementor, processor)
        }
        ProviderType.TYPE_BAROMETER -> {
            val implementor = SimpleAndroidSensorImplementor(
                context,
                intArrayOf(Sensor.TYPE_PRESSURE),
                samplingRate,
                LowPassFilter(0.15),
                BarometerData()
            )
            Barometer(implementor, processor)
        }
        ProviderType.TYPE_COMPASS -> {
            var implementor: SensorImplementor
            implementor = OrientationSensorImplementor(context, samplingRate)
            if (!implementor.isSupported) {
                implementor = OrientationSensorImplementor2(context, samplingRate)
                if (!implementor.isSupported) {
                    implementor = OrientationSensorImplementor3(context, samplingRate)
                    if (!implementor.isSupported) {
                        implementor = OrientationSensorImplementor5(context, samplingRate)
                    }
                }
            }
            OrientationProvider(implementor)
        }
        ProviderType.TYPE_HYGROMETER -> {
            val implementor = SimpleAndroidSensorImplementor(
                context,
                intArrayOf(Sensor.TYPE_RELATIVE_HUMIDITY),
                samplingRate,
                LowPassFilter(0.15),
                HygrometerData()
            )
            Hygrometer(implementor)
        }
        ProviderType.TYPE_LEVEL -> {
            createHardwareProvider(context, ProviderType.TYPE_ORIENTATION, samplingRate)
        }
        ProviderType.TYPE_LIGHT_METER -> {
            val implementor = SimpleAndroidSensorImplementor(
                context,
                intArrayOf(Sensor.TYPE_LIGHT),
                samplingRate,
                DummyFilter(),
                AmbientLightData()
            )
            LightProvider(implementor)
        }
        ProviderType.TYPE_MAGNETOMETER -> {
            val implementor = NaiveAndroidSensorImplementor(
                context,
                intArrayOf(Sensor.TYPE_MAGNETIC_FIELD),
                samplingRate,
                MagnetometerData()
            )
            Magnetometer(implementor)
        }
        ProviderType.TYPE_ORIENTATION -> {
            var implementor: SensorImplementor
            implementor = OrientationSensorImplementor(context, samplingRate)
            if (!implementor.isSupported) {
                implementor = OrientationSensorImplementor2(context, samplingRate)
                if (!implementor.isSupported) {
                    implementor = OrientationSensorImplementor3(context, samplingRate)
                    if (!implementor.isSupported) {
                        implementor = OrientationSensorImplementor4(context, samplingRate)
                        if (!implementor.isSupported) {
                            implementor = OrientationSensorImplementor5(context, samplingRate)
                        }
                    }
                }
            }
            OrientationProvider(implementor)
        }
        ProviderType.TYPE_SOUND_LEVEL_METER -> {
            val impl = SoundSensorImplementor(30, LowPassFilter(0.3))
            SoundLevelProvider(impl)
        }
        ProviderType.TYPE_SPEED_METER -> {
            SpeedMeterProvider(SpeedMeterImplementor(context, 60000, SpeedMeterData()))
        }
        ProviderType.TYPE_THERMOMETER -> {
            val implementor = SimpleAndroidSensorImplementor(
                context,
                intArrayOf(Sensor.TYPE_AMBIENT_TEMPERATURE),
                samplingRate,
                LowPassFilter(0.15),
                ThermometerData()
            )
            Thermometer(implementor, processor)
        }
        else -> throw Exception("Unknown sensor type $type.")
    }

    private fun createSimulatedProvider(type: Int, processor: DataProcessor? = null): Provider =
        when (type) {
            ProviderType.TYPE_ACCELEROMETER -> {
                throw Exception("Unsupported sensor type for simulation.")
            }
            ProviderType.TYPE_ALTIMETER -> {
                val values = PatternCreator.createFloat(15.0f, 25.0f, 0.1f)
                Altimeter(SimulatedImplementor(values, 100), processor)
            }
            ProviderType.TYPE_BAROMETER -> {
                val values = PatternCreator.createFloat(1000f, 1021f, 1)
                Barometer(SimulatedImplementor(values, 150), processor)
            }
            ProviderType.TYPE_COMPASS -> {
                throw Exception("Unsupported sensor type for simulation.")
            }
            ProviderType.TYPE_HYGROMETER -> {
                val values = PatternCreator.createFloat(50f, 70f, 1)
                Hygrometer(SimulatedImplementor(values, 150))
            }
            ProviderType.TYPE_LEVEL -> {
                throw Exception("Unsupported sensor type for simulation.")
            }
            ProviderType.TYPE_LIGHT_METER -> {
                throw Exception("Unsupported sensor type for simulation.")
            }
            ProviderType.TYPE_MAGNETOMETER -> {
                val values = PatternCreator.createFloat(20f, 70f, 1)
                Magnetometer(SimulatedImplementor(values, 150L))
            }
            ProviderType.TYPE_ORIENTATION -> {
                throw Exception("Unsupported sensor type for simulation.")
            }
            ProviderType.TYPE_SOUND_LEVEL_METER -> {
                val values = floatArrayOf(20f, 30f, 40f, 50f, 60f, 70f, 75f)
                SoundLevelProvider(SimulatedImplementor(values, 300))
            }
            ProviderType.TYPE_SPEED_METER -> {
                val values = floatArrayOf(20f, 30f, 40f, 50f, 60f, 70f, 75f)
                SpeedMeterProvider(SimulatedImplementor(values, 300), processor)
            }
            ProviderType.TYPE_THERMOMETER -> {
                val values = PatternCreator.createFloat(15.0f, 25.0f, 0.1f)
                Thermometer(SimulatedImplementor(values, 100), processor)
            }
            else -> throw Exception("Unknown sensor type $type.")
        }

    fun createDebugProvider(
        context: Context,
        type: Int,
        samplingRate: Int,
        processor: DataProcessor? = null
    ): Provider = when (type) {
        ProviderType.TYPE_ACCELEROMETER -> {
            createHardwareProvider(context, type, samplingRate, processor)
        }
        ProviderType.TYPE_ALTIMETER -> {
            createSimulatedProvider(type, processor)
        }
        ProviderType.TYPE_BAROMETER -> {
            createSimulatedProvider(type, processor)
        }
        ProviderType.TYPE_COMPASS -> {
            createHardwareProvider(context, type, samplingRate, processor)
        }
        ProviderType.TYPE_HYGROMETER -> {
            createSimulatedProvider(type, processor)
        }
        ProviderType.TYPE_LEVEL -> {
            createHardwareProvider(context, type, samplingRate, processor)
        }
        ProviderType.TYPE_LIGHT_METER -> {
            createHardwareProvider(context, type, samplingRate, processor)
        }
        ProviderType.TYPE_MAGNETOMETER -> {
            createSimulatedProvider(type, processor)
        }
        ProviderType.TYPE_ORIENTATION -> {
            createHardwareProvider(context, type, samplingRate, processor)
        }
        ProviderType.TYPE_SOUND_LEVEL_METER -> {
            createSimulatedProvider(type, processor)
        }
        ProviderType.TYPE_SPEED_METER -> {
            createSimulatedProvider(type, processor)
        }
        ProviderType.TYPE_THERMOMETER -> {
            createSimulatedProvider(type, processor)
        }
        else -> throw Exception("Unknown sensor type $type.")
    }
}
