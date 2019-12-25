package com.ibashkimi.provider.provider

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log


abstract class GpsBasedProvider(context: Context, private var minTime: Int) :
    AbstractProvider(), LocationListener {

    private var locationManager: LocationManager? = null

    protected var provider: String

    override val isSimulated: Boolean = false

    override val isSupported: Boolean = locationManager != null

    init {
        this.locationManager =
            context.applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        val criteria = Criteria()
        this.provider = locationManager!!.getBestProvider(criteria, false)
    }

    @SuppressLint("MissingPermission")
    override fun startListening() {
        super.startListening()
        Log.d(TAG, "gps backend start listening")
        if (!isListening && locationManager != null) {
            locationManager!!.requestLocationUpdates(provider, minTime.toLong(), 1000f, this)
            isListening = true
        }
    }

    @SuppressLint("MissingPermission")
    override fun stopListening() {
        super.stopListening()
        Log.d(TAG, "stop listening called")
        if (isListening && locationManager != null) {
            locationManager!!.removeUpdates(this)
            isListening = false
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
        private val TAG = GpsBasedProvider::class.java.simpleName
    }
}
