package com.ibashkimi.provider.provider

/**
 * Represents an active object that provides measurements from certain tools.
 * This tools can depend on physical devices/measurements or can be simulated.
 */
interface Provider {
    /**
     * @return true if this provider is supported on the device.
     */
    val isSupported: Boolean

    val isSimulated: Boolean

    /**
     * Checks if the listener is registered.
     *
     * @param listener the listener to check.
     * @return <tt>true</tt> if the listener is already registered.
     */
    fun isRegistered(listener: ProviderListener): Boolean

    /**
     * Registers the listener in order to be updated when value changes.
     *
     * @param listener the listener to register.
     */
    fun register(listener: ProviderListener)

    /**
     * Unregisters the listener: it won't receive any more updated.
     *
     * @param listener the listener to remove.
     */
    fun unregister(listener: ProviderListener)

    fun unregisterAll()
}