package com.victoria.customer.ui.home.ride_history.fragments

import android.arch.lifecycle.ViewModelProviders
import android.view.View
import com.squareup.picasso.Picasso
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.PastRide
import com.victoria.customer.data.pojo.User
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.viewmodel.HomeActivityViewModel
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.util.CircleTransform
import com.victoria.customer.util.DateTimeFormatter
import kotlinx.android.synthetic.main.fragment_completed_ride_details_layout.*

import kotlinx.android.synthetic.main.toolbar_with_close.*

class PastRideDetailFragment : BaseFragment() {

    lateinit var pastRide: PastRide

    override fun createLayout(): Int {
        return R.layout.fragment_completed_ride_details_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun bindData() {
        observeUserDetailResponse()
        toolBarText.text = getString(R.string.toolbar_title_completed_ride_detail)

        val parameter = Parameter()
        parameter.user_type = Common.DRIVER
        parameter.userId = pastRide.driverId.toString()
        navigator.toggleLoader(true)
        homeActivityViewModel.userDetailApiCall(parameter)
        imageViewBack.setOnClickListener(this::onViewClick)
    }

    private fun setUserData(data: User?) {
        textViewRideDateTime.text = DateTimeFormatter.utcToLocal(pastRide.tripdatetime, Common.DATE_TIME_FORMAT_UTC, Common.DATE_TIME_FORMAT_OUT_THREE)
        textViewTotalCost.text = getString(R.string.label_curruncy_sar) + " " + pastRide.totalAmount
        textViewPickup.text = pastRide.pickupAddress
        textViewDropoff.text = pastRide.dropoffAddress
        textViewEstimatedKm.text = pastRide.distance.toString() + " km"
        textViewEstimatedDuration.text = pastRide.totalTime.toString() + " min"

        textViewName.text = session.user?.name
        textViewPhoneNumber.text = session.user?.phone
        textViewDriverName.text = data?.name
        Picasso.get()
                .load(data?.profileImageThumb)
                .transform(CircleTransform())
                .into(imageViewDriver)
        textViewPhoneNumber.text = data?.phone
        textViewRating.text = (String.format("%.2f", data?.rating))
        textViewCarType.text = data?.vehicle_brand + " " + data?.vehicle_model + " - " + data?.vehicle_number
    }

    override fun onViewClick(view: View) {
        when (view.id) {
            R.id.imageViewBack -> {
                activity!!.onBackPressed()
            }
        }
    }

    fun passRideDetail(pastRide: PastRide) {
        this.pastRide = pastRide
    }

    private fun observeUserDetailResponse() {
        homeActivityViewModel.userData.observe(this, onChange = {
            navigator.toggleLoader(false)
            if (it.responseCode == 1) {
                setUserData(it.data)
            } else {
                showMessage(it.message)
            }
        }, onError = {
            true
        })
    }
}
