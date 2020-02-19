package com.victoria.customer.core.map_route.map.draw_route


import android.app.Activity
import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.android.PolyUtil
import com.google.maps.android.SphericalUtil
import com.google.maps.model.DirectionsResult
import com.victoria.customer.R
import com.victoria.customer.core.map_route.map.PathUpdateListener
import com.victoria.customer.ui.interfaces.UpdateTimeAndDistance
import java.util.*


class DrawRoute(private val googleMap: GoogleMap?, private val context: Activity?) : PathUpdateListener {
    private val key: String? = context?.resources?.getString(R.string.browser_key)
    internal var imageViewBlue: ImageView? = null
    internal var imageViewOrang: ImageView? = null
    internal var customMarkerView: View? = null


    var currentPath: List<LatLng>? = null
        private set
    var startMarker: Marker? = null
        private set
    var endMarker: Marker? = null
        private set
    private var points: ArrayList<com.google.maps.model.LatLng>? = null
    private val builder = LatLngBounds.Builder()
    private var currentPolyline: Polyline? = null

    private var updateTimeAndDistance: UpdateTimeAndDistance? = null

    val bearing: Float
        get() {
            if (points != null && points!!.size > 1) {
                val from = points!![0]
                val to = points!![1]
                return SphericalUtil.computeHeading(LatLng(from.lat, from.lng), LatLng(to.lat, to.lng)).toFloat()
            }
            return 0f
        }


    private fun addMarker(location: LatLng?, @DrawableRes drawable: Int): Marker? {
        if (googleMap != null && location != null && drawable != 0) {
            //val bearingTo = SphericalUtil.computeHeading(LatLng(location.latitude, location.longitude),).toFloat()

            val markerOptions = MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromResource(drawable))
            return googleMap.addMarker(markerOptions)
        }
        return null
    }

    fun setUpdateTimeAndDistance(updateTimeAndDistance: UpdateTimeAndDistance) {
        this.updateTimeAndDistance = updateTimeAndDistance
    }

    private fun addBitmapMarker(location: LatLng?, bitmap: Bitmap?): Marker? {
        if (googleMap != null && location != null && bitmap != null) {
            val markerOptions = MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
            return googleMap.addMarker(markerOptions)
        }
        return null
    }


    fun drawPath(latLngs: List<LatLng>, onDrawComplete: OnDrawComplete) {
        drawPath(latLngs, 0, 0, onDrawComplete)
    }


    fun drawPath(latLngs: List<LatLng>?, start: Bitmap, last: Bitmap, onDrawComplete: OnDrawComplete?) {


        this.currentPath = latLngs

        if (latLngs == null || latLngs.size < 2)
            return

        googleMap!!.clear()

        try {
            //   Picasso.with(context).load(R.drawable.user_profile).fetch();
            val context = GeoApiContext().setApiKey(key)
            val directionsApiRequest = DirectionsApi.newRequest(context)

            val latLng1 = com.google.maps.model.LatLng(latLngs[0].latitude, latLngs[0].longitude)
            directionsApiRequest.origin(latLng1)
            startMarker = addBitmapMarker(latLngs[0], start)
            //startMarker = addCustomMarker(latLngs.get(0), 1);


            val latLng2 = com.google.maps.model.LatLng(latLngs[latLngs.size - 1].latitude, latLngs[latLngs.size - 1].longitude)
            directionsApiRequest.destination(latLng2)
            endMarker = addBitmapMarker(latLngs[latLngs.size - 1], last)
            //endMarker = addCustomMarker(latLngs.get(latLngs.size() - 1), 2);
            //  builder = new LatLngBounds.Builder();

            builder.include(latLngs[0]).include(latLngs[latLngs.size - 1])


            /*  List<com.google.maps.model.LatLng> waypont = new ArrayList<>();
            if (latLngs.size() > 2) {

                for (int i = 1; i <= latLngs.size() - 2; i++) {
                    com.google.maps.model.LatLng latLng = new com.google.maps.model.LatLng(latLngs.get(i).latitude, latLngs.get(i).longitude);
                    waypont.add(latLng);
                    builder.include(latLngs.get(i));
                    addNumberMarker(latLngs.get(i), i);
                }
            }

            if (!waypont.isEmpty())
                directionsApiRequest.waypoints(waypont.toArray(new com.google.maps.model.LatLng[waypont.size()]));*/

            directionsApiRequest.setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult) {

                    this@DrawRoute.context?.runOnUiThread {
                        routeDraw(result)
                        onDrawComplete?.onComplete()
                    }

                }


                override fun onFailure(e: Throwable) {

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun drawPath(latLngs: List<LatLng>?, @DrawableRes start: Int, @DrawableRes
    last: Int, onDrawComplete: OnDrawComplete?) {


        this.currentPath = latLngs

        if (latLngs == null || latLngs.size < 2)
            return

        googleMap!!.clear()

        try {
            //   Picasso.with(context).load(R.drawable.user_profile).fetch();
            val context = GeoApiContext().setApiKey(key)
            val directionsApiRequest = DirectionsApi.newRequest(context)

            val latLng1 = com.google.maps.model.LatLng(latLngs[0].latitude, latLngs[0].longitude)
            directionsApiRequest.origin(latLng1)
            startMarker = addMarker(latLngs[0], start)

            //startMarker = addCustomMarker(latLngs.get(0), 1);


            val latLng2 = com.google.maps.model.LatLng(latLngs[latLngs.size - 1].latitude, latLngs[latLngs.size - 1].longitude)
            directionsApiRequest.destination(latLng2)
            endMarker = addMarker(latLngs[latLngs.size - 1], last)
            //endMarker = addCustomMarker(latLngs.get(latLngs.size() - 1), 2);
            //  builder = new LatLngBounds.Builder();

            builder.include(latLngs[0]).include(latLngs[latLngs.size - 1])


            /*  List<com.google.maps.model.LatLng> waypont = new ArrayList<>();
            if (latLngs.size() > 2) {

                for (int i = 1; i <= latLngs.size() - 2; i++) {
                    com.google.maps.model.LatLng latLng = new com.google.maps.model.LatLng(latLngs.get(i).latitude, latLngs.get(i).longitude);
                    waypont.add(latLng);
                    builder.include(latLngs.get(i));
                    addNumberMarker(latLngs.get(i), i);
                }
            }

            if (!waypont.isEmpty())
                directionsApiRequest.waypoints(waypont.toArray(new com.google.maps.model.LatLng[waypont.size()]));*/

            directionsApiRequest.setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult) {

                    this@DrawRoute.context?.runOnUiThread {
                        routeDraw(result)
                        onDrawComplete?.onComplete()
                        if (updateTimeAndDistance != null) {
                            if (result.routes.isNotEmpty()) {
                                updateTimeAndDistance!!.onComplete(result.routes[0].legs[0].duration.toString(),
                                        result.routes[0].legs[0].distance.toString())
                            }else{
                                updateTimeAndDistance!!.onComplete("0 min",
                                        "0 km")
                            }
                        }

                    }
                }

                override fun onFailure(e: Throwable) {

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun drawNewPath(newStart: LatLng, onDrawComplete: OnDrawComplete?) {

        // must have current path
        if (currentPolyline == null)
            return

        val context = GeoApiContext().setApiKey(key)
        val directionsApiRequest = DirectionsApi.newRequest(context)

        val start = com.google.maps.model.LatLng(newStart.latitude, newStart.longitude)
        directionsApiRequest.origin(start)

        val endLng = currentPath!![currentPath!!.size - 1]
        val end = com.google.maps.model.LatLng(endLng.latitude, endLng.longitude)
        directionsApiRequest.destination(end)

        //  builder = new LatLngBounds.Builder();
        builder.include(newStart).include(endLng)

        //   LatLngBounds bounds = builder.build();


        /* List<com.google.maps.model.LatLng> waypont = new ArrayList<>();
        if (currentPath.size() > 2) {

            for (int i = 1; i <= currentPath.size() - 2; i++) {
                com.google.maps.model.LatLng latLng = new com.google.maps.model.LatLng(currentPath.get(i).latitude, currentPath.get(i).longitude);
                waypont.add(latLng);
                builder.include(currentPath.get(i));
                addNumberMarker(currentPath.get(i), i);
            }
        }

        if (!waypont.isEmpty())
            directionsApiRequest.waypoints(waypont.toArray(new com.google.maps.model.LatLng[waypont.size()]));*/

        directionsApiRequest.setCallback(object : PendingResult.Callback<DirectionsResult> {
            override fun onResult(result: DirectionsResult) {

                this@DrawRoute.context?.runOnUiThread {
                    routeDraw(result)
                    onDrawComplete?.onComplete()

                    if (updateTimeAndDistance != null) {
                        updateTimeAndDistance!!.onComplete(result.routes[0].legs[0].duration.toString(),
                                result.routes[0].legs[0].distance.toString())
                    }
                }

            }


            override fun onFailure(e: Throwable) {

            }
        })

    }


    private fun routeDraw(result: DirectionsResult) {

        points = ArrayList()

        if (result.routes != null && result.routes.size > 0) {

            for (i in result.routes[0].legs.indices) {
                for (j in result.routes[0].legs[i].steps.indices) {
                    points!!.addAll(result.routes[0].legs[i].steps[j].polyline.decodePath())
                }
            }


            if (currentPolyline != null)
                currentPolyline?.remove()

            val currentPolylineOptions = PolylineOptions()
            // polyline.remove();
            currentPolylineOptions.color(ContextCompat.getColor(context!!, R.color.colorAccent))
            currentPolylineOptions.width(10f)

            currentPolylineOptions.addAll(convert(points!!))
            currentPolyline = googleMap!!.addPolyline(currentPolylineOptions)
            val bounds = builder.build()
            // bounds.including()
            val padding = 30
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            googleMap.animateCamera(cu)
        }

    }


    private fun convert(latLngs: ArrayList<com.google.maps.model.LatLng>): ArrayList<LatLng> {
        val list = ArrayList<LatLng>()
        for (latLng in latLngs) {
            list.add(LatLng(latLng.lat, latLng.lng))
        }
        return list
    }


    fun clearCurrentPath() {
        if (currentPolyline != null) {
            currentPolyline!!.remove()
            currentPolyline!!.points.clear()
            currentPolyline = null
        }
    }


    override fun updatePath(location: LatLng) {

        if (currentPolyline != null) {
            val points = currentPolyline!!.points
            if (points != null && !points.isEmpty()) {
                if (!PolyUtil.isLocationOnPath(location, points, true, 100.0)) {
                    Log.d("Is On Path", " " + false)
                    drawNewPath(location, null)
                } else
                    Log.d("Is On Path", " " + true)
            }
        }

    }


    interface OnDrawComplete {

        fun onComplete()
    }
}
