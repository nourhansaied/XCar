package com.victoria.customer.ui.home.ride.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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
import com.victoria.customer.ui.home.fragments.HomeStartFragment
import com.victoria.customer.ui.home.ride.dialog.DriverArrivedDialog
import com.victoria.customer.ui.interfaces.ISelectCountry
import com.victoria.customer.ui.interfaces.ItemClickEventListener
import com.victoria.customer.ui.interfaces.UpdateTimeAndDistance
import com.victoria.customer.util.CircleTransform
import kotlinx.android.synthetic.main.fragment_driver_coming_layout.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*
import java.util.*


class DriverComingFragment : BaseFragment(), OnMapReadyCallback, UpdateTimeAndDistance {

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    public lateinit var driverArrivedDialog: DriverArrivedDialog
    private var drawRoute: DrawRoute? = null
    var latitudeDriver: kotlin.Double = 0.0
    var longitudeDriver: kotlin.Double = 0.0

    private var mapNavigator: MapNavigator? = null
    lateinit var handlerForDrivLatLn: Handler
    lateinit var runnableDriverLatLn: Runnable
    private var isDialogOpen: Boolean = false

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

        textViewCancel.visibility = View.VISIBLE
        textViewCancel.setOnClickListener(this::onViewClick)
        imageViewMenu.setOnClickListener(this::onViewClick)
        imageViewDriver.setOnClickListener(this::onViewClick)
        imagViewMessage.setOnClickListener(this::onViewClick)
        imageViewCall.setOnClickListener(this::onViewClick)

        handlerForDrivLatLn = Handler()


        setUserData()

    }

    @SuppressLint("SetTextI18n")
    private fun setUserData() {
        loadMap()

        latitudeCurrent = VictoriaCustomer.getTripData().pickupLatitude.toString().toDouble()
        longitudeCurrent = VictoriaCustomer.getTripData().pickupLongitude.toString().toDouble()

        latitudeDriver = VictoriaCustomer.getTripData().driverData.latitude.toString().toDouble()
        longitudeDriver = VictoriaCustomer.getTripData().driverData.longitude.toString().toDouble()

        textViewDriverName.text = VictoriaCustomer.getTripData().driverData.name
        textViewCarType.text = VictoriaCustomer.getTripData().driverData.vehicleModel +
                " - " + VictoriaCustomer.getTripData().vehicleData.vehicle +
                " - " + VictoriaCustomer.getTripData().driverData.vehicleNumber
        Picasso.get()
                .load(VictoriaCustomer.getTripData().driverData.profileImageThumb)
                .transform(CircleTransform())
                .into(imageViewDriver)

        textViewRating.text = (String.format("%.2f", VictoriaCustomer.getTripData().driverData.rating))
        textViewTime.text = VictoriaCustomer.getTripData().waitingTime.toString()
    }

    private fun loadMap() {

        // if(view!=null) {
        mapFragment = SupportMapFragment()
        this.childFragmentManager.beginTransaction().replace(R.id.frameLayoutMapRide, mapFragment).commit()
        mapFragment.getMapAsync(this)

        // }
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


        googleMap.setOnMapLoadedCallback(GoogleMap.OnMapLoadedCallback {
            drawPickUpPath()
        })


        runnableDriverLatLn = object : Runnable {
            override fun run() {
                homeActivityViewModel.trackTripApiCall()
                handlerForDrivLatLn.postDelayed(this, 5000)
            }
        }

        handlerForDrivLatLn.postDelayed(runnableDriverLatLn, 5000)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.textViewCancel -> {
                navigator.load(CancelRideFragment::class.java).replace(true)
            }

            R.id.imageViewDriver -> {
                /*if (fragmentManager != null) {
                    driverArrivedDialog = DriverArrivedDialog()
                    driverArrivedDialog.setCallback(ISelectCountry { countryCode, country, id -> navigator.load(RideFragment::class.java).replace(false) })
                    driverArrivedDialog.show(fragmentManager, "")
                }*/
            }
            R.id.imagViewMessage -> {
                navigator.load(ChatMessageListingFragment::class.java).replace(true)
                /*if (fragmentManager != null) {
                    driverArrivedDialog = DriverArrivedDialog()
                    driverArrivedDialog.setCallback(ISelectCountry { countryCode, country, id -> navigator.load(RideFragment::class.java).replace(false) })
                    driverArrivedDialog.show(fragmentManager, "")
                }*/
            }
            R.id.imageViewCall -> {
                if (isPermissionGranted()) {
                    callAction()
                }
            }
        }
    }

    private fun callAction() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + VictoriaCustomer.getTripData().driverData.countryCode
                + VictoriaCustomer.getTripData().driverData.phone)
        startActivity(callIntent)
    }

    private fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this!!.activity!!, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                return true
            } else {

                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 1)
                return false
            }
        } else { //permission is automatically granted on sdLog.v("TAG", "Permission is granted")
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (isPermissionGranted()) callAction()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun drawPickUpPath() {
        try {
            val driverCurrent = LatLng(latitudeDriver, longitudeDriver)
            val latLng = LatLng(latitudeCurrent, longitudeCurrent)
            drawRoute?.drawPath(Arrays.asList(driverCurrent, latLng), R.drawable.car, R.drawable.dropoff_big,
                    object : DrawRoute.OnDrawComplete {
                        override fun onComplete() {
                            val startMarker = drawRoute?.startMarker
                            startMarker!!.rotation = drawRoute!!.bearing
                            mapNavigator?.setCurrentMarker(startMarker)
                            mapNavigator?.startNavigation()
                            drawRoute!!.setUpdateTimeAndDistance(this@DriverComingFragment)

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
        if (::handlerForDrivLatLn.isInitialized && ::runnableDriverLatLn.isInitialized)
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

    @SuppressLint("SetTextI18n")
    override fun onComplete(time: String?, km: String?) {
        Handler().postDelayed({
            textViewTime?.text = "$time"
        }, 300)
        if (arguments != null && arguments!!.containsKey(Common.IS_OPEN_CHAT)) {
            if (arguments!!.getBoolean(Common.IS_OPEN_CHAT)) {
                arguments!!.clear()
                navigator.load(ChatMessageListingFragment::class.java).replace(true)
            }
        }
    }

    private fun handleTrackTripResponse(responseBody: ResponseBody<PlaceOrder>) {
        navigator.toggleLoader(false)
        if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {
            VictoriaCustomer.setTripData(responseBody.data)
            if (responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) &&
                    responseBody.data?.status.equals(Common.TRIP_TAG_ASSIGNED)) {
                val driverLocation = Location("")
                if (responseBody.data!!.driverData.latitude.isNotEmpty()) {
                    driverLocation.latitude = responseBody.data!!.driverData.latitude.toDouble()
                    driverLocation.longitude = responseBody.data!!.driverData.longitude.toDouble()
                } else {
                    driverLocation.latitude = responseBody.data!!.driverData.preLatitude.toDouble()
                    driverLocation.longitude = responseBody.data!!.driverData.preLongitude.toDouble()
                }
                mapNavigator!!.accept(driverLocation)
            } else if (responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) &&
                    responseBody.data?.status.equals(Common.TRIP_TAG_ARRIVED)) {
                if (fragmentManager != null) {
                    if (!isDialogOpen) {
                        isDialogOpen = true
                        driverArrivedDialog = DriverArrivedDialog()
                        driverArrivedDialog.setCallback(ISelectCountry { countryCode, country, id -> navigator.load(RideFragment::class.java).replace(false) })
                        driverArrivedDialog.setCallback(ItemClickEventListener {
                            if (::driverArrivedDialog.isInitialized) {
                                isDialogOpen = false
                                driverArrivedDialog.dismissAllowingStateLoss()
                            }
                            navigator.load(ChatMessageListingFragment::class.java).replace(true)
                        })
                        driverArrivedDialog.show(fragmentManager, "")
                    }
                }
            } else if (responseBody.data?.tripType.equals(Common.TRIP_TAG_TRIP_NOW) &&
                    responseBody.data?.status.equals(Common.TRIP_TAG_PROCESSING)) {
                if (::driverArrivedDialog.isInitialized)
                    driverArrivedDialog.dismiss()
                navigator.load(RideFragment::class.java).replace(true)
            }
        } else if (responseBody.responseCode == 2) {
        }
    }

    public fun closeDialogAndReturn() {

        if (::driverArrivedDialog.isInitialized && driverArrivedDialog != null) {
            driverArrivedDialog.dismissAllowingStateLoss()
            navigator.load(HomeStartFragment::class.java).replace(false)
        } else {
            navigator.load(HomeStartFragment::class.java).replace(false)
        }
    }
}
