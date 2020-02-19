package com.victoria.customer.ui.home.fragments

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Handler
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ListView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.internal.it
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.SphericalUtil
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Validator
import com.victoria.customer.core.map_route.MyGeocoder
import com.victoria.customer.data.URLFactory
import com.victoria.customer.data.pojo.FavouriteAddress
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.error.ExceptionHandler
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.model.FavouritesAddressParam
import com.victoria.customer.model.Parameter
import com.victoria.customer.model.PlaceModel
import com.victoria.customer.model.VehicleData
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.adapter.FavoritePlaceAdapter
import com.victoria.customer.ui.home.adapter.PlaceAutocompleteAdapter
import com.victoria.customer.ui.home.dialog.DialogSelectAddressType
import com.victoria.customer.ui.home.viewmodel.SelectLocationViewModel
import com.victoria.customer.ui.interfaces.ItemClickListener
import com.victoria.customer.util.AnchorBottomSheetBehavior
import com.victoria.customer.util.EditTextDebounce
import com.victoria.customer.util.LocationManager
import kotlinx.android.synthetic.main.fragmeny_select_location.*
import kotlinx.android.synthetic.main.toolbar_with_close.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class SelectLocationFragment : BaseFragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener,
        ItemClickListener, GoogleApiClient.OnConnectionFailedListener {

    @Inject
    lateinit var validator: Validator

    /*@Inject
    lateinit var locationManager: LocationManager*/

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    private lateinit var myGeocoder: MyGeocoder
    private lateinit var myCurrent: String

    private lateinit var center: LatLng
    private var isPickupFocusable = true
    private var isBottomSheetOpen = true
    private var isHomeSelected = false
    private var isFirst = false
    private var dataAdapter: PlaceAutocompleteAdapter? = null
    private var recylerViewSearch: ListView? = null

    lateinit var layoutManager: LinearLayoutManager

    private lateinit var placeModels: List<PlaceModel>
    private lateinit var favoritePlaceAdapter: FavoritePlaceAdapter
    lateinit var mGoogleApiClient: GoogleApiClient

    private var addressList: MutableList<FavouriteAddress>? = null

    lateinit var favAddressParameterName: FavouritesAddressParam

    override fun createLayout(): Int {
        return R.layout.fragmeny_select_location
    }


    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val selectLocationViewModel: SelectLocationViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SelectLocationViewModel::class.java]
    }

    override fun bindData() {

        setGoogleApiClient()

        navigator.toggleLoader(true)

        recylerViewSearch = view?.findViewById(R.id.recylerViewSearch) as ListView
        toolBarText.text = ""

        layoutManager = activity?.let { LinearLayoutManager(it) }!!

        recyclerViewAddress.layoutManager = layoutManager
        addressList = ArrayList<FavouriteAddress>() as MutableList<FavouriteAddress>?

        favAddressParameterName = FavouritesAddressParam()
        myGeocoder = MyGeocoder(context)

        /**==Get FavouriteAddress List===*/
        selectLocationViewModel.getFavouriteList()

        setAdapter()

        openBottomSheetLocationSelection()

        loadMap()

        if (!Places.isInitialized()) {
            Places.initialize(activity!!, getString(R.string.browser_key))
        }

        /*if (VictoriaCustomer.getRideData().carType == 4) {


            editTextDropoffLocation.isEnabled = false
            editTextDropoffLocation.setText(getString(R.string.service_4))
            editTextDropoffLocation.setSelectAllOnFocus(false)
        }*/

        designBottomSheet.setOnTouchListener { p0, p1 ->

            closeBottomSheet()
            true
        }
        constraintLayoutBottomSheet.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    hideKeyBoard()
                }
            }
            return@setOnTouchListener true
        }

        editTextPickupLocation.setOnFocusChangeListener { view, b ->

            when {
                !b -> {
                    isPickupFocusable = false
                    hideKeyBoard()
                }
                else -> isPickupFocusable = true
            }

            when {
                !isBottomSheetOpen -> openBottomSheetLocationSelection()
                else -> {
                    recylerViewSearch?.visibility = View.GONE
                    dataAdapter?.clear()
                    constraintLayoutBottomSheet?.visibility = View.VISIBLE
                }
            }
        }

        editTextDropoffLocation.setOnFocusChangeListener { view, b ->
            when {
                !b -> {
                    isPickupFocusable = true
                    hideKeyBoard()
                }
                else -> {
                    isPickupFocusable = false
                    Handler().postDelayed({
                        activity.run {
                            showKeyBoard()
                        }
                    }, 300)
                }
            }

            if (getView() != null) {
                when {
                    !isBottomSheetOpen -> openBottomSheetLocationSelection()
                    else -> {
                        recylerViewSearch?.visibility = View.GONE
                        dataAdapter?.clear()
                        constraintLayoutBottomSheet.visibility = View.VISIBLE
                    }
                }
            }
        }


        editTextPickupLocation.setupClearButtonWithActionPickup()
        editTextDropoffLocation.setupClearButtonWithActionDropoff()
        editTextPickupLocation.setOnClickListener { onViewClick(it) }
        editTextDropoffLocation.setOnClickListener { onViewClick(it) }
        imageViewBack.setOnClickListener { onViewClick(it) }
        buttonGoNext.setOnClickListener { onViewClick(it) }
        textViewAddFavouritePlace.setOnClickListener { onViewClick(it) }
        textViewLocationOnmap.setOnClickListener { onViewClick(it) }

    }

    private fun setGoogleApiClient() {

        mGoogleApiClient = GoogleApiClient.Builder(this.activity!!)
                .enableAutoManage(this.activity!! /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .build()
    }

    override fun onPause() {
        super.onPause()
        activity?.let { mGoogleApiClient.stopAutoManage(it) }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }


    private fun setAdapter() {
        placeModels = ArrayList()

        dataAdapter = PlaceAutocompleteAdapter(context, R.layout.row_place_auto_search_layout, placeModels, this)
        recylerViewSearch?.adapter = dataAdapter
    }

    private fun setFavoritePlaceAdapter() {


        favoritePlaceAdapter = FavoritePlaceAdapter(activity!!, addressList!!, object : FavoritePlaceAdapter.CallbackFavPlace {
            override fun selectedFavouriteAddress(item: FavouriteAddress) {
                isHomeSelected = true
                if (isPickupFocusable) {

                    editTextPickupLocation?.setText(item.address)
                    VictoriaCustomer.getRideData().pickup_address = item.address
                    VictoriaCustomer.getRideData().pickup_latitude = item.latitude
                    VictoriaCustomer.getRideData().pickup_longitude = item.longitude

                } else {

                    editTextDropoffLocation?.setText(item.address)
                    VictoriaCustomer.getRideData().dropoff_address = item.address
                    VictoriaCustomer.getRideData().dropoff_latitude = item.latitude
                    VictoriaCustomer.getRideData().dropoff_longitude = item.longitude

                }

            }

            override fun deleteFavouriteAddress(item: FavouriteAddress) {

                val favParam = FavouritesAddressParam()
                favParam.address_id = item.addressId.toString()
                selectLocationViewModel.removeFavouriteAddress(favParam)
            }
        })
        recyclerViewAddress.adapter = favoritePlaceAdapter

    }

    private fun loadMap() {

        mapFragment = SupportMapFragment()
        this.childFragmentManager.beginTransaction().replace(R.id.frameLayoutMapSelectLocation, mapFragment).commit()
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        MapsInitializer.initialize(context)
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isCompassEnabled = false
        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(latLng: LatLng) {
                Common.latitudeCurrent = latLng.latitude
                Common.longitudeCurrent = latLng.longitude
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                if (view != null) {
                    if (!isPickupFocusable) {
                        imageViewCenterPin.setImageResource(R.drawable.dropoff_big)
                    } else {
                        imageViewCenterPin.setImageResource(R.drawable.pickup_big)
                    }
                }

                locationManager.stop()

                hideKeyBoard()

                setAddressLocation(latLng.latitude, latLng.longitude, Common.IS_PICKUP,
                        false, true)
                //editTextDropoffLocation.clearFocus()
                setObserver()

            }

            override fun onFail(status: LocationManager.LocationListener.Status) {
                setObserver()
            }
        })


        googleMap.setOnCameraIdleListener(this)
    }

    override fun onCameraIdle() {
        center = googleMap.cameraPosition.target
        if (center != null) {
            if (!isFirst) {
                if (isPickupFocusable) {
                    setAddressLocation(center.latitude, center.longitude, Common.IS_PICKUP, true, false)
                } else {
                    setAddressLocation(center.latitude, center.longitude, Common.IS_DROPOFF, true, false)
                }
            }
        }
    }

    private fun setAddressLocation(latitude: Double, longitude: Double, isPickup: Int,
                                   isDrag: Boolean, isFirstTIme: Boolean) {
        if (isFirstTIme)
            navigator.toggleLoader(false)
        try {
            myCurrent = getAddress(latitude, longitude)
            if (myCurrent != "" && isPickup == Common.IS_PICKUP) {

                editTextPickupLocation?.setText(myCurrent)
                VictoriaCustomer.getRideData().pickup_address = editTextPickupLocation?.text.toString()
                VictoriaCustomer.getRideData().pickup_latitude = latitude.toString()
                VictoriaCustomer.getRideData().pickup_longitude = longitude.toString()
                isFirst = true

                if (editTextPickupLocation != null) {
                    editTextPickupLocation?.clearFocus()
                    editTextDropoffLocation?.requestFocus()
                    editTextDropoffLocation?.isFocusable = true
                    editTextDropoffLocation?.isFocusableInTouchMode = true
                    isPickupFocusable = false
                }

            } else {
                if (!isFirstTIme) {
                    isPickupFocusable = false

                    editTextDropoffLocation?.setText(myCurrent)
                    VictoriaCustomer.getRideData().dropoff_address = editTextDropoffLocation?.text.toString()
                    VictoriaCustomer.getRideData().dropoff_latitude = latitude.toString()
                    VictoriaCustomer.getRideData().dropoff_longitude = longitude.toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    override fun onViewClick(view: View) {
        //  super.onViewClick(view)
        when (view.id) {

            R.id.imageViewBack -> {
                navigator.goBack()
            }

            R.id.editTextPickupLocation -> {
                editTextPickupLocation?.isCursorVisible = true
                openBottomSheetLocationSelection()
            }
            R.id.editTextDropoffLocation -> {

                editTextDropoffLocation?.isCursorVisible = true
                openBottomSheetLocationSelection()


            }
            R.id.buttonGoNext -> {
                if (editTextPickupLocation.text.toString().trim().isNotEmpty()) {
                    if (editTextDropoffLocation.text.toString().trim().isNotEmpty()) {
                        navigator.toggleLoader(true)
                        selectLocationViewModel.nearbyDriverListing(getNearByDriverData())
                    } else {
                        showMessage(getString(R.string.validation_empty_dropoff_location))
                    }
                } else {
                    showMessage(getString(R.string.validation_empty_pick_up))
                }
            }

            R.id.textViewLocationOnmap -> {
                hideKeyBoard()
                closeBottomSheet()
            }
            R.id.textViewAddFavouritePlace -> {
                hideKeyBoard()
                openPlacePicker()
            }

        }
    }

    private fun getNearByDriverData(): Parameter {
        val parameter = Parameter()

        parameter.latitude = VictoriaCustomer.getRideData().pickup_latitude!!
        parameter.longitude = VictoriaCustomer.getRideData().pickup_longitude!!

        return parameter
    }


    private fun checkValidation(): Boolean {

        try {

            validator.submit(editTextPickupLocation).checkEmpty().errorMessage(getString(R.string.validation_empty_pickup)).check()
            validator.submit(editTextDropoffLocation).checkEmpty().errorMessage(getString(R.string.validation_empty_dropoff)).check()

            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showSnackBar(e.message)
        }
        return false
    }

    private fun AppCompatEditText.setupClearButtonWithActionPickup() {


        EditTextDebounce.create(editTextPickupLocation).watch({ result ->
            if (view != null) {
                val clearIcon = if (result.isNotEmpty()) R.drawable.cancel_smallest else 0

                if (Locale.getDefault().language == "ar") {
                    setCompoundDrawablesWithIntrinsicBounds(clearIcon, 0, 0, 0)
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
                }

                // isPickupFocusable = true

                editTextPickupLocation.compoundDrawablePadding = resources.getDimension(R.dimen.dp_8).toInt()

            }

            if (view != null || !isHomeSelected) {

                if (isFirst || result.length < 4) {

                    recylerViewSearch?.visibility = View.GONE
                    constraintLayoutBottomSheet.visibility = View.VISIBLE
                    isFirst = false
                } else {

                    recylerViewSearch?.visibility = View.VISIBLE
                    constraintLayoutBottomSheet.visibility = View.GONE
                }

                //isHomeSelected = false
                dataAdapter?.filter?.filter(result)

            }


        }, 300)


        setOnTouchListener(View.OnTouchListener { _, event ->

            isHomeSelected = false

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    this.setText("")

                    return@OnTouchListener true
                } else if (Locale.getDefault().language == "ar") {
                    if (event.rawX >= (this.left - this.compoundPaddingRight)) {
                        this.setText("")

                        return@OnTouchListener true
                    }
                }
            }
            return@OnTouchListener false
        })
    }

    private fun AppCompatEditText.setupClearButtonWithActionDropoff() {


        EditTextDebounce.create(editTextDropoffLocation).watch({ result ->

            if (view != null) {
                val clearIcon = if (result.isNotEmpty()) R.drawable.cancel_smallest else 0
                if (Locale.getDefault().language == "ar") {
                    setCompoundDrawablesWithIntrinsicBounds(clearIcon, 0, 0, 0)
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(0, 0, clearIcon, 0)
                }
                editTextDropoffLocation.compoundDrawablePadding = resources.getDimension(R.dimen.dp_8).toInt()
                //  isPickupFocusable = false

                //if (VictoriaCustomer.getRideData().carType != 4)

            }

            if (view != null || !isHomeSelected) {

                dataAdapter?.filter?.filter(result)

                if (result.length < 4) {
                    recylerViewSearch?.visibility = View.GONE
                    constraintLayoutBottomSheet?.visibility = View.VISIBLE
                } else {
                    recylerViewSearch?.visibility = View.VISIBLE
                    constraintLayoutBottomSheet?.visibility = View.GONE
                }


                // isHomeSelected = false

                //dataAdapter?.notifyDataSetChanged()
            }


        }, 300)



        setOnTouchListener(View.OnTouchListener { _, event ->
            isHomeSelected = false

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                    this.setText("")
                    return@OnTouchListener true
                } else if (Locale.getDefault().language == "ar") {
                    if (event.rawX >= (this.left - this.compoundPaddingRight)) {
                        this.setText("")
                        return@OnTouchListener true
                    }
                }
            }
            return@OnTouchListener false
        })
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val address: String  // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        val addresses: List<Address>
        addresses = geocoder.getFromLocation(latitude, longitude, 5)
        address = if (addresses.isNotEmpty()) addresses[0].getAddressLine(0)
        else ""

        return address
    }

    private var vehicleData: VehicleData? = null

    private fun openAnotherScreen() {
        if (checkValidation()) {
            VictoriaCustomer.getRideData().pickup_address = editTextPickupLocation?.text.toString()
            VictoriaCustomer.getRideData().pickup_dropoff = true
            VictoriaCustomer.getRideData().dropoff_address = editTextDropoffLocation?.text.toString()


            /**===========Changed by khushbu _02_07_19 : As per client's feedback on normal ride changed the flow ============*/
            /*if (VictoriaCustomer.getRideData().isCarSelected) {
                var bundle = Bundle()
                bundle.putParcelable(Common.VEHICLE_DATA, vehicleData)
                navigator.load(FareEstimationFragment::class.java).setBundle(bundle).replace(false)
            } else {
                if (arguments != null) {
                    if (arguments!!.containsKey(Common.VEHICLE_DATA)) {
                        vehicleData = arguments!!.getParcelable(Common.VEHICLE_DATA)
                    }
                }
                if (vehicleData != null) {
                    val bundle = Bundle()
                    bundle.putParcelable(Common.VEHICLE_DATA, vehicleData)
                    navigator.load(HomeFragment::class.java).setBundle(bundle).replace(false)
                } else {
                    navigator.load(HomeFragment::class.java).replace(false)
                }

            }*/

            navigator.load(FareEstimationFragment::class.java).replace(false)


        }

    }

    override fun onItemEventFired(result: String?, pos: Int) {
        if (!isPickupFocusable) {
            editTextDropoffLocation.setText(result)
            VictoriaCustomer.getRideData().dropoff_address = result
            setLatLng(false, pos)
            dataAdapter?.clear()
            hideKeyBoard()
            editTextPickupLocation.isCursorVisible = false
            editTextPickupLocation.setSelectAllOnFocus(false)
            editTextDropoffLocation.setSelectAllOnFocus(false)
        } else {
            editTextPickupLocation.setText(result)
            VictoriaCustomer.getRideData().dropoff_address = result
            setLatLng(true, pos)
            dataAdapter?.clear()
        }

        Handler().postDelayed({
            constraintLayoutBottomSheet.visibility = View.VISIBLE
            recylerViewSearch?.visibility = View.GONE
            dataAdapter?.clear()
            editTextDropoffLocation.clearFocus()
        }, 350)

    }

    private fun setLatLng(isPickUp: Boolean, pos: Int) {
        val str = dataAdapter?.resultList!![pos].heading + " , " + dataAdapter?.resultList!![pos].subTitle

        val place_id = dataAdapter?.resultList!![pos].placeId
        // DataToPref.setSharedPreferanceData(getApplicationContext(), HiOscarConstant.SELECTED_PLACE_ID, HiOscarConstant.SELECTED_PLACE_ID_KEY, placeModels.get(position).getPlaceID() + "");
        val coder = Geocoder(context)
        val address: List<Address>?
        var data = ""

        try {
            // address = coder.getFromLocationName(strAddress, 5);
            address = coder.getFromLocationName(str, 15)
            if (address == null || address.size <= 0) {
            }
            val location = address[0]
            if (isPickUp) {
                VictoriaCustomer.getRideData().pickup_latitude = location.latitude.toString()
                VictoriaCustomer.getRideData().pickup_longitude = location.longitude.toString()
            } else {
                VictoriaCustomer.getRideData().dropoff_latitude = location.latitude.toString()
                VictoriaCustomer.getRideData().dropoff_longitude = location.longitude.toString()
            }

        } catch (ex: Exception) {

            ex.printStackTrace()
        }
    }

    /*val placeResult: PendingResult<PlaceBuffer> = com.google.android.gms.location.places.Places.GeoDataApi
            .getPlaceById(mGoogleApiClient, dataAdapter?.resultList!![pos].placeId)
    placeResult.setResultCallback(
    object : ResultCallback<PlaceBuffer> {
        override fun onResult(places: PlaceBuffer) {
            if (!places.status.isSuccess) {
                places.release()
                return
            }

        }
    })
}*/

    private fun openBottomSheetLocationSelection() {

        //buttonDone.visibility = View.GONE
        isBottomSheetOpen = true

        val behavior = AnchorBottomSheetBehavior.from<FrameLayout>(designBottomSheet)
        behavior.isHideable = false
        behavior.state = AnchorBottomSheetBehavior.STATE_EXPANDED
        behavior.anchorOffset = resources.getDimension(R.dimen.anchor_offset).toInt()
        behavior.allowUserDragging = true

    }

    private fun closeBottomSheet() {

        //buttonDone.visibility = View.VISIBLE
        isBottomSheetOpen = false

        val behavior = AnchorBottomSheetBehavior.from<FrameLayout>(designBottomSheet)
        behavior.isHideable = true
        behavior.allowUserDragging = true
        behavior.state = AnchorBottomSheetBehavior.STATE_HIDDEN

    }


    private fun openPlacePicker() {
        try {

            // Set the fields to specify which types of place data to return.
            val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            val center = LatLng(Common.latitudeCurrent, Common.longitudeCurrent)
            val northSide = SphericalUtil.computeOffset(center, 50000.0, 0.0)
            val southSide = SphericalUtil.computeOffset(center, 50000.0, 180.0)

            val bounds = LatLngBounds.builder()
                    .include(northSide)
                    .include(southSide)
                    .build()

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .setLocationBias(RectangularBounds.newInstance(bounds))
                    .build(activity!!)
            startActivityForResult(intent, Common.RequestCode.PLACE_AUTOCOMPLETE_REQUEST_CODE)

        } catch (e: GooglePlayServicesRepairableException) {
        } catch (e: GooglePlayServicesNotAvailableException) {
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activity != null) {
            if (requestCode == Common.RequestCode.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                when (resultCode) {
                    Activity.RESULT_OK -> {

                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        Log.i(ExceptionHandler.TAG, "Place: " + place.name + ", " + place.address)
                        favAddressParameterName.address = place.address
                        favAddressParameterName.latitude = place.latLng?.latitude.toString()
                        favAddressParameterName.longitude = place.latLng?.longitude.toString()


                        showSelectAddressTypeDialog(place.address)

                        // navigator.goBack()
                    }

                    AutocompleteActivity.RESULT_ERROR -> {
                        val status = Autocomplete.getStatusFromIntent(data!!)
                        Log.i(ExceptionHandler.TAG, status.statusMessage)
                    }

                    Activity.RESULT_CANCELED -> {

                    }
                }
            }
        }
    }

    private fun showSelectAddressTypeDialog(address: String?) {

        val dialogAddressType = DialogSelectAddressType()
        dialogAddressType.setAddressData(object : DialogSelectAddressType.CallBackInterface {
            override fun onAddressTypeSelected(type: Common.AddressType) {

                favAddressParameterName.type = type.addressType
                // favoritePlaceAdapter.setItem(AddressType(address!!,type))
                navigator.toggleLoader(true)
                selectLocationViewModel.addFavouriteAddress(favAddressParameterName)


            }
        }, address!!)

        dialogAddressType.show(fragmentManager, "")

    }


    /**
     * Observer
     * */


    private fun setObserver() {

        selectLocationViewModel.favouriteAddressLiveData.observe(this@SelectLocationFragment, onChange = {
            navigator.toggleLoader(false)
            if (it.responseCode == URLFactory.ResponseCode.SUCCESS) {
                addressList = it.data as MutableList<FavouriteAddress>?

                setFavoritePlaceAdapter()

            }

        }, onError = { throwable: Throwable ->

            true

        })

        selectLocationViewModel.addFavLiveData.observe(this@SelectLocationFragment, onChange = {
            navigator.toggleLoader(false)
            if (it.responseCode == URLFactory.ResponseCode.SUCCESS) {

                selectLocationViewModel.getFavouriteList()
            }

        }, onError = {
            true
        })


        selectLocationViewModel.nearByDriver.observe(this@SelectLocationFragment, onChange = {
            navigator.toggleLoader(false)
            when (it.responseCode) {

                URLFactory.ResponseCode.SUCCESS -> {
                    openAnotherScreen()
                }
                else -> showSnackBar(it.message)
            }


        }, onError = {

            true
        })
    }


}
