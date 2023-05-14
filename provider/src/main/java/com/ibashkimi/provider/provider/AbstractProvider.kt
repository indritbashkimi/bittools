package com.ibashkimi.provider.provider

import android.util.Log
import androidx.annotation.CallSuper
import com.ibashkimi.provider.providerdata.SensorData

abstract class AbstractProvider : Provider {

    private val listeners: HashSet<ProviderListener> = HashSet(1)

    protected var isListening: Boolean = false

    abstract override val isSupported: Boolean

    override fun isRegistered(listener: ProviderListener): Boolean = listeners.contains(listener)

    override fun register(listener: ProviderListener) {
        Log.d(TAG, "registering listener: $listener")
        if (listeners.add(listener)) {
            if (!isListening) {
                startListening()
            }
            Log.d(TAG, listener.javaClass.simpleName + " registered. size = " + listeners.size)
        } else
            Log.d(TAG, listener.javaClass.simpleName + " NOT registered. size = " + listeners.size)
    }

    protected fun notifyEvent(event: Int) {

    }

    override fun unregister(listener: ProviderListener) {
        listeners.remove(listener)
        if (isListening && listeners.size == 0) {
            stopListening()
        }
    }

    override fun unregisterAll() {
        listeners.clear()
        stopListening()
    }

    @CallSuper
    open fun startListening() {
        isListening = true
    }

    @CallSuper
    open fun stopListening() {
        isListening = false
    }

    protected fun onDataChanged(data: SensorData) {
        listeners.forEach { it.onDataChanged(data) }
    }

    companion object {
        private val TAG = AbstractProvider::class.java.simpleName
    }
}
