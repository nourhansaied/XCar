package com.victoria.customer.core.map_route.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.victoria.customer.util.PermissionUtil
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Hyperlink infosystem team  on 27/9/16.
 */

@Singleton
class DriverLocationManager @Inject
constructor() : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {



    private var mGoogleApiClient: GoogleApiClient? = null

    private var activity: AppCompatActivity? = null

    private var appContext: Context? = null

    private var mLocationRequest: LocationRequest? = null
    var lastLocation: Location? = null
        private set
    private val isFreshLocation = true

    private var locationListener: LocationListener? = null

    var subject: Subject<Location> = PublishSubject.create()

    fun setAppContext(appContext: Context) {
        this.appContext = appContext
    }

    fun setActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

    fun triggerLocation(locationListener: LocationListener) {
        this.locationListener = locationListener
        init()
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestPermission(): Boolean {

        if (activity != null) {
            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity!!.requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CHECK_PERMISSION)
            } else if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity!!.requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CHECK_PERMISSION)
            } else if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity!!.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CHECK_PERMISSION)
            } else {
                return true
            }
            return false
        } else
            return false
    }

    private fun init() {


        if (checkPlayServices()) {

            if (Build.VERSION.SDK_INT >= 23 && requestPermission()) {
                connectToClient()
            } else {
                connectToClient()
            }

        } else {

            if (locationListener != null)
                locationListener!!.onFail(LocationListener.Status.NO_PLAY_SERVICE)
        }
    }

    private fun connectToClient() {
        buildGoogleApiClient()
        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }


    /**
     * Creating google api client object
     */
    @Synchronized
    fun buildGoogleApiClient() {

        mGoogleApiClient = GoogleApiClient.Builder(if (activity == null) appContext!! else activity!!)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApiIfAvailable(LocationServices.API).build()

    }

    /**
     * Creating location request object
     */
    protected fun createLocationRequest() {

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL.toLong()
        mLocationRequest!!.fastestInterval = FATEST_INTERVAL.toLong()
        mLocationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mLocationRequest!!.smallestDisplacement = DISPLACEMENT.toFloat() // 10 meters

    }


    /**
     * Starting the location updates
     */

    fun startLocationUpdates() {

        if (!checkPermission()) {
            if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected)
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }

    }

    /**
     * Stopping location updates
     */
    protected fun stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected)
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this)
    }


    /**
     * Method to display the location on UI
     */

    fun getLocation() {


        if (isFreshLocation) {

            mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

            startLocationUpdates()


        } else {

            if (!checkPermission()) {

                lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

                if (lastLocation != null) {
                    val latitude = lastLocation!!.latitude
                    val longitude = lastLocation!!.longitude
                    Log.d("LAST Location ", (+latitude).toString() + " : " + longitude)
                    if (locationListener != null)
                        locationListener!!.onLocationAvailable(LatLng(latitude, longitude))
                } else {
                    mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    startLocationUpdates()
                }
            }

        }


    }

    private fun checkPermission(): Boolean {
        return if (activity != null && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            true
        } else false
    }


    /**
     * Method to verify google play services on the device
     */
    private fun checkPlayServices(): Boolean {

        if (activity != null) {
            val googleAPI = GoogleApiAvailability.getInstance()
            val result = googleAPI.isGooglePlayServicesAvailable(activity)
            if (result != ConnectionResult.SUCCESS) {
                if (googleAPI.isUserResolvableError(result)) {
                    googleAPI.getErrorDialog(activity, result,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show()
                }

                return false
            }
            return true
        } else
            return false
    }

    fun checkLocationEnable() {

        val locationSettingsRequestBuilder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest!!)
        locationSettingsRequestBuilder.setAlwaysShow(true)
        val result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build())
        result.setResultCallback(this)

    }

    override fun onConnected(bundle: Bundle?) {
        Log.d("Location Manager", "onConnected")
        createLocationRequest()
        checkLocationEnable()
    }

    override fun onConnectionSuspended(i: Int) {
        Log.d("Location Manager", "onConnectionSuspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

        Log.d("Location Manager", "onConnectionFailed ")
    }

    override fun onLocationChanged(location: Location) {

        // stopLocationUpdates();
        // mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        lastLocation = location
        subject.onNext(location)
        Log.d("Location Manager", "onLocationChanged " + location.latitude + " : " + location.longitude)
        if (locationListener != null)
            locationListener!!.onLocationAvailable(LatLng(location.latitude, location.longitude))

    }

    fun stop() {
        locationListener = null
        stopLocationUpdates()
        if (mGoogleApiClient != null)
            mGoogleApiClient!!.disconnect()
    }


    override fun onResult(locationSettingsResult: LocationSettingsResult) {

        val status = locationSettingsResult.status
        Log.d("Location Manager", "Location Setting " + status.hasResolution())
        if (status.hasResolution()) {
            Toast.makeText(activity, "Please Enable the Location service ", Toast.LENGTH_SHORT).show()
            try {
                status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
            } catch (e: IntentSender.SendIntentException) {
                e.printStackTrace()
            }

        } else {
            getLocation()
        }


    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == Activity.RESULT_OK) {
                getLocation()
            } else {

                if (locationListener != null)
                    locationListener!!.onFail(LocationListener.Status.DENIED_LOCATION_SETTING)
            }


        }
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CHECK_PERMISSION) {

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                connectToClient()
            } else {

                if (locationListener != null)
                    locationListener!!.onFail(LocationListener.Status.PERMISSION_DENIED)

            }

        }
    }

    interface LocationListener {
        fun onLocationAvailable(latLng: LatLng)

        fun onFail(status: Status)

        enum class Status {
            PERMISSION_DENIED, NO_PLAY_SERVICE, DENIED_LOCATION_SETTING
        }
    }

    companion object {


        private val PLAY_SERVICES_RESOLUTION_REQUEST = 1000
        private val REQUEST_CHECK_SETTINGS = 111
        private val REQUEST_CHECK_PERMISSION = 222
        // Location updates intervals in sec
        private val UPDATE_INTERVAL = 3000 // 3 sec
        private val FATEST_INTERVAL = 3000 // 3 sec
        private val DISPLACEMENT = 5 // 1 meters
    }


}