package com.ibashkimi.shared.ext

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull

fun SharedPreferences.getStringFlow(key: String, defValue: String? = null): Flow<String?> =
    getFlow(key) { getString(it, defValue) }

fun SharedPreferences.getIntFlow(key: String, defValue: Int = -1): Flow<Int> =
    getFlow(key) { getInt(it, defValue) }.filterNotNull()

fun <T> SharedPreferences.getFlow(
    key: String,
    getter: (String) -> T?
): Flow<T?> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, k ->
        if (k == key) {
            trySend(getter(key))
        }
    }
    registerOnSharedPreferenceChangeListener(listener)
    trySend(getter(key))
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}
