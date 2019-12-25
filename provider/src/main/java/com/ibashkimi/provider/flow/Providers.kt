package com.ibashkimi.provider.flow

import android.content.Context
import com.ibashkimi.provider.factories.ProviderFactory
import com.ibashkimi.provider.provider.ProviderListener
import com.ibashkimi.provider.provider.ProviderType
import com.ibashkimi.provider.providerdata.SensorData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun sensorFlow(
    context: Context,
    type: Int,
    samplingRate: Int
): Flow<SensorData> = callbackFlow {
    val listener = ProviderListener { offer(it) }
    val provider =
        ProviderFactory.createProviderForDebugging(context, type, samplingRate)
    provider.register(listener)
    awaitClose {
        provider.unregister(listener)
    }
}

fun accelerometerFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_ACCELEROMETER, samplingRate)

fun altimeterFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_ALTIMETER, samplingRate)

fun barometerFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_BAROMETER, samplingRate)

fun compassFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_COMPASS, samplingRate)

fun hygrometerFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_HYGROMETER, samplingRate)

fun lightMeterFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_LIGHT_METER, samplingRate)

fun levelFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_LEVEL, samplingRate)

fun magnetometerFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_MAGNETOMETER, samplingRate)

fun orientationFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_ORIENTATION, samplingRate)

fun speedometerFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_SPEED_METER, samplingRate)

fun soundLevelMeterFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_SOUND_LEVEL_METER, samplingRate)

fun thermometerFlow(context: Context, samplingRate: Int) =
    sensorFlow(context, ProviderType.TYPE_THERMOMETER, samplingRate)
