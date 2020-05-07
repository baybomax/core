package com.libs.core.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Looper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.libs.core.util.Locater.MAX_UPDATE_INTERVAL
import com.libs.core.util.Locater.MIN_UPDATE_INTERVAL

/**
 * The utility of location information
 *
 */
object Locater {

    const val ENABLE_LOCATION = 4

    const val MIN_UPDATE_INTERVAL = 5  * 1000L
    const val MAX_UPDATE_INTERVAL = 10 * 1000L

    var latitude : Double? = null
    var longitude: Double? = null
}


/**
 *
 */
class LocationGetter(val ctx: Context) {

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback   : LocationCallback? = null
    private var locationRequest    : LocationRequest?  = null
    private var settingClient      : SettingsClient?   = null
    private var locationSettingRequest: LocationSettingsRequest?   = null

    /**
     *
     */
    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
        addLocationListener()

        settingClient = LocationServices.getSettingsClient(ctx)

        locationRequest = LocationRequest()
            .apply {
                priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
                fastestInterval = MIN_UPDATE_INTERVAL
                interval = MAX_UPDATE_INTERVAL
            }

        locationSettingRequest =
            LocationSettingsRequest
                .Builder()
                .addLocationRequest(locationRequest!!)
                .setAlwaysShow(true)
                .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                val location = locationResult?.lastLocation

                Locater.run {
                    latitude   = location?.latitude
                    longitude  = location?.longitude
                }

                callback?.invoke()
                callback = null
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun addLocationListener()=
        try {
            fusedLocationClient?.lastLocation?.addOnCompleteListener {
                if (null != it.result) {
                    val location = it.result

                    Locater.run {
                        latitude   = location?.latitude
                        longitude  = location?.longitude
                    }
                } else {
                    requestLocationUpdates()
                }

                callback?.invoke()
                callback = null
            }
        } catch (e: Exception) {
            // missing permission
        }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() =
        try {
            settingClient
                ?.checkLocationSettings(locationSettingRequest)
                ?.addOnSuccessListener {
                    // request location
                    fusedLocationClient?.requestLocationUpdates(
                        locationRequest ,
                        locationCallback,
                        Looper.myLooper()
                    )
                }
                ?.addOnFailureListener {
                    val act = ctx as? Activity? ?: return@addOnFailureListener
                    if ((it as? ApiException?)?.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        (it as? ResolvableApiException?)?.startResolutionForResult(act,
                            Locater.ENABLE_LOCATION
                        )
                    }
                }
        } catch (e: Exception) {
            // missing permission
        }

    fun removeLocationUpdates() =
        fusedLocationClient?.removeLocationUpdates(locationCallback)

    private var callback: (() -> Unit)? = null

    fun requestLocationUpdatesCallBack(callback: () -> Unit) {
        this.callback = callback
        requestLocationUpdates()
    }
}