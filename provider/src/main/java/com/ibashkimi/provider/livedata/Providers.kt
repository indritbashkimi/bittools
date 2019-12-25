package com.ibashkimi.provider.livedata

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.ibashkimi.provider.factories.ProviderFactory
import com.ibashkimi.provider.provider.Provider
import com.ibashkimi.provider.provider.ProviderListener
import com.ibashkimi.provider.provider.ProviderType.*
import com.ibashkimi.provider.providerdata.SensorData

class SensorLiveData(
    val context: Context,
    val type: Int,
    val samplingRate: Int
) : MutableLiveData<SensorData>() {

    private val listener = ProviderListener { value = it }

    private val provider =
        ProviderFactory.createProviderForDebugging(context, type, samplingRate)

    var isListening: Boolean = false
        private set

    override fun onActive() {
        super.onActive()
        resume()
    }

    override fun onInactive() {
        super.onInactive()
        stop()
    }

    fun stop() {
        provider.unregister(listener)
        isListening = false
    }

    fun resume() {
        provider.register(listener)
        isListening = true
    }
}

fun accelerometerLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_ACCELEROMETER, samplingRate)

fun altimeterLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_ALTIMETER, samplingRate)

fun barometerLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_BAROMETER, samplingRate)

fun compassLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_COMPASS, samplingRate)

fun hygrometerLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_HYGROMETER, samplingRate)

fun lightMeterLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_LIGHT_METER, samplingRate)

fun levelLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_LEVEL, samplingRate)

fun magnetometerLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_MAGNETOMETER, samplingRate)

fun orientationLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_ORIENTATION, samplingRate)

fun speedometerLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_SPEED_METER, samplingRate)

fun soundLevelLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_SOUND_LEVEL_METER, samplingRate)

fun thermometerLiveData(context: Context, samplingRate: Int) =
    SensorLiveData(context, TYPE_THERMOMETER, samplingRate)
