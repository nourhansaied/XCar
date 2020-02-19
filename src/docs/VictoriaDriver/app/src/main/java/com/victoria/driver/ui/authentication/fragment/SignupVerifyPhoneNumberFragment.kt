package com.victoria.driver.ui.authentication.fragment


import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.AppCompatEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.driver.R
import com.victoria.driver.data.pojo.ResponseBody
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.OtpVerifyViewModel
import com.victoria.driver.util.OTPWatcher
import kotlinx.android.synthetic.main.fragment_verification_phone_number_layout.*


class SignupVerifyPhoneNumberFragment : BaseFragment() {

    lateinit var editTextOtp1: AppCompatEditText
    lateinit var editTextOtp2: AppCompatEditText
    lateinit var editTextOtp3: AppCompatEditText
    lateinit var editTextOtp4: AppCompatEditText
    var otp: String = ""

    override fun createLayout(): Int {
        return R.layout.fragment_verification_phone_number_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val otpVerifyViewModel: OtpVerifyViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[OtpVerifyViewModel::class.java]
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
        val view = inflater.inflate(R.layout.fragment_verification_phone_number_layout, container, false)

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
                    showMessage(getString(R.string.validation_empty_otp))
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
        parameter.driver_id = session.user?.driverId.toString()
        parameter.otpCode = editTextOtp1.text.toString() +
                editTextOtp2.text.toString() +
                editTextOtp3.text.toString() +
                editTextOtp4.text.toString()

        return parameter
    }

    private fun observeOtpClickResponse() {
        otpVerifyViewModel.otpVerify.observe(this, { responseBody ->
            handleOtpResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun handleOtpResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        if (responseBody.responseCode == 1) {
            navigator.load(SignUpVehicleInfoFragment::class.java).replace(true)
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
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun getOtpResendData(): Parameter {
        val parameter = Parameter()
        parameter.driver_id = session.user?.driverId.toString()

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

