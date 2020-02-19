package com.victoria.customer.ui.home.ride.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.viewmodel.HomeActivityViewModel
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.dialog.ChangePaymentMethodPopup
import com.victoria.customer.ui.home.fragments.AddAmountFragment
import com.victoria.customer.util.DateTimeFormatter
import kotlinx.android.synthetic.main.fragment_receipt_layout.*
import com.victoria.customer.ui.home.settings.fragments.PaymentDetailsFragment


class ReceiptFragment : BaseFragment(), ChangePaymentMethodPopup.CallBackForLanguageSelect {

    override fun createLayout(): Int {
        return com.victoria.customer.R.layout.fragment_receipt_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        setUserData()

        buttonConfirm.setOnClickListener(this::onViewClick)
    }

    private lateinit var totalAmount: String

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    private fun setUserData() {
        textViewUserName.text = getString(com.victoria.customer.R.string.label_hello) + " " + VictoriaCustomer.getTripData().customerData.firstName
        appCompatTextView2.text = VictoriaCustomer.getTripData().driverData.name

        textViewCarType.text = VictoriaCustomer.getTripData().driverData.vehicleModel +
                " - " + VictoriaCustomer.getTripData().vehicleData.vehicle +
                " - " + VictoriaCustomer.getTripData().driverData.vehicleNumber

        textViewDate.text = DateTimeFormatter.utcToLocal(VictoriaCustomer.getTripData().tripdatetime, Common.DATE_TIME_FORMAT_UTC, Common.DATE_TIME_FORMAT_OUT)
        textViewTime.text = DateTimeFormatter.utcToLocal(VictoriaCustomer.getTripData().tripdatetime, Common.DATE_TIME_FORMAT_UTC, Common.DATE_TIME_FORMAT_OUT_TWO)

        textViewDistance.text = VictoriaCustomer.getTripData().distance.toFloat().toString() + " " + "km"
        textViewDuration.text = VictoriaCustomer.getTripData().totalTime.toInt().toString() + " " + "min"
        textViewSubTotal.text = getString(com.victoria.customer.R.string.label_curruncy_sar) + " " +
                VictoriaCustomer.getTripData().totalAmount.toString()
        textViewPromocode.text = "-" + getString(com.victoria.customer.R.string.label_curruncy_sar) + " " +
                VictoriaCustomer.getTripData().tripDetails.promocodeAmount.toString()

        if (VictoriaCustomer.getTripData().paymentMode.equals("wallet")) {
            labelWallet.visibility = View.VISIBLE
            textViewWallet.visibility = View.VISIBLE
        } else {
            labelWallet.visibility = View.GONE
            textViewWallet.visibility = View.GONE
        }

        if (VictoriaCustomer.getTripData().customerData.wallet > VictoriaCustomer.getTripData().totalAmount - VictoriaCustomer.getTripData().tripDetails.promocodeAmount) {
            textViewWallet.text = "-" + getString(com.victoria.customer.R.string.label_curruncy_sar) + " " + (VictoriaCustomer.getTripData().totalAmount - VictoriaCustomer.getTripData().tripDetails.promocodeAmount).toString()
            textViewTotalCost.text = getString(com.victoria.customer.R.string.label_curruncy_sar) + " " + (VictoriaCustomer.getTripData().totalAmount
                    - VictoriaCustomer.getTripData().tripDetails.promocodeAmount).toString()
            totalAmount = (VictoriaCustomer.getTripData().totalAmount
                    - VictoriaCustomer.getTripData().tripDetails.promocodeAmount).toString()
        } else {
            textViewWallet.text = "-" + getString(com.victoria.customer.R.string.label_curruncy_sar) + " " + (VictoriaCustomer.getTripData().totalAmount - VictoriaCustomer.getTripData().tripDetails.promocodeAmount - VictoriaCustomer.getTripData().customerData.wallet).toString()
            textViewTotalCost.text = getString(com.victoria.customer.R.string.label_curruncy_sar) + " " + (VictoriaCustomer.getTripData().totalAmount
                    - VictoriaCustomer.getTripData().tripDetails.promocodeAmount).toString()
            totalAmount = (VictoriaCustomer.getTripData().totalAmount
                    - VictoriaCustomer.getTripData().tripDetails.promocodeAmount).toString()
        }
        textViewReceiptId.text = getString(com.victoria.customer.R.string.dummy_order_id_1234567890) + " " + VictoriaCustomer.getTripData().tripNo.toString()
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            com.victoria.customer.R.id.buttonConfirm -> {
                val changePaymentMethodPopup = ChangePaymentMethodPopup(this, appPreferences, session, navigator, VictoriaCustomer.getTripData().paymentMode)
                changePaymentMethodPopup.show(fragmentManager, "")


                /**
                 * @param :trip_id,payment_mode[wallet,cash,card]
                 */

            }
        }
    }

    private fun observePaymentConfirmation(parameter: Parameter) {
        homeActivityViewModel.paymentConfirmation.value = null
        homeActivityViewModel.paymentConfirmation(parameter)
        homeActivityViewModel.paymentConfirmation.observe(this, onChange = {
            navigator.toggleLoader(false)
            if (it.responseCode == 1) {
                //VictoriaCustomer.setTripData(it.data)
                navigator.load(RatingFragment::class.java).replace(false)
            } else if (it.responseCode == 12) {
                //VictoriaCustomer.setTripData(it.data)
                navigator.load(AddAmountFragment::class.java).replace(true)
            } else {
                showMessage(it.message)
            }
        }, onError = {
            true
        })
    }

    override fun dissmissDialog(paymentMode: String) {
        if (paymentMode == "card") {
            navigator.load(PaymentDetailsFragment::class.java).setBundle(Bundle().apply {
                putBoolean(Common.IS_FROM_SETTING, false)
                putBoolean(Common.IS_FROM_RECEIPT, true)
                putString(Common.TOTAL_COST, totalAmount)
            }).replace(true)
        } else if (paymentMode == "cash") {
            val parameter = Parameter()
            parameter.tripId = VictoriaCustomer.getTripData().tripId.toString()
            parameter.paymentMode = paymentMode
            navigator.toggleLoader(true)
            observePaymentConfirmation(parameter)

        } else {
            val parameter = Parameter()
            parameter.tripId = VictoriaCustomer.getTripData().tripId.toString()
            parameter.paymentMode = paymentMode
            navigator.toggleLoader(true)
            observePaymentConfirmation(parameter)
        }
    }
}
