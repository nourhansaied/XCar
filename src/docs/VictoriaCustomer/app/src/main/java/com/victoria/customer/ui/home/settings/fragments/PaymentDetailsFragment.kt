package com.victoria.customer.ui.home.settings.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
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
import com.victoria.customer.ui.home.ride.fragments.RatingFragment
import com.victoria.customer.ui.home.settings.adapter.PaymentDetailsAdapter
import com.victoria.customer.ui.home.viewmodel.FareEstimationViewModel
import kotlinx.android.synthetic.main.fragment_payment_details_layout.*
import kotlinx.android.synthetic.main.toolbar_with_close.*
import java.util.*
import kotlin.collections.ArrayList


class PaymentDetailsFragment : BaseFragment(), PaymentDetailsAdapter.CallBackInterFace {


    private var totalAmount: String? = ""
    lateinit var list: ArrayList<CardListing>
    lateinit var adapter: PaymentDetailsAdapter

    override fun createLayout(): Int {
        return R.layout.fragment_payment_details_layout
    }

    private val homeActivityViewModel: HomeActivityViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[HomeActivityViewModel::class.java]
    }

    private val viewModel: FareEstimationViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[FareEstimationViewModel::class.java]
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {

        toolBarText.text = getString(R.string.toolbar_title_payment_details)
        //imageViewAdd.setOnClickListener(this::onViewClick)
        imageViewBack.setOnClickListener(this::onViewClick)
        buttonConfirm.setOnClickListener(this::onViewClick)
        observeResponseForCardList(Parameter())
        setAdapter()

        if (arguments != null && arguments!!.containsKey(Common.IS_FROM_SETTING)) {
            if (arguments!!.getBoolean(Common.IS_FROM_SETTING)) {
                buttonConfirm.visibility = View.GONE
            } else {
                totalAmount = arguments!!.getString(Common.TOTAL_COST)
                buttonConfirm.visibility = View.VISIBLE
            }
        }

        if (arguments != null && arguments?.containsKey(Common.IS_FROM_RECEIPT)!!) {
            if (arguments?.getBoolean(Common.IS_FROM_RECEIPT)!!) {
                adapter.showHide(false)
            }
        }

    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {
            R.id.buttonConfirm -> {
                getSdkToken()
            }
            /*R.id.imageViewAdd -> {
                navigator.load(AddCreditCardFragment::class.java).replace(true)
            }*/

            R.id.imageViewBack -> {
                navigator.goBack()
            }
        }
    }

    private fun setAdapter() {
        list = ArrayList()

        val adapter = PaymentDetailsAdapter(context, list, this)
        this.adapter = adapter
        adapter.showHide(true)
        recycleViewPaymentDetails.layoutManager = LinearLayoutManager(context)
        recycleViewPaymentDetails.adapter = adapter

    }

    override fun deleteClick(cardDetail: CardListing) {
        val parameter = Parameter()
        parameter.card_id = cardDetail.cardId.toString()
        showDialogWithTwoActions(null, getString(R.string.delete_message), getString(R.string.label_yes), getString(R.string.label_no), { yesclick, id ->
            observeResponseForRemoveCard(parameter, cardDetail)
        }, { noClick, id ->
        })
    }

    private fun observeResponseForRemoveCard(parameter: Parameter, cardDetail: CardListing) {
        navigator.toggleLoader(false)
        navigator.toggleLoader(true)
        viewModel.removecard.value = null
        viewModel.removecard(parameter)
        viewModel.removecard.observe(this, { responseBody ->
            navigator.toggleLoader(false)
            if (responseBody.responseCode == URLFactory.ResponseCode.SUCCESS) {
                list.remove(cardDetail)
                adapter.notifyDataSetChanged()
                observeResponseForCardList(Parameter())
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
                recycleViewPaymentDetails.visibility = View.VISIBLE
                relativeLayoutNoData.visibility = View.GONE
            } else if (responseBody.responseCode == URLFactory.ResponseCode.NO_DATA_FOUND || responseBody.responseCode == 0) {
                relativeLayoutNoData.visibility = View.VISIBLE
                recycleViewPaymentDetails.visibility = View.GONE
                textViewMessage.text = responseBody.message
            }
        })
    }

    private fun observePaymentConfirmation(parameter: Parameter) {
        homeActivityViewModel.paymentConfirmation.value = null
        homeActivityViewModel.paymentConfirmation(parameter)
        homeActivityViewModel.paymentConfirmation.observe(this, onChange = {
            navigator.toggleLoader(false)
            showMessage(it.message)
            if (it.responseCode == 1) {
                navigator.load(RatingFragment::class.java).replace(false)
            } else if (it.responseCode == 2) {
                navigator.load(RatingFragment::class.java).replace(false)
            }
        }, onError = {
            true
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
        amount = (java.lang.Double.parseDouble(String.format(Locale.US, "%.2f", totalAmount?.toDouble())) * 100).toInt()

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
        if (adapter.selectedPosition != -1)
            hashMapString["token_name"] = list[adapter.selectedPosition].cardToken
        hashMapString["merchant_reference"] = getRandomNumber() ///qwe9239-po898
        hashMapString["sdk_token"] = sdkTokenPojo.sdkToken


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

                    Log.d("merchantReference", merchantReference)

                    //presenter.callCheckPayfortStatusWS(merchantReference)
                    //cardSuccessMap = map1
                    val parameter = Parameter()
                    parameter.tripId = VictoriaCustomer.getTripData().tripId.toString()
                    parameter.paymentMode = VictoriaCustomer.getTripData().paymentMode
                    parameter.merchant_reference = merchantReference
                    parameter.transaction_id = map1["fort_id"].toString()
                    parameter.authorization_code = map1["authorization_code"].toString()
                    parameter.card_token = map1["token_name"].toString()
                    parameter.card_number = map1["card_number"].toString()
                    parameter.expiry_date = map1["expiry_date"].toString()
                    parameter.card_holder_name = map1["card_holder_name"].toString()
                    parameter.card_type = map1["payment_option"].toString()

                    navigator.toggleLoader(true)
                    Handler().postDelayed({
                        observePaymentConfirmation(parameter)
                    }, 30)

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
        val uniqueNo = VictoriaCustomer.getTripData().tripNo.toString() + "_" + tsLong.toString()

        /*val random = Random()
        return random.nextInt(900000000) + 100000000*/
        return uniqueNo
    }
}
