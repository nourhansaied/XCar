package com.victoria.driver.ui.home.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.driver.R
import com.victoria.driver.di.App
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.HomeActivityViewModel
import com.victoria.driver.util.DateTimeFormatter
import kotlinx.android.synthetic.main.fragment_receipt_layout.*

class ReceiptFragment : BaseFragment() {

    var myRideId = ""

    override fun createLayout(): Int {
        return R.layout.fragment_receipt_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        myRideId = App.getTripData().tripId.toString()

        buttonConfirm.setOnClickListener(this::onViewClick)

        if (arguments != null && arguments!!.containsKey(com.victoria.driver.core.Common.TRIP_ID)) {
            observeRateAndReviewConfirmation()
        } else {
            setUserData()
        }

    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    private fun setUserData() {
        textViewUserName.text = getString(R.string.label_hello) + App.getTripData().customerData.firstName
        appCompatTextView2.text = App.getTripData().customerData.name
        textViewPickup.text = App.getTripData().pickupAddress
        textViewDropoff.text = App.getTripData().dropoffAddress
        textViewDate.text = DateTimeFormatter.utcToLocal(App.getTripData().tripdatetime, com.victoria.driver.core.Common.DATE_TIME_FORMAT_UTC, com.victoria.driver.core.Common.DATE_TIME_FORMAT_OUT)
        textViewTime.text = DateTimeFormatter.utcToLocal(App.getTripData().tripdatetime, com.victoria.driver.core.Common.DATE_TIME_FORMAT_UTC, com.victoria.driver.core.Common.DATE_TIME_FORMAT_OUT_TWO)

        textViewDistance.text = App.getTripData().distance.toString() + " km"
        textViewDuration.text = App.getTripData().totalTime.toString() + " min"
        textViewSubTotal.text = getString(R.string.label_curruncy_sar) + " " + App.getTripData().totalAmount.toString()
        textViewPromocode.text = "-" + getString(R.string.label_curruncy_sar) + " " + App.getTripData().tripDetails.promocodeAmount.toString()

        if (App.getTripData().customerData.wallet > App.getTripData().totalAmount
                - App.getTripData().tripDetails.promocodeAmount) {
            textViewWallet.text = "-" + getString(R.string.label_curruncy_sar) + " " + (App.getTripData().totalAmount - App.getTripData().tripDetails.promocodeAmount).toString()
            textViewTotalCost.text = getString(R.string.label_curruncy_sar) + " " + (App.getTripData().totalAmount
                    - App.getTripData().tripDetails.promocodeAmount).toString()
        } else {
            textViewWallet.text = "-" + getString(R.string.label_curruncy_sar) + " " + (App.getTripData().totalAmount - App.getTripData().tripDetails.promocodeAmount - App.getTripData().customerData.wallet).toString()
            textViewTotalCost.text = getString(R.string.label_curruncy_sar) + " " + (App.getTripData().totalAmount
                    - App.getTripData().tripDetails.promocodeAmount).toString()
        }

        textViewReceiptId.text = getString(R.string.dummy_order_id_1234567890) + " " + App.getTripData().tripNo.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receipt_layout, container, false)


    }


    override fun onViewClick(view: View) {

        when (view.id) {

            R.id.buttonConfirm -> {
                observeReceiptConfirmation()
            }
        }
    }


    private fun observeReceiptConfirmation() {
        homeActivityViewModel.confirmReciept.observe(this, onChange = {
            navigator.toggleLoader(false)
            homeActivityViewModel.confirmReciept.removeObservers(this)
            showMessage(it.message)
            if (it.responseCode == 1) {
                App.setTripData(it.data)
                navigator.load(RatingFragment::class.java).replace(false)
            }
        }, onError = {
            true
        })

        val parameter = Parameter()
        parameter.tripId = myRideId
        navigator.toggleLoader(true)
        homeActivityViewModel.confirmRecieptApi(parameter)
    }

    private fun observeRateAndReviewConfirmation() {
        homeActivityViewModel.trackTripApiTwoCall.observe(this, onChange = {
            homeActivityViewModel.trackTripApiTwoCall.removeObservers(this)
            showMessage(it.message)
            if (it.responseCode == 1) {
                App.setTripData(it.data)
                setUserData()
            }
        }, onError = {
            true
        })

        val parameter = Parameter()
        parameter.tripId = arguments?.getString(com.victoria.driver.core.Common.TRIP_ID)!!

        homeActivityViewModel.trackTripApiTwoCall(parameter)
    }


}
