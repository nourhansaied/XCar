package com.victoria.driver.ui.home.fragment

import android.arch.lifecycle.ViewModelProviders
import android.view.View
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.data.pojo.ResponseBody
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.dialog_fragment.RideRequestPopup
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeFragmentViewModel
import com.victoria.driver.util.LocationManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*
import javax.inject.Inject


class HomeFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener, RideRequestPopup.CallBackForRideNavigation {

    @Inject
    lateinit var locationManager: LocationManager

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    var latitudeCurrent: kotlin.Double = 23.0752
    var longitudeCurrent: kotlin.Double = 72.5257

    lateinit var latLngMain: LatLng

    override fun createLayout(): Int = R.layout.fragment_home

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val homeFragmentViewModel: HomeFragmentViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeFragmentViewModel::class.java]
    }

    override fun bindData() {
        loadMap()
        // toolBarText.text = getString(R.string.online)


        checkBoxOnOff.visibility = View.VISIBLE
        checkBoxOnOff.isChecked = session.user?.service.equals(Common.ONLINE)

        if (checkBoxOnOff.isChecked) {
            toolBarText.text = getString(R.string.online)
        } else {
            toolBarText.text = getString(R.string.offline)
        }

        imageViewClose.setOnClickListener { onViewClick(it) }
        frameLayoutMapHome.setOnClickListener { onViewClick(it) }
        imageViewNightMode.setOnClickListener { onViewClick(it) }
        imageViewRecenter.setOnClickListener { onViewClick(it) }
        viewBackGroundOnline.setOnClickListener { onViewClick(it) }
        //service[,]
        checkBoxOnOff.setOnClickListener {

            if (checkBoxOnOff.isChecked) {

                observeStatusChangeResponse(false)

                toolBarText.text = getString(R.string.online)
                viewBackGroundOnline.setBackgroundResource(R.drawable.online)
                val top = resources.getDrawable(R.drawable.smile)
                textViewOffOnText.text = getString(R.string.you_are_online)
                textViewOffOnText.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null)
                textViewOffOnText.visibility = View.VISIBLE
                openOnlineOffLine()
            } else {

                observeStatusChangeResponse(true)

                toolBarText.text = getString(R.string.offline)
                viewBackGroundOnline.setBackgroundResource(R.drawable.off_line)
                textViewOffOnText.visibility = View.VISIBLE
                textViewOffOnText.text = getString(R.string.you_are_offlin)
                textViewOffOnText.visibility = View.VISIBLE
                val top = resources.getDrawable(R.drawable.sad)
                textViewOffOnText.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null)
                openOnlineOffLine()
            }
        }
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
                addMarker()
                latLngMain = latLng
                locationManager.stop()
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }

            override fun onFail(status: LocationManager.LocationListener.Status) {

            }
        })

        googleMap.setOnMapClickListener {

            closeOnlineOffLine()
        }

        googleMap.setOnCameraIdleListener(this)


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


    override fun onViewClick(view: View) {

        when (view.id) {

            R.id.viewBackGroundOnline -> {
                closeOnlineOffLine()
            }

            R.id.imageViewNightMode -> {
                if (googleMap.mapType == GoogleMap.MAP_TYPE_SATELLITE) {
                    googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                } else {
                    googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                }
                /* val dialogFragment = RideRequestPopup(this, "30")
                 dialogFragment.show(activity!!.supportFragmentManager, "")*/
            }

            R.id.imageViewClose -> {
                closeOnlineOffLine()
            }

            R.id.imageViewRecenter -> {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitudeCurrent, longitudeCurrent), 15f))
            }
        }
    }

    private fun loadMap() {

        mapFragment = SupportMapFragment()
        this.childFragmentManager.beginTransaction().replace(R.id.frameLayoutMapHome, mapFragment).commit()
        mapFragment.getMapAsync(this)

    }

    private fun openOnlineOffLine() {

        viewBackGroundOnline.visibility = View.VISIBLE
        imageViewClose.visibility = View.VISIBLE

        viewBackGroundOnline.animate().translationY(0.toFloat()).setDuration(500).withEndAction {
            textViewOffOnText.visibility = View.VISIBLE


        }.start()
    }

    private fun closeOnlineOffLine() {

        var animValue: Float = viewBackGroundOnline.height.toFloat()
        /* viewBackGroundOnline.visibility = View.GONE
         imageViewClose.visibility = View.GONE*/
        viewBackGroundOnline.animate().translationY(-animValue).setDuration(500).withEndAction {
            textViewOffOnText.visibility = View.GONE
            imageViewClose.visibility = View.GONE
        }.start()
    }

    override fun rideNavigationCallBack(isAccept: Boolean) {


        //navigator.load(DriverGoingFragment::class.java).replace(true)
    }

    /**
     * Change Status API calling stuff
     * */


    private fun observeStatusChangeResponse(isOnline: Boolean) {
        homeFragmentViewModel.changeStatus.observe(this, { responseBody ->
            homeFragmentViewModel.changeStatus.removeObservers(this)
            handleStatusChangeResponse(responseBody)
        })
        if (isOnline) {
            var parameter = Parameter()
            parameter.service = "Offline"
            var user = session.user
            user?.service = "Offline"
            session.user = user
            homeFragmentViewModel.changeStatusApiCall(parameter)
        } else {
            var parameter = Parameter()
            parameter.service = "Online"
            var user = session.user
            user?.service = "Online"
            session.user = user
            homeFragmentViewModel.changeStatusApiCall(parameter)
        }

    }

    private fun handleStatusChangeResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        if (responseBody.responseCode == 1) {

        } else {
            showMessage(responseBody.message)
        }
    }

    override fun dissmissAfterTimesUp() {

    }
    /**
     * Change Status API calling stuff
     * */


}






