package com.victoria.customer.ui.home.fragments

import android.arch.lifecycle.ViewModelProviders
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.transition.TransitionManager
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Common.latitudeCurrent
import com.victoria.customer.core.Common.longitudeCurrent
import com.victoria.customer.data.URLFactory
import com.victoria.customer.data.pojo.ResponseBody
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.*
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.dialog.ServiceBottomSheetFragment
import com.victoria.customer.ui.home.viewmodel.HomeViewModel
import com.victoria.customer.util.LocationManager
import kotlinx.android.synthetic.main.fragment_home_start_layout.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*
import java.util.*
import kotlin.collections.ArrayList


class HomeStartFragment : BaseFragment(), OnMapReadyCallback {


    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    lateinit var icon: ImageView
    lateinit var list: ArrayList<Service>

    var latitude = 0.0
    var longitude = 0.0

    private var isMenuOpen: Boolean = false
    var driverList: List<NearByDriver>? = null

    override fun createLayout(): Int {
        return R.layout.fragment_home_start_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val homeViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]
    }

    override fun bindData() {
        //homeViewModel = ViewModelProviders.of(this, viewModelFactory)[HomeViewModel::class.java]
        toolBarText.text = ""
        driverList = ArrayList()
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        if (arguments != null && arguments!!.containsKey(Common.SHOW_MESSGE_FOR_NO_DRIVER_AVAILABLE)) {
            showMessage(arguments!!.getString(Common.MESSAGE))
        }

        textViewDropoff.setOnClickListener { onViewClick(it) }
        imageViewCurrentLocation.setOnClickListener { onViewClick(it) }
        imageViewMapType.setOnClickListener { onViewClick(it) }
        imageViewMenu.setOnClickListener { onViewClick(it) }
        imagViewCircularMenu.setOnClickListener { onViewClick(it) }
        imageService2.setOnClickListener { onViewClick(it) }
        imageService3.setOnClickListener { onViewClick(it) }
        imageService4.setOnClickListener { onViewClick(it) }
        textViewService2.setOnClickListener { onViewClick(it) }
        textViewServiceName3.setOnClickListener { onViewClick(it) }
        textViewServiceName4.setOnClickListener { onViewClick(it) }

        loadMap()
        //createMenu()

        if (Locale.getDefault().language == "ar") {
            textViewServiceName3.rotation = -48f
        } else {
            textViewServiceName3.rotation = 35f
        }
    }

    private fun loadMap() {
        mapFragment = SupportMapFragment()
        this.childFragmentManager.beginTransaction().replace(R.id.frameLayoutMap, mapFragment).commit()
        mapFragment.getMapAsync(this)

    }


    private fun addMarker() {

        val builder = LatLngBounds.Builder()

        val markerOptions = MarkerOptions()
                .position(LatLng(latitudeCurrent, longitudeCurrent))
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.navigate))
        googleMap.addMarker(markerOptions)

        builder.include(LatLng(latitudeCurrent, longitudeCurrent))

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        MapsInitializer.initialize(context)
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(latLng: LatLng) {
                if (isAdded) {
                    observeNearByDriverListing()
                    observeVehicleListing()
                    navigator.toggleLoader(false)

                    latitude = latLng.latitude
                    longitude = latLng.longitude

                    latitudeCurrent = latLng.latitude
                    longitudeCurrent = latLng.longitude

                    locationManager.stop()

                    addMarker()
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                    if (view != null) {
                        navigator.toggleLoader(false)
                        navigator.toggleLoader(true)
                        homeViewModel?.nearByDriverListing(getNearByDriverData())
                    }
                }
            }

            override fun onFail(status: LocationManager.LocationListener.Status) {
                observeNearByDriverListing()
                observeVehicleListing()
            }
        })

        this.googleMap.setOnMapLoadedCallback {

        }

        googleMap.setOnMapClickListener { arg0 ->
            circularMenuOpen(true)
        }


        //addDrivers(responseBody.data)
    }

    private fun addDrivers(data: List<NearByDriver>?) {

        if (view != null) {

            driverList = data!!

            val builder = LatLngBounds.Builder()

            for (item in data.indices) {

                if (data[item].latitude != "" && data[item].longitude != "") {
                    val latLng = LatLng(data[item].latitude.toDouble(), data[item].longitude.toDouble())
                    var bitmapLoad: Bitmap? = null

                    Glide.with(this)
                            .asBitmap()
                            .load(R.drawable.car)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    bitmapLoad = resource
                                    val markerOptions = MarkerOptions()
                                            .position(latLng)
                                            .flat(true)
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmapLoad))

                                    googleMap.addMarker(markerOptions)

                                    builder.include(latLng)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {

                                }
                            })

                    //for bounding camera for multiple markers
                    val b = LatLngBounds.Builder()
                    b.include(LatLng(data[item].latitude.toDouble(),
                            data[item].longitude.toDouble()))
                    b.include(LatLng(latitudeCurrent, longitudeCurrent))
                    val bounds = b.build()
                    val display = activity!!.windowManager.defaultDisplay
                    val size = Point()
                    display.getSize(size)
                    val width = size.x
                    val height = size.y

                    val padding = (Math.min(width, height) * 0.1).toInt()
                    val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                    googleMap.animateCamera(cu)

                }
                /*googleMap.setOnMapLoadedCallback {
                    val bounds = builder.build()
                    builder.include(LatLng(data[item].latitude.toDouble(),
                            data[item].longitude.toDouble()))
                    val cu = CameraUpdateFactory.newLatLngBounds(bounds, resources
                            .getDimension(R.dimen.dp_60).toInt())
                    googleMap.animateCamera(cu)
                }*/
            }


        }
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewMenu -> {
                circularMenuOpen(true)

                openDrawer()
            }

            R.id.textViewDropoff -> {

                if (vehicleData != null) {
                    val bundle = Bundle()
                    VictoriaCustomer.getRideData().isCarSelected = true
                    VictoriaCustomer.getRideData().carType = 4
                    VictoriaCustomer.getRideData().vehicle_id = vehicleData?.airport?.get(0)?.id.toString()
                    VictoriaCustomer.getRideData().ride_type = "Normal"
                    VictoriaCustomer.getRideData().vehicle_name = VictoriaCustomer.getRideData().ride_type + " - " + vehicleData?.airport?.get(0)?.vehicle
                    bundle.putParcelable(Common.VEHICLE_DATA, vehicleData)
                    navigator.load(SelectLocationFragment::class.java).setBundle(bundle).replace(true)
                }

            }


            R.id.imageViewCurrentLocation -> {
                locationManager.triggerLocation(object : LocationManager.LocationListener {
                    override fun onLocationAvailable(latLng: LatLng) {
                        latitudeCurrent = latLng.latitude
                        longitudeCurrent = latLng.longitude
                        locationManager.stop()
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }

                    override fun onFail(status: LocationManager.LocationListener.Status) {

                    }
                })
            }

            R.id.imageViewMapType -> {
                if (googleMap.mapType == GoogleMap.MAP_TYPE_SATELLITE) {
                    googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                } else {
                    googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
            }

            R.id.imagViewCircularMenu -> {

                if (!isMenuOpen) {

                    circularMenuOpen(false)
                } else {

                    circularMenuOpen(true)
                }

            }

            R.id.imageService2 -> {
                if (vehicleData != null)
                    openServicePopup(getString(R.string.service_2),
                            vehicleData!!.victoriaRoyal, 2)
            }

            R.id.textViewService2 -> {
                if (vehicleData != null)
                    openServicePopup(getString(R.string.service_2),
                            vehicleData!!.victoriaRoyal, 2)
            }

            R.id.imageService3 -> {
                if (vehicleData != null)
                    openServicePopup(getString(R.string.service_3),
                            vehicleData!!.group, 3)
            }

            R.id.textViewServiceName3 -> {
                if (vehicleData != null)
                    openServicePopup(getString(R.string.service_3),
                            vehicleData!!.group,
                            3)
            }

            R.id.imageService4 -> {
                if (vehicleData != null)
                    openServicePopup(getString(R.string.service_4),
                            vehicleData!!.airport, 4)
            }

            R.id.textViewServiceName4 -> {
                if (vehicleData != null)
                    openServicePopup(getString(R.string.service_4), vehicleData!!.airport,
                            4)
            }
        }
    }

    private fun openServicePopup(serviceType: String, carTypes: List<VictoriaVehicleData>, position: Int) {
        circularMenuOpen(true)
        val serviceBottomSheetFragment = ServiceBottomSheetFragment(object : ServiceBottomSheetFragment.CallbackVehicleSelection {
            override fun vehicleSelected(vehicle: VictoriaVehicleData) {

                VictoriaCustomer.getRideData().isCarSelected = true
                VictoriaCustomer.getRideData().carType = position
                VictoriaCustomer.getRideData().vehicle_id = vehicle.vehicleId.toString()

                when (position) {
                    2 -> VictoriaCustomer.getRideData().ride_type = "VictoriaRoyal"
                    3 -> VictoriaCustomer.getRideData().ride_type = "Group"
                    else -> VictoriaCustomer.getRideData().ride_type = "Airport"
                }
                VictoriaCustomer.getRideData().vehicle_name = VictoriaCustomer.getRideData().ride_type + " - " + vehicle.vehicle
                if (vehicleData != null) {
                    val bundle = Bundle()
                    bundle.putParcelable(Common.VEHICLE_DATA, vehicleData)
                    navigator.load(SelectLocationFragment::class.java).setBundle(bundle).replace(true)
                }

                /*if (VictoriaCustomer.getRideData().pickup_dropoff && (driverList?.size!! > 0)) {

                    navigator.load(FareEstimationFragment::class.java).replace(true)

                } else {


                }*/
            }
        })/*CallbackSuccess {

            VictoriaCustomer.getRideData().isCarSelected = true
            VictoriaCustomer.getRideData().carType = position

            if (VictoriaCustomer.getRideData().pickup_address != null
                    && VictoriaCustomer.getRideData().dropoff_address != null && (driverList?.size!!>0)) {

                navigator.load(FareEstimationFragment::class.java).replace(true)

            } else {

                if (vehicleData != null) {
                    val bundle = Bundle()
                    bundle.putParcelable(Common.VEHICLE_DATA, vehicleData)
                    navigator.load(SelectLocationFragment::class.java).setBundle(bundle).replace(true)
                }
            }

        })*/
        serviceBottomSheetFragment.setData(serviceType, carTypes)
        serviceBottomSheetFragment.show(fragmentManager, "")
    }

    private fun circularMenuOpen(isOpen: Boolean) {
        if (!isOpen) {
            isMenuOpen = true
            imagViewCircularMenu.setImageResource(R.drawable.circular_menu_pink)
            TransitionManager.beginDelayedTransition(constraintLayoutRoot, android.support.transition
                    .Fade(1))
            group3.visibility = View.VISIBLE
        } else {
            isMenuOpen = false
            imagViewCircularMenu.setImageResource(R.drawable.circular_menu)
            TransitionManager.beginDelayedTransition(constraintLayoutRoot, android.support.transition
                    .Fade(2))
            group3.visibility = View.GONE
        }
    }

    /**
     * nearby driver API calling stuff
     * */

    private fun observeNearByDriverListing() {
        homeViewModel?.nearByDriver?.observe(this, { responseBody ->

            navigator.toggleLoader(false)

            if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {

                handleNearByDriverResponse(responseBody)
            } else {
                showMessage(responseBody.message)
            }

        }, {
            navigator.toggleLoader(false);true
        })
    }

    /**
     * nearby driver API calling stuff
     * */


    private fun getNearByDriverData(): Parameter {
        val parameter = Parameter()

        parameter.latitude = latitude.toString()
        parameter.longitude = longitude.toString()

        return parameter
    }

    private fun handleNearByDriverResponse(responseBody: ResponseBody<List<NearByDriver>>) {
        navigator.toggleLoader(false)
        VictoriaCustomer.getRideData().isNearByDriver = false
        when {
            responseBody.responseCode == URLFactory.ResponseCode.SUCCESS -> {
                addDrivers(responseBody.data)
                navigator.toggleLoader(true)
                VictoriaCustomer.getRideData().isNearByDriver = true
                homeViewModel?.vehicleListing()
            }
            responseBody.responseCode == URLFactory.ResponseCode.NO_DATA_FOUND -> {
                navigator.toggleLoader(true)
                showMessage(responseBody.message)
                homeViewModel?.vehicleListing()
            }
            else -> showMessage(responseBody.message)
        }
    }

    /**
     * vehicleListing driver API calling stuff
     * */

    private fun observeVehicleListing() {
        homeViewModel?.vehicleData?.observe(this, { responseBody ->
            handleVehicleListingResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    /**
     * vehicleListing API calling stuff
     * */

    private fun handleVehicleListingResponse(responseBody: ResponseBody<VehicleData>) {
        navigator.toggleLoader(false)
        if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {
            setDataFromVehicleDetail(responseBody.data)
        } else {
            showMessage(responseBody.message)
        }
    }

    private var vehicleData: VehicleData? = null

    private fun setDataFromVehicleDetail(vehicleData: VehicleData?) {
        if (vehicleData != null) {
            this.vehicleData = vehicleData
        }
    }


}
