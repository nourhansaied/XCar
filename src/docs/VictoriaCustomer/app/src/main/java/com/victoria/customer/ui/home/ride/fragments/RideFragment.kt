package com.victoria.customer.ui.home.ride.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.location.Location
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Common.latitudeCurrent
import com.victoria.customer.core.Common.longitudeCurrent
import com.victoria.customer.core.map_route.map.MapNavigator
import com.victoria.customer.core.map_route.map.draw_route.DrawRoute
import com.victoria.customer.data.URLFactory
import com.victoria.customer.data.pojo.PlaceOrder
import com.victoria.customer.data.pojo.ResponseBody
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.ui.authentication.viewmodel.HomeActivityViewModel
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.interfaces.UpdateTimeAndDistance
import com.victoria.customer.util.CircleTransform
import kotlinx.android.synthetic.main.fragment_driver_coming_layout.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*
import java.util.*


class RideFragment : BaseFragment(), OnMapReadyCallback, UpdateTimeAndDistance {

    private lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    private var drawRoute: DrawRoute? = null

    var latitudeDriver: kotlin.Double = 23.0497
    var longitudeDriver: kotlin.Double = 72.5117

    private var mapNavigator: MapNavigator? = null
    lateinit var handlerForDrivLatLn: Handler
    lateinit var runnableDriverLatLn: Runnable

    override fun createLayout(): Int {
        return R.layout.fragment_driver_coming_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun bindData() {

        observeTrackTrip()
        handlerForDrivLatLn = Handler()

        toolBarText.text = getString(R.string.toolbar_title_ride)
        textViewCancel.visibility = View.INVISIBLE
        imageViewCall.setImageResource(R.drawable.calldisable)
        imagViewMessage.setImageResource(R.drawable.msgdisable)
        imageViewMenu.setOnClickListener(this::onViewClick)
        imageViewDriver.setOnClickListener(this::onViewClick)
        setUserData()

    }

    @SuppressLint("SetTextI18n")
    private fun setUserData() {
        latitudeCurrent = VictoriaCustomer.getTripData().dropoffLatitude.toString().toDouble()
        longitudeCurrent = VictoriaCustomer.getTripData().dropoffLongitude.toString().toDouble()

        latitudeDriver = VictoriaCustomer.getTripData().driverData.preLatitude.toString().toDouble()
        longitudeDriver = VictoriaCustomer.getTripData().driverData.preLongitude.toString().toDouble()

        textViewDriverName.text = VictoriaCustomer.getTripData().driverData.name
        textViewCarType.text = VictoriaCustomer.getTripData().driverData.vehicleModel +
                " - " + VictoriaCustomer.getTripData().vehicleData.vehicle +
                " - " + VictoriaCustomer.getTripData().driverData.vehicleNumber
        Picasso.get()
                .load(VictoriaCustomer.getTripData().driverData.profileImageThumb)
                .transform(CircleTransform())
                .into(imageViewDriver)

        textViewRating.text = (String.format("%.1f", VictoriaCustomer.getTripData().driverData.rating))
        textViewTime.text = VictoriaCustomer.getTripData().waitingTime.toString()

        loadMap()

    }

    private fun loadMap() {

        // if(view!=null) {
        mapFragment = SupportMapFragment()
        this.childFragmentManager.beginTransaction().replace(R.id.frameLayoutMapRide, mapFragment).commit()
        mapFragment.getMapAsync(this)

        //}
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        googleMap.isMyLocationEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.setPadding(0, 0, 0, 15)
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        drawRoute = DrawRoute(googleMap, activity!!)
        mapNavigator = MapNavigator(locationManager, googleMap)
        mapNavigator?.setPathUpdateListener(drawRoute!!)

        googleMap.setOnMapLoadedCallback {
            drawPickUpPath()
            runnableDriverLatLn = object : Runnable {
                override fun run() {
                    homeActivityViewModel.trackTripApiCall()
                    handlerForDrivLatLn.postDelayed(this, 5000)
                }
            }

            handlerForDrivLatLn.postDelayed(runnableDriverLatLn, 5000)
        }


    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewMenu -> {

            }

            R.id.imageViewDriver -> {
                //navigator.load(ReceiptFragment::class.java).replace(false)

            }
        }
    }

    private fun drawPickUpPath() {
        try {
            val driverCurrent = LatLng(latitudeDriver, longitudeDriver)
            val latLng = LatLng(latitudeCurrent, longitudeCurrent)
            drawRoute?.drawPath(Arrays.asList(driverCurrent, latLng), R.drawable.car,
                    R.drawable.dropoff_big, object : DrawRoute.OnDrawComplete {
                override fun onComplete() {
                    val startMarker = drawRoute?.startMarker
                    startMarker!!.rotation = drawRoute!!.bearing
                    mapNavigator?.setCurrentMarker(startMarker)
                    mapNavigator?.startNavigation()
                    drawRoute!!.setUpdateTimeAndDistance(this@RideFragment)

                }
            })
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::handlerForDrivLatLn.isInitialized&&::runnableDriverLatLn.isInitialized)
            handlerForDrivLatLn.removeCallbacks(runnableDriverLatLn)
        if (mapNavigator != null) {
            mapNavigator!!.stopNavigation()
        }
    }

    private fun observeTrackTrip() {
        homeActivityViewModel.trackTrip.observe(this, { responseBody ->
            handleTrackTripResponse(responseBody)
        })
    }

    private fun handleTrackTripResponse(responseBody: ResponseBody<PlaceOrder>) {
        navigator.toggleLoader(false)
        if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {

            if (responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) &&
                    responseBody.data?.status.equals(Common.TRIP_TAG_PROCESSING)) {
                val driverLocation = Location("")
                if (responseBody.data!!.driverData.latitude.isNotEmpty()) {
                    driverLocation.latitude = responseBody.data.driverData.latitude.toDouble()
                    driverLocation.longitude = responseBody.data.driverData.longitude.toDouble()
                } else {
                    driverLocation.latitude = responseBody.data.driverData.preLatitude.toDouble()
                    driverLocation.longitude = responseBody.data.driverData.preLongitude.toDouble()
                }
                if (responseBody.data.dropoffLatitude != VictoriaCustomer.getTripData().dropoffLongitude) {
                    VictoriaCustomer.setTripData(responseBody.data)

                    latitudeCurrent = VictoriaCustomer.getTripData().dropoffLatitude.toString().toDouble()
                    longitudeCurrent = VictoriaCustomer.getTripData().dropoffLongitude.toString().toDouble()

                    latitudeDriver = VictoriaCustomer.getTripData().driverData.preLatitude.toString().toDouble()
                    longitudeDriver = VictoriaCustomer.getTripData().driverData.preLongitude.toString().toDouble()

                    drawPickUpPath()
                } else {
                    mapNavigator!!.accept(driverLocation)
                    VictoriaCustomer.setTripData(responseBody.data)
                }

            } else if (responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) &&
                    responseBody.data?.status.equals(Common.TRIP_TAG_PROCESSING) &&
                    responseBody.data?.paymentStatus.equals(Common.TRIP_TAG_PAYMENT_STATUS_UNPAID)) {
                VictoriaCustomer.setTripData(responseBody.data)
                navigator.load(ReceiptFragment::class.java).replace(false)
            } else if (responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) &&
                    responseBody.data?.status.equals(Common.TRIP_TAG_COMPLETED) &&
                    responseBody.data?.paymentStatus.equals(Common.TRIP_TAG_PAYMENT_STATUS_UNPAID)) {
                VictoriaCustomer.setTripData(responseBody.data)
                navigator.load(ReceiptFragment::class.java).replace(false)
            }

        } else if (responseBody.responseCode == 2) {
        }
    }

    override fun onComplete(time: String?, km: String?) {
        textViewTime.text = "$time"
    }
}
