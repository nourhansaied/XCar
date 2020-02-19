package com.victoria.customer.ui.home.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.transition.Fade
import android.support.transition.TransitionManager
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Validator
import com.victoria.customer.core.map_route.map.draw_route.DrawRoute
import com.victoria.customer.data.URLFactory
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.model.FareEstimationData
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.dialog.CountryCodeDialog
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.ride.fragments.CarAllocationFragment
import com.victoria.customer.ui.home.ride.fragments.ScheduleRideFragment
import com.victoria.customer.ui.home.viewmodel.FareEstimationViewModel
import com.victoria.customer.ui.interfaces.ISelectCountry
import com.victoria.customer.util.CountryUtils
import com.victoria.customer.util.PopupViews
import kotlinx.android.synthetic.main.fragment_fare_estimation_layout.*
import java.util.*
import javax.inject.Inject

class FareEstimationFragment : BaseFragment(), OnMapReadyCallback {

    @Inject
    lateinit var validator: Validator

    private lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    private var drawRoute: DrawRoute? = null
    private var ridBy: String = "Me"

    private val fareEstimationViewModel: FareEstimationViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[FareEstimationViewModel::class.java]
    }

    override fun createLayout(): Int {
        return R.layout.fragment_fare_estimation_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    @SuppressLint("SetTextI18n")
    override fun bindData() {

        setObserver()
        // DebugLog.e("FARE DATA "+VictoriaCustomer.getRideData().toString())
        group2.visibility = ConstraintLayout.GONE

        textViewPickup.text = VictoriaCustomer.getRideData()?.pickup_address
        textViewDropoff.text = VictoriaCustomer.getRideData()?.dropoff_address
        textViewCarType.text = VictoriaCustomer.getRideData().vehicle_name
        VictoriaCustomer.getRideData().payment_mode = "cash"

        callGetFareEstimation()

        val locale = activity?.resources?.configuration?.locale?.country

        for (countryCode in CountryUtils.readCountryJson(activity)) {

            if (locale.equals(countryCode.iso2Cc, ignoreCase = true)) {
                textViewCountryCode.text = "+${countryCode.e164Cc}"
                imageViewFlag.setImageResource(CountryUtils.getFlagDrawableResId(countryCode))
                break
            }
        }

        loadMap()
        imageViewClose.setOnClickListener(this::onViewClick)
        radioButtonRideNow.setOnClickListener(this::onViewClick)
        radioButtonRideLater.setOnClickListener(this::onViewClick)
        imageViewFlag.setOnClickListener(this::onViewClick)
        textViewCountryCode.setOnClickListener(this::onViewClick)
        buttonPromoApply.setOnClickListener(this::onViewClick)
        textViewWhoRide.setOnClickListener(this::onViewClick)
        textViewPaymentMethod.setOnClickListener(this::onViewClick)
        imageRemove.setOnClickListener(this::onViewClick)

    }

    private fun loadMap() {

        mapFragment = SupportMapFragment()
        this.childFragmentManager.beginTransaction().replace(R.id.frameLayoutMapFare, mapFragment).commit()
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        googleMap.isMyLocationEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.setPadding(0, 0, 0, 15)
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        drawRoute = DrawRoute(googleMap, activity!!)

        googleMap.setOnMapLoadedCallback {
            drawPickUpPath()
        }
    }

    private fun init(fareEstimation: FareEstimationData?) {
        textViewEstimatedDuration.text = fareEstimation?.timeText
        textViewEstimatedKm.text = fareEstimation?.distanceText
        textViewEstimatedCost.text = "EGP " + (String.format("%.2f", fareEstimation?.totalAmount))
    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewClose -> {

                navigator.goBack()
            }

            R.id.radioButtonRideNow -> {
                VictoriaCustomer.getRideData().trip_type = Common.RideType.NOW.rideType
                when {
                    textViewWhoRide.text == getString(R.string.option_other) -> if (checkValidation()) {

                        VictoriaCustomer.getRideData().guest_phone = editTextPhoneNumber.text.toString()

                        placeOrderApi()

                    }
                    else -> placeOrderApi()
                }

            }

            R.id.radioButtonRideLater -> {

                VictoriaCustomer.getRideData().trip_type = Common.RideType.LATER.rideType
                VictoriaCustomer.getRideData().rideby = textViewWhoRide.text?.toString()
                VictoriaCustomer.getRideData().guest_phone = editTextPhoneNumber.text.toString()

                if (textViewWhoRide.text == getString(R.string.option_other)) {
                    if (checkValidation()) {
                        val bundle = Bundle()
                        bundle.putString(Common.TOTAL_COST, textViewEstimatedCost.text.toString())
                        navigator.load(ScheduleRideFragment::class.java).setBundle(bundle).replace(true)
                    }
                } else {
                    val bundle = Bundle()
                    bundle.putString(Common.TOTAL_COST, textViewEstimatedCost.text.toString())
                    navigator.load(ScheduleRideFragment::class.java).setBundle(bundle).replace(true)
                }

            }

            R.id.imageViewFlag -> {
                openCountryCode()
            }

            R.id.textViewCountryCode -> {
                openCountryCode()
            }

            R.id.textViewWhoRide -> {

                PopupViews.openPupupMenu(context, textViewWhoRide, R.menu.menu_1) { id ->
                    if (id == R.id.itemOther) {
                        ridBy = "Other"
                        textViewWhoRide.text = getString(R.string.other)
                        TransitionManager.beginDelayedTransition(constraintLayoutRoot, Fade(1))

                        group2.visibility = ConstraintLayout.VISIBLE
                    } else if (id == R.id.itemMe) {
                        ridBy = "Me"
                        textViewWhoRide.text = getString(R.string.label_me)
                        TransitionManager.beginDelayedTransition(constraintLayoutRoot, Fade(2))

                        group2.visibility = ConstraintLayout.GONE
                    }


                }
            }

            R.id.textViewPaymentMethod -> {

                PopupViews.openPupupMenu(context, textViewWhoRide, R.menu.menu_2) { id ->
                    when (id) {
                        R.id.itemCash -> {
                            textViewPaymentMethod.text = getString(R.string.option_c_cash)
                            VictoriaCustomer.getRideData().payment_mode = "cash"
                        }
                        R.id.itemCard -> {
                            textViewPaymentMethod.text = getString(R.string.label_card)
                            VictoriaCustomer.getRideData().payment_mode = "card"
                        }
                        R.id.itemWallet -> {
                            textViewPaymentMethod.text = getString(R.string.option_c_wallet)
                            VictoriaCustomer.getRideData().payment_mode = "wallet"
                        }
                    }
                }
            }

            R.id.buttonPromoApply -> {

                if (editTextPromocode.text!!.isEmpty()) {
                    showMessage(getString(R.string.validation_empty_promocode))

                } else {

                    VictoriaCustomer.getRideData().promocode = editTextPromocode.text.toString()
                    verifyPromocode()

                    // showMessage(getString(R.string.success_promocode))
                }
            }

            R.id.imageRemove -> {

                editTextPromocode.isEnabled = true
                editTextPromocode.text?.clear()
                imageRemove.visibility = View.GONE
                buttonPromoApply.visibility = View.VISIBLE
                VictoriaCustomer.getRideData().promocode = ""

            }
        }
    }

    private fun checkValidation(): Boolean {

        try {
            validator.submit(editTextPhoneNumber).checkEmpty().errorMessage(getString(R.string.validation_empty_phone)).checkMinDigits(6).errorMessage(getString(R.string.validation_valid_phone_number)).check()

            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showSnackBar(e.message)
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun openCountryCode() {
        var countryCodeDialog = CountryCodeDialog()
        countryCodeDialog.setCallback(ISelectCountry { countryCode, country, id ->
            textViewCountryCode.text = "+$countryCode"
            imageViewFlag.setImageResource(CountryUtils.getFlagDrawableResId(id))

            VictoriaCustomer.getRideData().guest_country_code = countryCode

        })
        countryCodeDialog.show(fragmentManager, "")
    }

    private fun drawPickUpPath() {
        try {

            val driverCurrent = LatLng(VictoriaCustomer.getRideData().dropoff_latitude?.toDouble()!!,
                    VictoriaCustomer.getRideData().dropoff_longitude?.toDouble()!!)

            val latLng = LatLng(VictoriaCustomer.getRideData().pickup_latitude?.toDouble()!!,
                    VictoriaCustomer.getRideData().pickup_longitude?.toDouble()!!)
            drawRoute?.drawPath(Arrays.asList(driverCurrent, latLng),
                    R.drawable.pickup_big, R.drawable.dropoff_big, object : DrawRoute.OnDrawComplete {
                override fun onComplete() {

                }
            })
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }


    /**===============API CALLING==========
     * API : trip/getfare_estimation
     * Mandatory :  vehicle_id,pickup_address,pickup_latitude,pickup_longitude,dropoff_address,dropoff_latitude,dropoff_longitude
     * Optional :  promocode
     *
     * */


    private fun callGetFareEstimation() {
        navigator.toggleLoader(true)
        fareEstimationViewModel.getFareEstimation(VictoriaCustomer.getRideData())
    }


    private fun verifyPromocode() {
        navigator.toggleLoader(true)
        fareEstimationViewModel.verifyPromocode(VictoriaCustomer.getRideData())
    }

    private fun placeOrderApi() {
        navigator.toggleLoader(true)
        VictoriaCustomer.getRideData().rideby = ridBy
        fareEstimationViewModel.placeOrder(VictoriaCustomer.getRideData())
    }

    private fun setObserver() {

        fareEstimationViewModel.fareEstimationLiveData.observe(this@FareEstimationFragment, onChange = {
            navigator.toggleLoader(false)
            when (it.responseCode) {

                URLFactory.ResponseCode.SUCCESS -> {

                    init(it.data)
                }
            }

        }, onError = {

            true
        })


        fareEstimationViewModel.verifyPromocodeLiveData.observe(this@FareEstimationFragment, onChange = {
            navigator.toggleLoader(false)
            showSnackBar(it.message)

            when (it.responseCode) {

                URLFactory.ResponseCode.SUCCESS -> {

                    VictoriaCustomer.getRideData().promocode = editTextPromocode.text.toString()

                    editTextPromocode.isEnabled = false
                    buttonPromoApply.visibility = View.INVISIBLE
                    imageRemove.visibility = View.VISIBLE

                    fareEstimationViewModel.getFareEstimation(VictoriaCustomer.getRideData())

                }
                else -> {
                    VictoriaCustomer.getRideData().promocode = ""
                }

            }
        }, onError = {

            true
        })


        fareEstimationViewModel.placeOrderLiveData.observe(this@FareEstimationFragment, onChange = {
            navigator.toggleLoader(false)
            when (it.responseCode) {
                URLFactory.ResponseCode.INVALID_REQUEST_FAIL_REQUESt -> {
                    showMessage(it.message)
                }
                URLFactory.ResponseCode.SUCCESS -> {
                    fareEstimationViewModel.placeOrderLiveData.removeObservers(this)
                    VictoriaCustomer.setTripData(it.data)

                    val parameter = Parameter()
                    parameter.tripId = VictoriaCustomer.getTripData().tripId.toString()
                    fareEstimationViewModel.recurringLiveData(parameter)
                    navigator.load(CarAllocationFragment::class.java).replace(true)
                }
            }

        }, onError = {

            true
        })

        fareEstimationViewModel.recurringLiveData.observe(this@FareEstimationFragment, onChange = {
            when (it.responseCode) {
                URLFactory.ResponseCode.INVALID_REQUEST_FAIL_REQUESt -> {
                    showMessage(it.message)
                }
                URLFactory.ResponseCode.SUCCESS -> {
                    //navigator.load(CarAllocationFragment::class.java).replace(true)
                }
            }
        }, onError = {

            true
        })

    }
}
