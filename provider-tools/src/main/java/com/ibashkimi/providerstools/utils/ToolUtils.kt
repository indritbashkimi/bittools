package com.ibashkimi.providerstools.utils

import android.content.Context
import com.ibashkimi.provider.factories.ProviderFactory
import com.ibashkimi.provider.provider.ProviderType
import com.ibashkimi.shared.Tool

fun Tool.isProviderSupported(context: Context): Boolean {
    return ProviderFactory.createHardwareProvider(context, this.providerType, 3).isSupported
}

val Tool.providerType: Int
    get() = when (this) {
        Tool.ACCELEROMETER -> ProviderType.TYPE_ACCELEROMETER
        Tool.BAROMETER -> ProviderType.TYPE_BAROMETER
        Tool.COMPASS -> ProviderType.TYPE_COMPASS
        Tool.HYGROMETER -> ProviderType.TYPE_HYGROMETER
        Tool.LEVEL -> ProviderType.TYPE_LEVEL
        Tool.LIGHT -> ProviderType.TYPE_LIGHT_METER
        Tool.MAGNETOMETER -> ProviderType.TYPE_MAGNETOMETER
        Tool.THERMOMETER -> ProviderType.TYPE_THERMOMETER
        else -> throw IllegalArgumentException("$name is not a provider tool")
    }