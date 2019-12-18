package com.ibashkimi.provider.provider

import android.util.Log
import java.util.*


abstract class AbstractProvider : Provider {

    protected val listeners: HashSet<ProviderListener> by lazy {
        HashSet<ProviderListener>(1)
    }

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

    // @ProviderListener.ProviderEvent
    protected fun notifyEvent(event: Int) {
        /*if (stateListeners != null) {
            for (ProviderStateListener listener : stateListeners)
                listener.onStateChanged(event);
        }*/
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

    open fun startListening() {
        isListening = true
    }

    open fun stopListening() {
        isListening = false
    }

    companion object {
        private val TAG = AbstractProvider::class.java.simpleName
    }
}
