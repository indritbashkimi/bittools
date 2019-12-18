package com.ibashkimi.provider.implementor

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log


abstract class GpsBasedImplementor(context: Context, private var minTime: Int) :
    AbstractSensorImplementor(), LocationListener {

    private var locationManager: LocationManager? = null

    protected var provider: String

    protected var listening: Boolean = false

    override val isSimulated: Boolean = false

    override val isSupported: Boolean = locationManager != null

    init {
        this.locationManager =
            context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        val criteria = Criteria()
        this.provider = locationManager!!.getBestProvider(criteria, false)
        this.listening = false
    }

    @SuppressLint("MissingPermission")
    override fun startListening() {
        Log.d(TAG, "gps backend start listening")
        if (!listening && locationManager != null) {
            Log.d(TAG, "primo if superato")
            locationManager!!.requestLocationUpdates(provider, minTime.toLong(), 1000f, this)
            Log.d(TAG, "requestLocationUpdates done")
            listening = true
        } else {
            Log.d(TAG, "primo if non superato")
        }
    }

    @SuppressLint("MissingPermission")
    override fun stopListening() {
        Log.d(TAG, "stop listening called")
        if (listening && locationManager != null) {
            locationManager!!.removeUpdates(this)
            listening = false
            Log.d(TAG, "updates removed successfully.")
        }
    }

    override abstract fun onLocationChanged(location: Location)

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        Log.d(TAG, "onStatusChanged. ")
    }

    override fun onProviderEnabled(provider: String) {
        Log.d(TAG, "onProviderEnable")
    }

    override fun onProviderDisabled(provider: String) {
        Log.d(TAG, "onProviderDisabled")
    }

    companion object {
        private val TAG = GpsBasedImplementor::class.java.simpleName
    }
}
