package com.ibashkimi.providerstools.resolver

import android.content.Context
import com.ibashkimi.provider.factories.ProviderFactory
import com.ibashkimi.provider.provider.Provider
import com.ibashkimi.provider.provider.ProviderType
import com.ibashkimi.provider.providerdata.DataProcessor
import com.ibashkimi.shared.Tool

object ProviderResolver {

    fun resolve(
        context: Context,
        tool: Tool,
        samplingRate: Int,
        dataProcessor: DataProcessor?
    ): Provider {
        return ProviderFactory.createHardwareProvider(
            context,
            getProviderType(tool),
            samplingRate,
            dataProcessor
        )
    }

    fun resolveDebug(
        context: Context,
        tool: Tool,
        samplingRate: Int,
        dataProcessor: DataProcessor?
    ): Provider {
        return ProviderFactory.createDebugProvider(
            context,
            getProviderType(tool),
            samplingRate,
            dataProcessor
        )
    }

    private fun getProviderType(tool: Tool): Int {
        return when (tool) {
            Tool.ACCELEROMETER -> ProviderType.TYPE_ACCELEROMETER
            Tool.BAROMETER -> ProviderType.TYPE_BAROMETER
            Tool.COMPASS -> ProviderType.TYPE_COMPASS
            Tool.HYGROMETER -> ProviderType.TYPE_HYGROMETER
            Tool.LEVEL -> ProviderType.TYPE_LEVEL
            Tool.LIGHT -> ProviderType.TYPE_LIGHT_METER
            Tool.MAGNETOMETER -> ProviderType.TYPE_MAGNETOMETER
            Tool.THERMOMETER -> ProviderType.TYPE_THERMOMETER
            else -> throw IllegalArgumentException("${tool.name} is not a provider tool")
        }
    }
}