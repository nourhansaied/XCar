package com.victoria.customer.core.map_route.map


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.location.Location
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.SphericalUtil
import com.victoria.customer.util.LocationManager
import io.reactivex.disposables.Disposable


/**
 * Created by hlink21 on 24/3/17.
 */

class MapNavigator(private val locationManager: LocationManager, private val googleMap: GoogleMap) {

    var length = 0.0

    private var disposable: Disposable? = null
    private val latLngTypeEvaluator = TypeEvaluator<LatLng> { v, fromLng, toLatLng -> SphericalUtil.interpolate(fromLng, toLatLng, v.toDouble()) }
    private var lastLocation: Location? = null
    private var lastHead: Float = 0.toFloat()
    private var currentMarker: Marker? = null

    private var startingLatLng: LatLng? = null

    private var isNavigationMpde = true

    private var pathUpdateListener: PathUpdateListener? = null


    fun setNavigationMpde(navigationMpde: Boolean) {
        isNavigationMpde = navigationMpde
    }

    fun setStartingLatLng(startingLatLng: LatLng) {
        this.startingLatLng = startingLatLng
    }

    fun startNavigation() {
        /*if (disposable == null)
            disposable = locationManager.subject.subscribe(this)*/
        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(latLng: LatLng) {

            }

            override fun onFail(status: LocationManager.LocationListener.Status) {

            }
        })
    }

    fun stopNavigation() {
        if (disposable != null)
            disposable!!.dispose()

        disposable = null
    }

    fun setCurrentMarker(currentMarker: Marker) {
        this.currentMarker = currentMarker
        this.currentMarker!!.isFlat = true
        this.currentMarker!!.setAnchor(0.5f, 0.5f)


        val build = CameraPosition.builder()
                .bearing(currentMarker.rotation)
                .target(currentMarker.position)
                .zoom(17f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(build))
        lastHead = googleMap.cameraPosition.bearing

        //        this.currentMarker.setFlat(true);

        //        this.currentMarker.setAnchor(0.5f, 0.5f);

        /* CameraPosition build = CameraPosition.builder()
                .bearing(currentMarker.getRotation())
                .target(currentMarker.getPosition())
                .zoom(17).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(build));
        lastHead = googleMap.getCameraPosition().bearing;*/

        //startingLatLng = currentMarker.getPosition();
    }

    fun addMarker(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)

        // currentMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).position(latLng));
        currentMarker!!.setPosition(latLng)
        currentMarker!!.isFlat = true
        this.currentMarker!!.setAnchor(0.5f, 0.5f)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))

        lastHead = googleMap.cameraPosition.bearing
    }

    @Throws(Exception::class)
    fun accept(location: Location) {
        if (currentMarker == null)
            addMarker(location)

        if (lastLocation == null || lastLocation!!.latitude != location.latitude) {
            animate(location)
            lastLocation = location
        }
    }


    private fun locationToLatLng(location: Location?): LatLng {
        return if (location != null)
            LatLng(location.latitude, location.longitude)
        else
            LatLng(0.0, 0.0)
    }


    private fun animate(location: Location) {

        if (lastLocation == null) {
            return
        }

        val from = locationToLatLng(lastLocation)
        val to = locationToLatLng(location)

        val valueAnimator = ValueAnimator.ofObject(latLngTypeEvaluator, from, to)
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.duration = 3000
        valueAnimator.addUpdateListener { valueAnimator ->
            val newLatLng = valueAnimator.animatedValue as LatLng

            currentMarker!!.setPosition(newLatLng)

            if (isNavigationMpde) {
                val lastCameraPosition = googleMap.cameraPosition

                val cameraPosition = CameraPosition.builder(lastCameraPosition)
                        .target(newLatLng)
                        .build()

                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }

        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                try {
                    if (!isNavigationMpde) {
                        val builder = LatLngBounds.Builder()
                        builder.include(to)
                        builder.include(startingLatLng!!)
                        val bounds = builder.build()
                        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 70)
                        googleMap.animateCamera(cu)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
        valueAnimator.start()

        val bearingTo = SphericalUtil.computeHeading(from, to).toFloat()
        val bearingAnimator = ValueAnimator.ofFloat(lastHead, bearingTo)
        bearingAnimator.interpolator = AccelerateDecelerateInterpolator()
        bearingAnimator.duration = 400
        bearingAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            currentMarker!!.rotation = value
            if (isNavigationMpde) {
                val cameraPosition = CameraPosition.builder(googleMap.cameraPosition)
                        .bearing(value)
                        .build()

                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
        bearingAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                lastHead = bearingTo
            }
        })
        bearingAnimator.start()

        if (pathUpdateListener != null)
            pathUpdateListener!!.updatePath(to)
    }

    fun setPathUpdateListener(pathUpdateListener: PathUpdateListener) {
        this.pathUpdateListener = pathUpdateListener
    }

}