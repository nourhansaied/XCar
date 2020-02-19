package com.victoria.driver.ui.home.fragment

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
import android.util.Log
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
import com.victoria.driver.ui.home.activity.IsolatedActivity
import com.victoria.driver.ui.interfaces.UpdateTimeAndDistance
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeActivityViewModel
import com.victoria.driver.util.CircleTransform
import com.victoria.driver.util.CityAsyncTask
import com.victoria.driver.util.LocationManager
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_driver_going.*
import kotlinx.android.synthetic.main.toolbar_with_menu_black.*
import java.util.*
import javax.inject.Inject


class RideStartFragment : BaseFragment(), OnMapReadyCallback, UpdateTimeAndDistance, Consumer<Location> {

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    private var disposable: Disposable? = null

    private var drawRoute: DrawRoute? = null
    internal var mapNavigator: MapNavigator? = null

    private val builder = LatLngBounds.Builder()

    var latitudeDriver: kotlin.Double = 0.0
    var longitudeDriver: kotlin.Double = 0.0

    var latitudeCurrent: kotlin.Double = 0.0
    var longitudeCurrent: kotlin.Double = 0.0
    private var distanceForEndTrip: Float = 0.0f

    private lateinit var oldLatLngForEndTrip: Location

    var isCompleteScreen: Boolean = false

    @Inject
    lateinit var locationManager: LocationManager
    private lateinit var currentLocation: Location


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

        if (disposable == null)
            disposable = locationManager.subject.subscribe(this)

        if (!appPreferences.getString(Common.TRIP_DISTANCE).equals("0.0") &&
                !appPreferences.getString(Common.TRIP_DISTANCE).equals("")) {
            distanceForEndTrip = appPreferences.getString(Common.TRIP_DISTANCE).toFloat()
        }

        textViewCancel.visibility = View.VISIBLE

        toolBarText.text = "Ride"
        buttonViewArrived.text = getString(R.string.let_go)
        buttonChangeRoute.setOnClickListener { onViewClick(it) }

        if (App.getTripData().status.equals(Common.TRIP_TAG_PROCESSING)) {
            changesAfterRideGetStart()
        }

        textViewCancel.setOnClickListener(this::onViewClick)
        imageViewMenu.setOnClickListener(this::onViewClick)
        imageViewDriver.setOnClickListener(this::onViewClick)
        imagViewMessage.setOnClickListener(this::onViewClick)
        buttonViewArrived.setOnClickListener(this::onViewClick)
        buttonViewCompleted.setOnClickListener(this::onViewClick)
        imageViewRecenter.setOnClickListener(this::onViewClick)
        imageViewMenu.setOnClickListener(this::onViewClick)
        imageViewCall.setOnClickListener(this::onViewClick)
        imageViewNightMode.setOnClickListener(this::onViewClick)
        setUserData()


    }

    @SuppressLint("SetTextI18n")
    private fun setUserData() {

        if (App.getTripData().dropoffLatitude != null && App.getTripData().dropoffLongitude != null) {
            latitudeCurrent = App.getTripData().dropoffLatitude.toString().toDouble()
            longitudeCurrent = App.getTripData().dropoffLongitude.toString().toDouble()

            latitudeDriver = App.getTripData().driverData.preLatitude.toString().toDouble()
            longitudeDriver = App.getTripData().driverData.preLongitude.toString().toDouble()

            textViewDriverName.text = App.getTripData().customerData.name
            textViewCarType.text = App.getTripData().dropoffAddress

            Picasso.get()
                    .load(App.getTripData().customerData.profileImageThumb)
                    .transform(CircleTransform())
                    .into(imageViewDriver)
            textViewRating.text = (String.format("%.2f", App.getTripData().customerData.rating))

            textViewTime.text = App.getTripData().waitingTime.toString()


            loadMap()
        }
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
        mapNavigator?.setPathUpdateListener(drawRoute)
        if (locationManager.lastLocation != null) {

            currentLocation = locationManager.lastLocation!!


        } /*else {
            locationManager.triggerLocation(object : LocationManager.LocationListener {

                override fun onLocationAvailable(latLng: LatLng) {
                    val location = Location("")
                    location.latitude = latLng.latitude
                    location.longitude = latLng.longitude
                    currentLocation = location
                    locationManager.stop()
                }

                override fun onFail(status: LocationManager.LocationListener.Status) {
                }
            })
        }*/
        drawPickUpPath()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (data != null && data?.getBooleanExtra(Common.ISCHANGEROUTE, false)!!) {
                setUserData()
            }
        }
    }

    override fun onViewClick(view: View) {
        when (view.id) {

            R.id.imageViewCall -> {
                if (imageViewCall.isEnabled) {
                    if (isPermissionGranted())
                        callAction()
                }
            }

            R.id.textViewCancel -> {
                navigator.load(CancelRideFragment::class.java).replace(true)
            }

            R.id.imagViewMessage -> {
                navigator.load(ChatMessageListingFragment::class.java).replace(true)

                //navigator.load(ChatFragment::class.java).replace(true)
            }

            R.id.buttonViewArrived -> {
                observeRideStartApiCalling()
                //changesAfterRideGetStart()
            }

            R.id.buttonViewCompleted -> {
                observerCompleteRideApiCalling()
            }

            R.id.buttonChangeRoute -> {
                navigator.loadActivity(IsolatedActivity::class.java)
                        .setPage(ChangeYourRouteFragment::class.java).forResult(1).start()
                //navigator.loadActivity(ChangeYourRouteFragment::class.java).add(true)
            }

            R.id.imageViewRecenter -> {
                builder.include(LatLng(App.getTripData().pickupLongitude.toDouble(),
                        App.getTripData().pickupLongitude.toDouble())).include(LatLng(App.getTripData().dropoffLatitude.toDouble(), App.getTripData().pickupLongitude.toDouble()))
                recenterMap()
            }

            R.id.imageViewMenu -> {
                // openDrawer()
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

            val latLng = LatLng(latitudeCurrent, longitudeCurrent)
            drawRoute?.drawPath(Arrays.asList(driverCurrent, latLng), R.drawable.car,
                    R.drawable.dropoff_big, object : DrawRoute.OnDrawComplete {
                override fun onComplete() {
                    val startMarker = drawRoute?.startMarker
                    startMarker?.rotation = drawRoute!!.bearing
                    mapNavigator?.setCurrentMarker(startMarker)
                    mapNavigator?.startNavigation()
                    drawRoute?.setUpdateTimeAndDistance(this@RideStartFragment)
                }
            })
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun changesAfterRideGetStart() {
        //appPreferences.putString(Common.TRIP_DISTANCE, "0.0")
        isCompleteScreen = true
        buttonViewCompleted.visibility = View.VISIBLE
        buttonChangeRoute.visibility = View.VISIBLE
        imageViewCall.setImageDrawable(context!!.getDrawable(R.drawable.calldisable))
        imagViewMessage.setImageDrawable(context!!.getDrawable(R.drawable.msgdisable))
        imagViewMessage.isEnabled = false
        textViewCancel.visibility = View.INVISIBLE
    }

    private fun recenterMap() {
        /*val currentPolylineOptions = PolylineOptions()
        // polyline.remove();
        currentPolylineOptions.color(ContextCompat.getColor(this!!.context
        !!, R.color.colorAccent))
        currentPolylineOptions.width(8f)

        val bounds = builder.build()
        // bounds.including()
        val padding = 70
        val width = context!!.resources.getDimensionPixelOffset(R.dimen.dp_250)
        val height = context!!.resources.getDimensionPixelOffset(R.dimen.dp_250)
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
        googleMap.animateCamera(cu)*/
        if (mapNavigator != null && mapNavigator?.lastLocation != null)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mapNavigator?.lastLocation?.latitude!!,
                    mapNavigator?.lastLocation?.longitude!!), 18f))
    }

    private fun callAction() {

        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + "1212121212")
        startActivity(callIntent)
    }

    private fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this!!.activity!!, Manifest.permission.CALL_PHONE) === PackageManager.PERMISSION_GRANTED) {

                return true
            } else {

                ActivityCompat.requestPermissions(this.activity!!, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return false
            }
        } else { //permission is automatically granted on sdLog.v("TAG", "Permission is granted")
            return true
        }
    }

    private fun observeRideStartApiCalling() {
        homeActivityViewModel.rideStart.observe(this, onChange = {
            homeActivityViewModel.rideStart.removeObservers(this)
            navigator.toggleLoader(false)
            if (it.responseCode == 1) {
                distanceForEndTrip = 0.0f
                appPreferences.putString(Common.TRIP_DISTANCE, "0.0")
                showMessage(it.message)
                App.setTripData(it.data)
                changesAfterRideGetStart()
            }
        }, onError = {
            true
        })

        val parameter = Parameter()
        parameter.tripId = App.getTripData().tripId.toString()
        navigator.toggleLoader(true)
        homeActivityViewModel.rideStartApiCall(parameter)

    }

    private fun observerCompleteRideApiCalling() {
        homeActivityViewModel.completeRide.observe(this, onChange = {
            navigator.toggleLoader(false)
            homeActivityViewModel.completeRide.removeObservers(this)
            showMessage(it.message)
            if (it.responseCode == 1) {
                App.setTripData(it.data)
                navigator.load(ReceiptFragment::class.java).replace(false)
            } else {
                showMessage(it.message)
            }
        }, onError = {
            true
        })

        val parameter = Parameter()
        parameter.tripId = App.getTripData().tripId.toString()
        parameter.distance = (appPreferences.getString(Common.TRIP_DISTANCE).toFloat() / 1000).toString()
        parameter.dropoff_latitude = App.getTripData().dropoffLatitude.toString()
        parameter.dropoff_longitude = App.getTripData().dropoffLongitude.toString()
        parameter.dropoff_address = getCityName(currentLocation.latitude, currentLocation.longitude)
        navigator.toggleLoader(true)
        homeActivityViewModel.completeRide(parameter)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {

            1 -> {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity!!.applicationContext, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show()
                    callAction()
                } else {
                    Toast.makeText(activity!!.applicationContext, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }

    override fun onComplete(time: String?, km: String?) {
        Handler().postDelayed({
            if (textViewTime != null) {

                textViewTime.text = "$time"
            }
        }, 300)

    }

    override fun accept(newLocation: Location?) {
        if (::currentLocation.isInitialized) {
            oldLatLngForEndTrip = currentLocation
        }

        currentLocation = newLocation!!


        if (::currentLocation.isInitialized && ::oldLatLngForEndTrip.isInitialized) {
            if (currentLocation != null && oldLatLngForEndTrip != null) {
                Log.e("Distance =====", "Distance =====" + appPreferences.getString(Common.TRIP_DISTANCE))
                val results = FloatArray(1)
                Location.distanceBetween(oldLatLngForEndTrip.latitude,
                        oldLatLngForEndTrip.longitude, currentLocation.latitude, currentLocation.longitude, results)
                distanceForEndTrip += results[0]
                appPreferences.putString(Common.TRIP_DISTANCE, distanceForEndTrip.toString())
                //  Toast.makeText(context, appPreferences.getString(Common.TRIP_DISTANCE), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCityName(latitude: Double, longitude: Double): String {
        //get address from latlng

        val cityNameInBackgroundTask = CityAsyncTask(activity, latitude, longitude)
        cityNameInBackgroundTask.execute()

        val locationName: String;
        locationName = cityNameInBackgroundTask.get().toString()
        return locationName
    }

}
