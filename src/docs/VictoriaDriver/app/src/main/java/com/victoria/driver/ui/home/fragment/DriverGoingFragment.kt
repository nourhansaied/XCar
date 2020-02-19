package com.victoria.driver.ui.home.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.squareup.picasso.Picasso
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.core.map_route.map.MapNavigator
import com.victoria.driver.core.map_route.map.draw_route.DrawRoute
import com.victoria.driver.di.App
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.interfaces.UpdateTimeAndDistance
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeActivityViewModel
import com.victoria.driver.util.CircleTransform
import com.victoria.driver.util.LocationManager
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_driver_going.*
import kotlinx.android.synthetic.main.toolbar_with_menu_black.*
import java.util.*
import javax.inject.Inject


class DriverGoingFragment : BaseFragment(), OnMapReadyCallback, UpdateTimeAndDistance/*, Consumer<Location>*/ {

    @Inject
    lateinit var locationManager: LocationManager

    private lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    private var drawRoute: DrawRoute? = null

    private val builder = LatLngBounds.Builder()

    private var latitudeDriver: kotlin.Double = 0.0
    private var longitudeDriver: kotlin.Double = 0.0

    private var latitudeCurrent: kotlin.Double = 0.0
    private var longitudeCurrent: kotlin.Double = 0.0
    private lateinit var disposable: Disposable
    private var mapNavigator: MapNavigator? = null

    override fun createLayout(): Int {
        return R.layout.fragment_driver_going
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun bindData() {
        imageViewMenu.visibility = View.INVISIBLE
        /*disposable = locationManager.subject.subscribe(this@DriverGoingFragment)*/
        appPreferences.putString(Common.TRIP_DISTANCE, "0.0")

        toolBarText.text = "Ride"
        textViewCancel.visibility = View.VISIBLE

        imageViewRecenter.setOnClickListener(this::onViewClick)
        buttonViewArrived.setOnClickListener(this::onViewClick)
        imageViewCall.setOnClickListener(this::onViewClick)
        imagViewMessage.setOnClickListener(this::onViewClick)
        imageViewDriver.setOnClickListener(this::onViewClick)
        textViewCancel.setOnClickListener(this::onViewClick)
        imageViewNightMode.setOnClickListener(this::onViewClick)
        loadMap()

        setUserData()

    }

    @SuppressLint("SetTextI18n")
    private fun setUserData() {
        latitudeCurrent = App.getTripData()?.pickupLatitude?.toString()?.toDouble()!!
        longitudeCurrent = App.getTripData()?.pickupLongitude?.toString()?.toDouble()!!

        latitudeDriver = App.getTripData()?.driverData?.latitude?.toString()?.toDouble()!!
        longitudeDriver = App.getTripData()?.driverData?.longitude?.toString()?.toDouble()!!

        textViewDriverName.text = App.getTripData().customerData.name
        textViewCarType.text = App.getTripData().dropoffAddress
        Picasso.get()
                .load(App.getTripData().customerData.profileImageThumb)
                .transform(CircleTransform())
                .into(imageViewDriver)
        textViewRating.text = (String.format("%.2f", App.getTripData().customerData.rating))

        textViewTime.text = App.getTripData().waitingTime.toString()
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
        mapNavigator?.setPathUpdateListener(drawRoute)

        drawPickUpPath()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewClick(view: View) {
        when (view.id) {


            R.id.imageViewCall -> {
                if (isPermissionGranted()) {
                    callAction()
                }
            }
            R.id.imageViewDriver -> {
                navigator.load(ReceiptFragment::class.java).replace(false)

            }
            R.id.buttonViewArrived -> {
                observeArrivedApiCall()
            }

            R.id.imagViewMessage -> {
                navigator.load(ChatMessageListingFragment::class.java).replace(true)
            }

            R.id.imageViewRecenter -> {
                builder.include(LatLng(App.getTripData().pickupLongitude.toDouble(),
                        App.getTripData().pickupLongitude.toDouble())).include(LatLng(App.getTripData().dropoffLatitude.toDouble()
                        , App.getTripData().pickupLongitude.toDouble()))
                recenterMap()
            }
            R.id.textViewCancel -> {
                navigator.load(CancelRideFragment::class.java).replace(true)
            }

            R.id.imageViewNightMode -> {
                if (googleMap.mapType == GoogleMap.MAP_TYPE_SATELLITE) {
                    googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                } else {
                    googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
            }
        }
    }

    private fun drawPickUpPath() {
        try {

            val driverCurrent = LatLng(latitudeDriver, longitudeDriver)

            drawRoute!!.setUpdateTimeAndDistance(this)
            val latLng = LatLng(latitudeCurrent, longitudeCurrent)
            drawRoute?.drawPath(Arrays.asList(driverCurrent, latLng), R.drawable.car,
                    R.drawable.pickup_big, object : DrawRoute.OnDrawComplete {
                override fun onComplete() {
                    val startMarker = drawRoute?.startMarker
                    startMarker!!.rotation = drawRoute!!.bearing
                    mapNavigator?.setCurrentMarker(startMarker)
                    mapNavigator?.startNavigation()
                    drawRoute!!.setUpdateTimeAndDistance(this@DriverGoingFragment)
                }
            })
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun recenterMap() {

        /*val currentPolylineOptions = PolylineOptions()
        // polyline.remove();
        currentPolylineOptions.color(ContextCompat.getColor(this!!.context!!, R.color.colorAccent))
        currentPolylineOptions.width(8f)

        val bounds = builder.build()
        // bounds.including()
        val padding = 70
        val width = context!!.resources.getDimensionPixelOffset(R.dimen.dp_250)
        val height = context!!.resources.getDimensionPixelOffset(R.dimen.dp_250)
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
        googleMap.animateCamera(cu)*/
        if (mapNavigator != null && mapNavigator?.lastLocation != null)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mapNavigator?.lastLocation?.latitude!!, mapNavigator?.lastLocation?.longitude!!), 18f))
    }

    private fun callAction() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + App.getTripData().customerData.countryCode + App.getTripData().customerData.phone)
        startActivity(callIntent)
    }

    private fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(this!!.activity!!, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                return true
            } else {
                requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 1)
                //ActivityCompat.requestPermissions(this.activity!!, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return false
            }
        } else { //permission is automatically granted on sdLog.v("TAG", "Permission is granted")
            return true
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callAction()
                } else {
                    Toast.makeText(activity!!.applicationContext, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    private fun observeArrivedApiCall() {
        homeActivityViewModel.arrived.observe(this, onChange = {
            navigator.toggleLoader(false)
            homeActivityViewModel.arrived.removeObservers(this)
            showMessage(it.message)
            if (it.responseCode == 1) {
                App.setTripData(it.data)
                navigator.load(RideStartFragment::class.java).replace(false)
            }
        }, onError = {
            true
        })

        val parameter = Parameter()
        parameter.tripId = App.getTripData().tripId.toString()
        navigator.toggleLoader(true)
        homeActivityViewModel.arrivedApiCall(parameter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mapNavigator != null) {
            mapNavigator!!.stopNavigation()
        }
    }

    override fun onComplete(time: String?, km: String?) {
        if (arguments != null && arguments!!.containsKey(Common.IS_OPEN_CHAT)) {
            if (arguments!!.getBoolean(Common.IS_OPEN_CHAT)) {
                arguments!!.clear()
                navigator.load(ChatMessageListingFragment::class.java).replace(true)
            }
        }
        textViewTime.text = "$time"
    }
}
