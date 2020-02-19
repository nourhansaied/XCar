package com.victoria.customer.ui.home.fragments

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.payfort.fort.android.sdk.base.FortSdk
import com.payfort.sdk.android.dependancies.base.FortInterfaces
import com.payfort.sdk.android.dependancies.models.FortRequest

import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.data.URLFactory
import com.victoria.customer.data.pojo.CardListing
import com.victoria.customer.data.pojo.PayfortAccess
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.viewmodel.HomeActivityViewModel
import com.victoria.customer.ui.base.BaseActivity
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.settings.adapter.PaymentDetailsAdapter
import com.victoria.customer.ui.home.viewmodel.FareEstimationViewModel
import kotlinx.android.synthetic.main.fragment_add_amount_layout.*
import kotlinx.android.synthetic.main.toolbar_with_close.*
import java.util.*
import kotlin.collections.ArrayList
import android.databinding.adapters.TextViewBindingAdapter.setText

class AddAmountFragment : BaseFragment(), PaymentDetailsAdapter.CallBackInterFace {

    private lateinit var list: ArrayList<CardListing>
    private lateinit var adapter: PaymentDetailsAdapter

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    private val viewModel: FareEstimationViewModel  by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[FareEstimationViewModel::class.java]
    }

    override fun createLayout(): Int {
        return com.victoria.customer.R.layout.fragment_add_amount_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {

        toolBarText.text = getString(com.victoria.customer.R.string.toolbar_title_add_amount)
        imageViewBack.setOnClickListener(this::onViewClick)
        imageViewAdd.setOnClickListener(this::onViewClick)
        buttonConfirm.setOnClickListener(this::onViewClick)
        editTextAmount.setText(getString(com.victoria.customer.R.string.label_curruncy_sar) + " ")
        editTextAmount.text?.length?.let { Selection.setSelection(editTextAmount.text, it) }
        observeResponseForCardList(Parameter())



        radioTip100.setOnClickListener {
            editTextAmount.clearFocus()
            editTextAmount.setText(getString(R.string.dummy_sar_1000))
            editTextAmount.clearFocus()
        }
        radioTip20.setOnClickListener {
            editTextAmount.clearFocus()
            editTextAmount.setText(getString(R.string.dummy_sar_20))
            editTextAmount.clearFocus()
        }
        radioTip10.setOnClickListener {
            editTextAmount.clearFocus()
            editTextAmount.setText(getString(R.string.sar_10))
            editTextAmount.clearFocus()
        }
        radioTip50.setOnClickListener {
            editTextAmount.clearFocus()
            editTextAmount.setText(getString(R.string.dummy_sar_50))
            editTextAmount.clearFocus()
        }

        setAdapter()

        editTextAmount.addTextChangedListener(
                object : TextWatcher {

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    }

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                                   after: Int) {

                    }

                    override fun afterTextChanged(s: Editable) {
                        if (!s.toString().startsWith(getString(com.victoria.customer.R.string.label_curruncy_sar) + " ")) {
                            editTextAmount.setText(getString(com.victoria.customer.R.string.label_curruncy_sar) + " ")
                            editTextAmount.text?.length?.let { Selection.setSelection(editTextAmount.getText(), it) }
                        }

                        if (s.toString().length == 2 && s.toString().substring(1).equals("0", ignoreCase = true)) {
                            s.clear()
                        }

                        if (s.isNotEmpty() && editTextAmount.hasFocus()) {
                            radioTip10.isChecked = false
                            radioTip20.isChecked = false
                            radioTip50.isChecked = false
                            radioTip100.isChecked = false
                        }
                    }
                })

        radioTip10.isChecked = true
        radioTip10.callOnClick()
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            com.victoria.customer.R.id.imageViewBack -> {
                navigator.goBack()
            }

            com.victoria.customer.R.id.imageViewAdd -> {
                navigator.load(AddCreditCardFragment::class.java).replace(true)
            }

            com.victoria.customer.R.id.buttonConfirm -> {
                if (editTextAmount.text.toString().length <= 3) {
                    showMessage("Please enter amount")
                } else {

                }
                getSdkToken()
                //navigator.load(AddCreditCardFragment::class.java).replace(true)
            }

        }
    }

    private fun setAdapter() {
        list = ArrayList()

        val adapter = PaymentDetailsAdapter(context, list, this)
        adapter.showHide(false)
        this.adapter = adapter
        recyleViewAddCard.layoutManager = LinearLayoutManager(context)
        recyleViewAddCard.adapter = adapter
    }

    private fun observeResponse(parameter: Parameter) {
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        viewModel.addwalletamount.value = null
        viewModel.addwalletamount(parameter)
        viewModel.addwalletamount.observe(this, { responseBody ->
            navigator.toggleLoader(false)
            if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {
                showMessage(responseBody.message)
                navigator.goBack()
            }
        })
    }


    private fun observeResponseForCardList(parameter: Parameter) {
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        viewModel.cardlist.value = null
        viewModel.cardlist(parameter)
        viewModel.cardlist.observe(this, { responseBody ->
            navigator.toggleLoader(false)
            if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {
                list.clear()
                responseBody.data?.let { list.addAll(it) }
                adapter.notifyDataSetChanged()
            } else if (responseBody.responseCode == URLFactory.ResponseCode.NO_DATA_FOUND || responseBody.responseCode == 0) {
                relativeLayoutNoData.visibility = View.VISIBLE
                textViewMessage.text = responseBody.message
            }
        })
    }

    private fun getSdkToken() {
        navigator.toggleLoader(true)
        val deviceId = FortSdk.getDeviceId(activity!!)
        observePaymentForSdkToken(deviceId)
    }

    private fun observePaymentForSdkToken(deviceId: String) {
        homeActivityViewModel.getsdktoken.value = null
        val parameter = Parameter()
        parameter.device_id = deviceId
        homeActivityViewModel.getsdktoken(parameter)
        homeActivityViewModel.getsdktoken.observe(this, onChange = {
            navigator.toggleLoader(false)
            if (it.responseCode == 1) {
                startPayFort(it.data!!)
            } else {
                showMessage(it.message)
            }
        }, onError = {
            true
        })
    }

    private fun startPayFort(sdkTokenPojo: PayfortAccess/*, @Nullable final CardPojo card*/) {

        val environment = Common.PAYFORT_ENVIRONMENT
        val fortRequest = FortRequest()
        val fortCallback = (activity as BaseActivity).setFortCallBackFectory()

        val hashMapString = HashMap<String, Any>()

        var command = ""
        var amount = 0


        /**
         * only AUTHORIZATION (save card) is removed,
         * @param command will be "PURCHASE" only and
         * @param clickTypeAddPay will be "Pay" only
         * after that card will be saved if it is new.
         */
        /*if (clickTypeAddPay.equals("Add")) {

            command = "AUTHORIZATION";
            amount = 1 * 100;

        } else if (clickTypeAddPay.equals("Pay")) {*/

        command = "PURCHASE"

        /**
         * if pay using WALLET AMOUNT then
         * @param amount will be 'remainingPayable' else 'totalAmmount'
         */
        /*if (checkBoxPaymentFromWallet.isChecked()) {
            amount = (java.lang.Double.parseDouble(String.format(Locale.US, "%.2f", remainingPayable)) * 100).toInt()
        } else {
        }*/
        amount = (java.lang.Double.parseDouble(String.format(Locale.US, "%.2f", context?.getString(R.string.label_curruncy_sar)?.let { editTextAmount.text.toString().replace(it, "").toDouble() })) * 100).toInt()

        /*int remainingPayable_Int = (int) (Double.parseDouble(String.format("%.2f", remainingPayable)) * 100);
            int totalAmmount_Int = (int) (Double.parseDouble(String.format("%.2f", totalAmmount)) * 100);
            amount = checkBoxPaymentFromWallet.isChecked() ? remainingPayable_Int : totalAmmount_Int;*/

        /*}*/


        hashMapString["command"] = command
        hashMapString["currency"] = "EGP"


        //        hashMapString.put("remember_me", "YES");


        hashMapString["amount"] = amount.toString() + ""

        hashMapString["language"] = if (VictoriaCustomer.isRTL_ar()) "ar" else "en"

        session.user?.email?.let { hashMapString.put("customer_email", it) }

        val name = session.user?.firstName + " " + session.user?.lastName

        hashMapString["customer_name"] = name.substring(0, Math.min(name.length, 35))

        hashMapString["merchant_reference"] = getRandomNumber() ///qwe9239-po898
        hashMapString["sdk_token"] = sdkTokenPojo.sdkToken
        if (adapter.selectedPosition != -1)
            hashMapString["token_name"] = list[adapter.selectedPosition].cardToken
        /*if (selectedCardDetail != null && clickTypeAddPay == "Pay") {
            isPayWithNewCard = false
            hashMapString["token_name"] = selectedCardDetail.getTokenName()
        } else if (selectedCardDetail == null && clickTypeAddPay == "Pay") {
            */
        /**
         * when any card is not selected but pay with new card
         *//*
            isPayWithNewCard = true
        }*/

        fortRequest.isShowResponsePage = false
        fortRequest.requestMap = hashMapString

        try {
            navigator.toggleLoader(true)
            FortSdk.getInstance().registerCallback(activity, fortRequest, environment, 52626,
                    fortCallback, false, object : FortInterfaces.OnTnxProcessed {

                override fun onCancel(map: Map<String, Any>, map1: Map<String, Any>) {

                    navigator.toggleLoader(false)
                    Log.d("Payfort", "onCancel")

                }

                override fun onSuccess(map: Map<String, Any>, map1: Map<String, Any>) {
                    navigator.toggleLoader(false)
                    val merchantReference = map1["merchant_reference"] as String

                    val parameter = Parameter()

                    parameter.amount = context?.getString(R.string.label_curruncy_sar)?.let { editTextAmount.text.toString().replace(it, "") }.toString()
                    parameter.merchant_reference = merchantReference
                    parameter.transaction_id = map1["fort_id"].toString()
                    parameter.authorization_code = map1["authorization_code"].toString()
                    parameter.card_token = map1["token_name"].toString()
                    parameter.card_number = map1["card_number"].toString()
                    parameter.expiry_date = map1["expiry_date"].toString()
                    parameter.card_holder_name = map1["card_holder_name"].toString()
                    parameter.card_type = map1["payment_option"].toString()

                    observeResponse(parameter)
                }

                override fun onFailure(map: Map<String, Any>, map1: Map<String, Any>) {

                    navigator.toggleLoader(false)

                    Log.d("Payfort", "onFailure")
                    /*Log.e("Payfort", map.values().toString());
                            Log.e("Payfort", map1.values().toString());*/
                    showMessage(getString(com.victoria.customer.R.string.paymentTransactionFailed))
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getRandomNumber(): String {
        val tsLong = System.currentTimeMillis() / 1000
        val uniqueNo = session.user?.customerId.toString() + "_" + tsLong.toString()

        /*val random = Random()
        return random.nextInt(900000000) + 100000000*/
        return uniqueNo
    }

    override fun deleteClick(cardDetail: CardListing) {

    }
}
