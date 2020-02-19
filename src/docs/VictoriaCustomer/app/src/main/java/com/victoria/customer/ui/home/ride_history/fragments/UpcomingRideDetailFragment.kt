package com.victoria.customer.ui.home.ride_history.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.data.pojo.PastRide
import com.victoria.customer.data.pojo.User
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.viewmodel.HomeActivityViewModel
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.ride.fragments.CancelRideFragment
import com.victoria.customer.util.DateTimeFormatter
import kotlinx.android.synthetic.main.fragment_upcoming_ride_details_layout.*
import kotlinx.android.synthetic.main.toolbar_with_close.*


class UpcomingRideDetailFragment : BaseFragment() {

    lateinit var pastRide: PastRide

    override fun createLayout(): Int {
        return R.layout.fragment_upcoming_ride_details_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun bindData() {
        observeUserDetailResponse()

        textViewCancel.setOnClickListener(this::onViewClick)
        imageViewBack.setOnClickListener(this::onViewClick)

        val parameter = Parameter()
        parameter.user_type = Common.CUSTOMER
        parameter.userId = pastRide.customerId.toString()

        homeActivityViewModel.userDetailApiCall(parameter)

        toolBarText.text = getString(R.string.toolbar_title_upcoming_ride_detail)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_upcoming_ride_details_layout, container, false)
    }

    private fun setUserData(data: User?) {
        textViewRideDateTime.text = DateTimeFormatter.utcToLocal(pastRide.tripdatetime, Common.DATE_TIME_FORMAT_UTC, Common.DATE_TIME_FORMAT_OUT_THREE)
        textViewPickup.text = pastRide.pickupAddress
        textViewDropoff.text = pastRide.dropoffAddress
        textViewEstimatedKm.text = "Est. " + pastRide.distance.toString() + " km"
        textViewEstimatedDuration.text = "Est. " + pastRide.totalTime.toString() + " min"


        textViewName.text = data?.name
        textViewPhoneNumber.text = data?.phone
    }

    override fun onViewClick(view: View) {
        when (view.id) {
            R.id.imageViewBack -> {
                navigator.goBack()
            }

            R.id.textViewCancel -> {
                var bundel: Bundle = Bundle()
                bundel.putString("ComingFrom", UpcomingRideFragment::class.java.name)
                bundel.putString(Common.TRIP_ID, pastRide.id.toString())
                navigator.load(CancelRideFragment::class.java).setBundle(bundel).replace(true)
            }
        }
    }

    fun passRideDetail(pastRide: PastRide) {
        this.pastRide = pastRide
    }

    private fun observeUserDetailResponse() {
        homeActivityViewModel.userData.observe(this, onChange = {
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
