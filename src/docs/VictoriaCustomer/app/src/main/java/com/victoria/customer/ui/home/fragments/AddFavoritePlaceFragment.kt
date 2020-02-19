package com.victoria.customer.ui.home.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.victoria.customer.R
import com.victoria.customer.core.Common.RequestCode.PLACE_AUTOCOMPLETE_REQUEST_CODE
import com.victoria.customer.data.pojo.FavouriteAddress
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.error.ExceptionHandler.TAG
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.adapter.FavoritePlaceAdapter
import kotlinx.android.synthetic.main.fragment_add_favorite_place.*
import kotlinx.android.synthetic.main.toolbar_with_close.*
import java.util.*


class AddFavoritePlaceFragment : BaseFragment() {

    override fun createLayout(): Int {
        return com.victoria.customer.R.layout.fragment_add_favorite_place
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {

        toolBarText.text = ""

        if (!Places.isInitialized()) {
            Places.initialize(activity!!, getString(R.string.browser_key))
        }

        setAdapter()

        textViewAddFavouritePlace.setOnClickListener(this::onViewClick)
        imageViewBack.setOnClickListener(this::onViewClick)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            com.victoria.customer.R.id.textViewAddFavouritePlace -> {
                openPlacePicker()
            }

            com.victoria.customer.R.id.imageViewBack -> navigator.goBack()
        }
    }

    private fun setAdapter() {
        val favoritePlaceAdapter = FavoritePlaceAdapter(context!!, ArrayList(),object :FavoritePlaceAdapter.CallbackFavPlace{
            override fun selectedFavouriteAddress(item: FavouriteAddress) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun deleteFavouriteAddress(item: FavouriteAddress) {

            }
        })
        recyclerViewFavoritePlace.adapter = favoritePlaceAdapter
    }

    private fun openPlacePicker() {
        try {

            // Set the fields to specify which types of place data to return.
            val fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS)

            // Start the autocomplete intent.
            val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(activity!!)
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)


         /*   val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN)
                    .setBoundsBias(LatLngBounds(LatLng(Common.latitudeCurrent, Common.longitudeCurrent)
                            , LatLng(Common.latitudeCurrent, Common.longitudeCurrent))).build(activity!!)
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE)*/
        } catch (e: GooglePlayServicesRepairableException) {
        } catch (e: GooglePlayServicesNotAvailableException) {
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activity != null) {
            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                when (resultCode) {
                    RESULT_OK -> {
                        val place = Autocomplete.getPlaceFromIntent(data!!)
                        Log.i(TAG, "Place: " + place.name + ", " + place.id)
                        // navigator.goBack()
                    }

                    AutocompleteActivity.RESULT_ERROR-> {
                        val status = Autocomplete.getStatusFromIntent(data!!)
                        Log.i(TAG, status.statusMessage)
                    }

                    RESULT_CANCELED -> {

                    }
                }
            }
        }
    }


}
