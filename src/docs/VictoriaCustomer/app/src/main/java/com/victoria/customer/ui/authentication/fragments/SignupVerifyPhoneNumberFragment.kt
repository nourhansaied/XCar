package com.victoria.customer.ui.authentication.fragments

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.AppCompatEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.customer.R
import com.victoria.customer.data.pojo.ResponseBody
import com.victoria.customer.data.pojo.User
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.viewmodel.OtpVerifyViewModel
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.activity.HomeActivity
import com.victoria.customer.util.OTPView
import com.victoria.customer.util.OTPWatcher
import com.victoria.customer.util.PARAMETERS
import kotlinx.android.synthetic.main.fragment_verification_phone_number_layout.*


class SignupVerifyPhoneNumberFragment : BaseFragment() {

    lateinit var editTextOtp1: AppCompatEditText
    lateinit var editTextOtp2: AppCompatEditText
    lateinit var editTextOtp3: AppCompatEditText
    lateinit var editTextOtp4: AppCompatEditText
    var otp: String = ""

    private lateinit var otpView: OTPView

    private val otpVerifyViewModel: OtpVerifyViewModel  by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[OtpVerifyViewModel::class.java]
    }

    override fun createLayout(): Int {
        return R.layout.fragment_verification_phone_number_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {

        setDataFromSession()

        observeOtpClickResponse()
        observeOtpResendClickResponse()

        imageViewNext.setOnClickListener(this::onViewClick)
        imageViewClose.setOnClickListener(this::onViewClick)
        textViewResend.setOnClickListener(this::onViewClick)

        editTextOtp1.requestFocus()
        editTextOtp1.isCursorVisible = true

        OTPWatcher(editTextOtp1, editTextOtp2, editTextOtp3, editTextOtp4, object : OTPWatcher.OTPListner {
            override fun onvalidOTP(s: String) {
                otp = s
            }

            override fun onInavlidOTP(s: String) {
                otp = s
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setDataFromSession() {
        textViewOtpSent.text = getString(R.string.label_otp_has_been_sent_to_00_123_123_1234) + " " + session.user?.countryCode + " " + (session.user?.phone)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_verification_phone_number_layout, container, false)

        editTextOtp1 = view.findViewById(R.id.editTextOtp1)
        editTextOtp2 = view.findViewById(R.id.editTextOtp2)
        editTextOtp3 = view.findViewById(R.id.editTextOtp3)
        editTextOtp4 = view.findViewById(R.id.editTextOtp4)

        return view

    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewNext -> {

                if (editTextOtp1.text!!.isNotEmpty() && editTextOtp2.text!!.isNotEmpty() && editTextOtp3.text!!.isNotEmpty() && editTextOtp4.text!!.isNotEmpty()) {
                    navigator.toggleLoader(true)
                    otpVerifyViewModel.otpVerifyApiCall(getOtpData())
                } else {
                    showSnackBar(getString(R.string.validation_empty_otp))
                }
            }

            R.id.imageViewClose -> {
                navigator.goBack()
            }

            R.id.textViewResend -> {
                navigator.toggleLoader(true)
                otpVerifyViewModel.resendOtpApiCall(getOtpResendData())
            }

        }
    }

    /**
     * OTP API calling stuff
     * */

    private fun getOtpData(): Parameter {
        val parameter = Parameter()
        parameter.customer_id = session.user?.customerId.toString()
        parameter.otpCode = editTextOtp1.text.toString() +
                editTextOtp2.text.toString() +
                editTextOtp3.text.toString() +
                editTextOtp4.text.toString()

        return parameter
    }

    private fun observeOtpClickResponse() {
        otpVerifyViewModel.otpVerify.observe(this, { responseBody ->
            handleOtpResponse(responseBody)
        }, { throwable ->
            navigator.toggleLoader(false);true
        })
    }

    private fun handleOtpResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        if (responseBody.responseCode == 1) {
            session.userSession = responseBody.data?.token!!
            appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, true)
            navigator.loadActivity(HomeActivity::class.java).byFinishingAll().start()
        } else {
            showMessage(responseBody.message)
        }

    }

    /**
     * OTP API calling stuff
     * */

    /**
     * OTP RESEND API calling stuff
     * */
    private fun observeOtpResendClickResponse() {
        otpVerifyViewModel.otpResend.observe(this, { responseBody ->
            handleOtpResendResponse(responseBody)
        }, { throwable ->
            navigator.toggleLoader(false);true
        })
    }

    private fun getOtpResendData(): Parameter {
        val parameter = Parameter()
        parameter.customer_id = session.user?.customerId.toString()

        return parameter
    }

    private fun handleOtpResendResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        //session.userSession = responseBody.data?.token!!
        showMessage(responseBody.message)
    }
}

/**
 * OTP RESEND API calling stuff
 * */

