package com.victoria.customer.ui.authentication.fragments

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.fondesa.kpermissions.extension.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.iid.FirebaseInstanceId
import com.victoria.customer.R
import com.victoria.customer.core.AppPreferences
import com.victoria.customer.core.Common
import com.victoria.customer.core.Validator
import com.victoria.customer.data.pojo.ResponseBody
import com.victoria.customer.data.pojo.User
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.viewmodel.SignInViewModel
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.activity.HomeActivity
import com.victoria.customer.util.LocationManager
import com.victoria.customer.util.PARAMETERS
import kotlinx.android.synthetic.main.fragment_sign_in_layout.*
import javax.inject.Inject


class SignInFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    @Inject
    lateinit var appPref: AppPreferences
    var token: String? = null
    private lateinit var latLngToUse: LatLng

    override fun createLayout(): Int {
        return R.layout.fragment_sign_in_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val signInViewModel: SignInViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[SignInViewModel::class.java]
    }

    override fun bindData() {
        getDeviceToken()
        observeSignInClickResponse()

        textViewSignUp.setOnClickListener { onViewClick(it) }
        textViewForgotPassword.setOnClickListener { onViewClick(it) }
        imageViewNext.setOnClickListener { onViewClick(it) }
    }

    override fun onBackActionPerform(): Boolean {
        navigator.load(StartFragment::class.java).replace(false)
        return false
    }

    private fun createRequestForPermission() {
        if (ActivityCompat.checkSelfPermission(activity!!,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            triggerLocation()
        } else {
            permissionsBuilder(Manifest.permission.ACCESS_FINE_LOCATION)
                    .build()
                    .onAccepted {
                        triggerLocation()
                    }
                    .onDenied {

                    }
                    .onPermanentlyDenied {
                        Toast.makeText(requireContext(), getString(R.string.validation_permission), Toast.LENGTH_LONG).show()
                        openPermissionSettings()
                    }
                    .onShouldShowRationale { strings, permissionNonce ->
                        permissionNonce.use()
                    }
                    .send()
        }
    }


    private fun triggerLocation() {
        navigator.toggleLoader(true)
        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(latLng: LatLng) {
                latLngToUse = latLng
                locationManager.stop()
                signInViewModel.loginApiCall(getSignInData())
            }

            override fun onFail(status: LocationManager.LocationListener.Status) {

            }
        })
    }

    private fun openPermissionSettings() {
        val intent = android.content.Intent()
        intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = android.net.Uri.fromParts("package", getString(R.string.default_notification_channel_id), null)
        startActivity(intent)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {
            R.id.textViewSignUp -> {
                hideKeyBoard()
                navigator.load(SignupFragment::class.java).replace(true)

            }
            R.id.textViewForgotPassword -> {

                hideKeyBoard()
                navigator.load(ForgotPasswordFragment::class.java).replace(true)
            }
            R.id.imageViewNext -> {
                hideKeyBoard()

                if (checkValidation()) {
                    createRequestForPermission()
                }
            }


        }
    }

    private fun checkValidation(): Boolean {
        try {
            if (editTextEmail.text?.matches(Patterns.EMAIL_ADDRESS.pattern().toRegex())!!) {
                validator.submit(editTextEmail)
                        .checkEmpty().errorMessage(getString(R.string.validation_valid_email))
                        .check()
            } else {
                validator.submit(editTextEmail)
                        .checkEmpty().errorMessage(getString(R.string.validation_email))
                        .check()

            }
            validator.submit(editTextPasword)
                    .checkEmpty().errorMessage(getString(R.string.validation_empty_password))
                    .check()
            return true
        } catch (e: ApplicationException) {
            hideKeyBoard()
            showSnackBar(e.message)
        }
        return false
    }

    /**
     * Sign In API calling stuff
     * */
    private fun observeSignInClickResponse() {
        signInViewModel.login.observe(this, { responseBody ->
            handleSignInResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun getSignInData(): Parameter {
        val parameter = Parameter()

        parameter.email = editTextEmail.text.toString()
        parameter.password = editTextPasword.text.toString()
        parameter.devieType = Common.DEVICE_TYPE
        parameter.deviceToken = this.token.toString()
        parameter.latitude = latLngToUse.latitude.toString()
        parameter.longitude = latLngToUse.longitude.toString()
        parameter.longitude = latLngToUse.longitude.toString()
        if (Common.WHICH_LANGUAGE_SELECTED.toString().isNotEmpty())
            parameter.appLanguage = Common.WHICH_LANGUAGE_SELECTED
        else
            parameter.appLanguage = "en"
        return parameter
    }

    private fun handleSignInResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        if (responseBody.responseCode == 1) {
            session.userSession = responseBody.data!!.token
            session.user = responseBody.data
            appPref.putBoolean(PARAMETERS.KEY_IS_LOGIN, true)
            navigator.loadActivity(HomeActivity::class.java).byFinishingCurrent().start()
        } else if (responseBody.responseCode == 4) {
            session.user = responseBody.data
            navigator.load(SignupVerifyPhoneNumberFragment::class.java).replace(true)
        } else {
            showMessage(responseBody.message)
        }
    }

    /**
     * Sign In API calling stuff
     * */

    private fun getDeviceToken() {

        token = FirebaseInstanceId.getInstance().token

    }
}



