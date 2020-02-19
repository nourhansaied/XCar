package com.victoria.driver.ui.home.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.view.View
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.di.App
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.dialog_fragment.RideRequestPopup
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeActivityViewModel
import com.victoria.driver.util.LocationManager
import kotlinx.android.synthetic.main.fragment_change_location.*
import javax.inject.Inject


class ChangeYourRouteFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, RideRequestPopup.CallBackForRideNavigation {

    @Inject
    lateinit var locationManager: LocationManager

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    private var latitudeCurrent: Double = 23.0752
    private var longitudeCurrent: Double = 72.5257

    private lateinit var latLngMain: LatLng
    private var PLACE_PICKER_REQUEST = 1
    private var updatedLatLng: LatLng? = null


    override fun createLayout(): Int = R.layout.fragment_change_location

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        navigator.toggleLoader(true)
        loadMap()

        imageViewNightMode.setOnClickListener { onViewClick(it) }
        imageViewRecenter.setOnClickListener { onViewClick(it) }
        textViewDropoff.setOnClickListener { onViewClick(it) }
        imageViewClose.setOnClickListener { onViewClick(it) }
        imageViewApply.setOnClickListener { onViewClick(it) }
    }

    override fun onCameraIdle() {

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!
        MapsInitializer.initialize(context)
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isCompassEnabled = false

        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(latLng: LatLng) {
                latitudeCurrent = latLng.latitude
                longitudeCurrent = latLng.longitude
                latLngMain = latLng
                locationManager.stop()
                if (App != null && App.getTripData() != null && App.getTripData().dropoffLatitude == null)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
            }

            override fun onFail(status: LocationManager.LocationListener.Status) {

            }
        })

        googleMap.setOnCameraIdleListener(this)
        addMarkerAfterLocationPick(LatLng(App.getTripData().dropoffLatitude.toDouble(), App.getTripData().dropoffLongitude.toDouble()))
        textViewDropoff.text = App.getTripData().dropoffAddress.toString()

        navigator.toggleLoader(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(data, context)
                val toastMsg = String.format("Place: %s", place.getName())
                updatedLatLng = place.latLng
                textViewDropoff.text = place.name
                addMarkerAfterLocationPick(place.latLng)
            }
        }
    }


    override fun onViewClick(view: View) {

        when (view.id) {
            R.id.imageViewNightMode -> {
                if (googleMap.mapType == GoogleMap.MAP_TYPE_SATELLITE) {
                    googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                } else {
                    googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
                /*val dialogFragment = RideRequestPopup(this, "30", appPreferences)
                dialogFragment.show(activity!!.supportFragmentManager, "")*/
            }

            R.id.imageViewRecenter -> {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitudeCurrent, longitudeCurrent), 18f))
            }

            R.id.textViewDropoff -> {
                val builder = PlacePicker.IntentBuilder()
                startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
            }

            R.id.imageViewApply -> {
                if (updatedLatLng != null)
                    observeChangeDropOff()
                else
                    navigator.finish()
            }

            R.id.imageViewClose -> {
                navigator.goBack()
            }
        }
    }

    private fun getParameters(): Parameter {

        val parameters = Parameter()
        parameters.tripId = App.getTripData().tripId.toString()
        parameters.dropoff_latitude = updatedLatLng?.latitude.toString()
        parameters.dropoff_longitude = updatedLatLng?.longitude.toString()
        parameters.dropoff_address = textViewDropoff.text.toString()

        return parameters
    }

    private fun loadMap() {

        mapFragment = SupportMapFragment()
        this.childFragmentManager.beginTransaction().replace(R.id.frameLayoutMapHome, mapFragment).commit()
        mapFragment.getMapAsync(this)

    }

    override fun rideNavigationCallBack(isAccept: Boolean) {
        navigator.load(DriverGoingFragment::class.java).replace(true)
    }

    private fun addMarkerAfterLocationPick(latLng: LatLng) {
        googleMap.clear()

        val builder = LatLngBounds.Builder()

        val markerOptions = MarkerOptions()
                .position(latLng)
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.navigate))
        googleMap.addMarker(markerOptions)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        builder.include(latLng)
    }

    private fun observeChangeDropOff() {
        homeActivityViewModel.changeDropOff.observe(this, onChange = {
            homeActivityViewModel.changeDropOff.removeObservers(this)
            navigator.toggleLoader(false)
            showMessage(it.message)
            if (it.responseCode == 1) {
                App.setTripData(it.data)
                val resultIntent = Intent()
                resultIntent.putExtra(Common.ISCHANGEROUTE, true)
                activity!!.setResult(Activity.RESULT_OK, resultIntent)
                navigator.finish()
            }
        }, onError = {
            true
        })
        navigator.toggleLoader(true)
        homeActivityViewModel.changeDropOff(getParameters())
    }

    override fun dissmissAfterTimesUp() {

    }
}






