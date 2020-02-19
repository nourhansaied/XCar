package com.victoria.customer.ui.home.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.transition.TransitionManager
import android.view.View
import android.widget.ImageView
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.map_route.map.draw_route.DrawRoute
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Service
import com.victoria.customer.model.VehicleData
import com.victoria.customer.model.VictoriaVehicleData
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.dialog.ServiceBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_home_layout.*
import kotlinx.android.synthetic.main.toolbar_with_close.*
import java.util.*


class HomeFragment : BaseFragment(), OnMapReadyCallback {


    private lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    lateinit var icon: ImageView

    private var drawRoute: DrawRoute? = null

    lateinit var list: ArrayList<Service>

    private var isMenuOpen: Boolean = false

    override fun createLayout(): Int {
        return R.layout.fragment_home_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private var vehicleData: VehicleData? = null

    override fun bindData() {
        toolBarText.text = ""

        loadMap()

        if (arguments != null) {
            if (arguments!!.containsKey(Common.VEHICLE_DATA)) {
                vehicleData = arguments!!.getParcelable(Common.VEHICLE_DATA)
            }
        }


        textViewPickup.text = VictoriaCustomer.getRideData().pickup_address
        textViewDropoff.text = VictoriaCustomer.getRideData().dropoff_address

        imageViewBack.setOnClickListener { onViewClick(it) }
        textViewPickup.setOnClickListener { onViewClick(it) }
        textViewDropoff.setOnClickListener { onViewClick(it) }
        imagViewCircularMenu.setOnClickListener { onViewClick(it) }
        imageService2.setOnClickListener { onViewClick(it) }
        imageService3.setOnClickListener { onViewClick(it) }
        imageService4.setOnClickListener { onViewClick(it) }
        textViewService2.setOnClickListener { onViewClick(it) }
        textViewServiceName3.setOnClickListener { onViewClick(it) }
        textViewServiceName4.setOnClickListener { onViewClick(it) }


    }


    private fun loadMap() {
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        mapFragment = SupportMapFragment()
        this.childFragmentManager.beginTransaction().replace(R.id.frameLayoutMapHome, mapFragment).commit()
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        MapsInitializer.initialize(context)
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.setPadding(0, 0, 0, 15)
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        drawRoute = DrawRoute(googleMap, activity)
        navigator.toggleLoader(false)
        googleMap.setOnMapLoadedCallback {
            drawPickUpPath()
        }

        googleMap.setOnMapClickListener { arg0 ->
            circularMenuOpen(true)
        }
    }

    private fun drawPickUpPath() {

        try {
            val driverCurrent = LatLng(VictoriaCustomer.getRideData().pickup_latitude?.toDouble()!!, VictoriaCustomer.getRideData().pickup_longitude?.toDouble()!!)

            val latLng = LatLng(VictoriaCustomer.getRideData().dropoff_latitude?.toDouble()!!, VictoriaCustomer.getRideData().dropoff_longitude?.toDouble()!!)
            drawRoute?.drawPath(Arrays.asList(driverCurrent, latLng), R.drawable.pickup_big,
                    R.drawable.dropoff_big, object : DrawRoute.OnDrawComplete {
                override fun onComplete() {


                }
            })
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewBack -> {
                circularMenuOpen(true)
                navigator.goBack()
            }

            R.id.textViewPickup -> {
                //openPlacePicker(Common.PICKEUP_LOCATION)
            }

            R.id.textViewDropoff -> {
                //openPlacePicker(Common.DROPOFF_LOCATION)
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
                VictoriaCustomer.getRideData().vehicle_id = vehicle.vehicleId.toString()
                VictoriaCustomer.getRideData().vehicle_name = vehicle.types + " - " + vehicle.vehicle
                val bundle = Bundle()
                bundle.putParcelable(Common.VEHICLE_DATA, vehicleData)

                navigator.load(FareEstimationFragment::class.java).setBundle(bundle).replace(true)

            }
        })/*CallbackSuccess {


            VictoriaCustomer.getRideData().isCarSelected = true


            navigator.load(FareEstimationFragment::class.java).replace(true)

        })*/
        serviceBottomSheetFragment.setData(serviceType, carTypes)
        serviceBottomSheetFragment.show(fragmentManager, "")

    }

    private fun circularMenuOpen(isOpen: Boolean) {

        if (!isOpen) {
            TransitionManager.beginDelayedTransition(constraintLayoutRoot, android.support.transition.Fade
            (1))
            isMenuOpen = true
            group3.visibility = View.VISIBLE
            imagViewCircularMenu.setImageResource(R.drawable.circular_menu_pink)
        } else {
            TransitionManager.beginDelayedTransition(constraintLayoutRoot, android.support.transition.Fade
            (2))
            isMenuOpen = false
            group3.visibility = View.GONE
            imagViewCircularMenu.setImageResource(R.drawable.circular_menu)
        }
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (activity != null) {
            when {
                requestCode == Common.PICKEUP_LOCATION && resultCode == RESULT_OK -> {
                    val place = PlaceAutocomplete.getPlace(activity, data)

                    textViewPickup.text = place.address


                }
                requestCode == Common.DROPOFF_LOCATION && resultCode == RESULT_OK -> {
                    val place = PlaceAutocomplete.getPlace(activity, data)
                    textViewDropoff.text = place.address

                }
                resultCode == PlaceAutocomplete.RESULT_ERROR -> PlaceAutocomplete.getStatus(activity, data)
                resultCode == RESULT_CANCELED -> {
                }
            }
        }
    }
}

