package com.ibashkimi.providerstools

import android.content.Context
import com.ibashkimi.providerstools.resolver.ProviderResolver
import com.ibashkimi.shared.Tool

fun Tool.isProviderSupported(context: Context): Boolean {
    return ProviderResolver.resolve(context, this, 3, null).isSupported
}