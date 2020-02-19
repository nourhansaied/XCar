package com.victoria.customer.ui.home.ride.fragments

import android.arch.lifecycle.ViewModelProviders
import android.view.View
import com.squareup.picasso.Picasso
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.viewmodel.HomeActivityViewModel
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.fragments.HomeStartFragment
import com.victoria.customer.util.CircleTransform
import kotlinx.android.synthetic.main.fragment_review_layout.*


class RatingFragment : BaseFragment() {


    override fun createLayout(): Int {
        return R.layout.fragment_review_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        observeRateAndReviewConfirmation()

        imageViewClose.setOnClickListener(this::onViewClick)
        buttonConfirm.setOnClickListener(this::onViewClick)

        setUserData()

    }

    private fun setUserData() {
        Picasso.get()
                .load(VictoriaCustomer.getTripData().driverData.profileImageThumb)
                .transform(CircleTransform())
                .into(imagViewDriverProfile)

    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.buttonConfirm -> {
                /**
                 * @param : trip_id,rate,ratetouser_id,ratetouser_type[Customer,Driver]
                 * */
                val parameter = Parameter()
                parameter.tripId = VictoriaCustomer.getTripData().tripId.toString()
                parameter.rate = appCompatRatingBar2.rating.toString()
                parameter.ratetouser_id = VictoriaCustomer.getTripData().driverData.driverId.toString()
                parameter.ratetouser_type = Common.DRIVER

                if (!editTextCommentBox.text.isNullOrEmpty()) {
                    parameter.comment = editTextCommentBox.text.toString()
                }
                navigator.toggleLoader(true)
                homeActivityViewModel.rateAndReviewApi(parameter)
            }

            R.id.imageViewClose -> {
                navigator.load(HomeStartFragment::class.java).replace(false)
            }

        }
    }

    private fun observeRateAndReviewConfirmation() {
        homeActivityViewModel.rateAndReview.observe(this, onChange = {
            navigator.toggleLoader(false)
            if (it.responseCode == 1) {
                showMessage(it.message)
                navigator.load(HomeStartFragment::class.java).replace(false)
            } else {
                showMessage(it.message)
            }
        }, onError = {
            true
        })
    }

}
