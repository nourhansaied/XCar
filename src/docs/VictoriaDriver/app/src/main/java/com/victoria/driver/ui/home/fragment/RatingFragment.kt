package com.victoria.driver.ui.home.fragment


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.squareup.picasso.Picasso
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.di.App
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeActivityViewModel
import com.victoria.driver.util.CircleTransform
import kotlinx.android.synthetic.main.fragment_review_layout.*


class RatingFragment : BaseFragment() {


    override fun createLayout(): Int {
        return R.layout.fragment_review_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {

        imageViewClose.setOnClickListener(this::onViewClick)
        buttonConfirm.setOnClickListener(this::onViewClick)

        setUserData()
    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_layout, container, false)
    }

    private fun setUserData() {
        Picasso.get()
                .load(App.getTripData().customerData.profileImageThumb)
                .transform(CircleTransform())
                .into(imagViewDriverProfile)

    }

    override fun onResume() {

        super.onResume()
        activity!!.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    override fun onDestroy() {
        super.onDestroy()
        activity!!.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    override fun onViewClick(view: View) {

        when (view.id) {

            R.id.buttonConfirm -> {
                observeRateAndReviewConfirmation()
            }

            R.id.imageViewClose -> {
                navigator.load(HomeFragment::class.java).clearHistory("").replace(false)
            }

        }
    }

    private fun observeRateAndReviewConfirmation() {
        homeActivityViewModel.rateAndReview.observe(this, onChange = {
            homeActivityViewModel.rateAndReview.removeObservers(this)
            navigator.toggleLoader(false)
            if (it.responseCode == 1) {

                showMessage(it.message)
                navigator.load(HomeFragment::class.java).clearHistory("").replace(false)
            } else {
                showMessage(it.message)
            }
        }, onError = {
            true
        })

        val parameter = Parameter()
        parameter.tripId = App.getTripData().tripId.toString()
        parameter.rate = appCompatRatingBar2.rating.toString()
        parameter.ratetouser_id = App.getTripData().customerData.customerId.toString()
        parameter.ratetouser_type = Common.CUSTOMER
        navigator.toggleLoader(true)
        homeActivityViewModel.rateAndReviewApi(parameter)
    }


}
