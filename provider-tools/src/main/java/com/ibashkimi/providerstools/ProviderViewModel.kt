package com.ibashkimi.providerstools

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ibashkimi.provider.factories.ProviderFactory
import com.ibashkimi.provider.livedata.SensorLiveData
import com.ibashkimi.provider.providerdata.SensorData
import com.ibashkimi.providerstools.data.MeasureUnit
import com.ibashkimi.providerstools.data.dataProcessor
import com.ibashkimi.providerstools.data.helper
import com.ibashkimi.providerstools.data.providerType
import com.ibashkimi.shared.Tool

class ProviderViewModel(app: Application) : AndroidViewModel(app) {

    private val _tool = MutableLiveData<Tool>()

    private var _providerLiveData: SensorLiveData? = null

    val measurementUnit = MutableLiveData<MeasureUnit>()

    val sensorData: LiveData<SensorData> = Transformations.switchMap(_tool) { tool ->
        val newLiveData = getProviderLiveData(tool).also {
            _providerLiveData = it
        }
        measurementUnit.value?.dataProcessor?.let { processor ->
            Transformations.map(newLiveData) {
                processor.process(it)
            }
        } ?: _providerLiveData
    }

    val isListening: Boolean
        get() = _providerLiveData?.isListening ?: false

    fun setTool(tool: Tool, unit: MeasureUnit) {
        measurementUnit.value = unit
        _tool.value = tool
    }

    fun resume() {
        _providerLiveData?.resume()
    }

    fun pause() {
        _providerLiveData?.stop()
    }

    private fun getProviderLiveData(tool: Tool): SensorLiveData {
        val context: Context = getApplication()
        val preferenceHelper = tool.helper(getApplication())
        val samplingRate = preferenceHelper.providerSamplingRate
        return ProviderFactory.getLiveData(context, tool.providerType, samplingRate)
    }
}