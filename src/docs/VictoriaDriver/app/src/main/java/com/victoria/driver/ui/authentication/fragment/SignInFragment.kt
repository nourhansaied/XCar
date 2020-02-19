package com.victoria.driver.ui.authentication.fragment

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fondesa.kpermissions.extension.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.iid.FirebaseInstanceId
import com.victoria.driver.R
import com.victoria.driver.core.AppPreferences
import com.victoria.driver.core.Common
import com.victoria.driver.core.Validator
import com.victoria.driver.data.pojo.ResponseBody
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.exception.ApplicationException
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.activity.HomeActivity
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.SignInViewModel
import com.victoria.driver.util.LocationManager
import com.victoria.driver.util.PARAMETERS
import kotlinx.android.synthetic.main.fragment_sign_in_layout.*
import javax.inject.Inject

class SignInFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator
    @Inject
    lateinit var locationManager: LocationManager
    @Inject
    lateinit var appPref: AppPreferences

    private var token: String? = null
    private var latLngToUse: LatLng? = null

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
        observeSignInClickResponse()
        getDeviceToken()

        textViewSignUp.setOnClickListener(this::onViewClick)
        textViewForgotPassword.setOnClickListener(this::onViewClick)
        imageViewNext.setOnClickListener(this::onViewClick)
    }

    private fun triggerLocation() {
        navigator.toggleLoader(true)
        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(latLng: LatLng?) {
                latLngToUse = latLng
                locationManager.stop()
                signInViewModel.loginApiCall(getSignInData())
            }

            override fun onFail(status: LocationManager.LocationListener.Status?) {
            }

        })
    }

    private fun getDeviceToken() {
        token = FirebaseInstanceId.getInstance().token
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in_layout, container, false)
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

    override fun onBackActionPerform(): Boolean {
        navigator.load(StartFragment::class.java).replace(false)
        return false
    }

    private fun checkValidation(): Boolean {
        try {
            validator.submit(editTextEmail).checkEmpty().errorMessage(getString(R.string.validation_email))
                    .matchPatter(Common.EMAILREGEX).errorMessage(getString(R.string.validation_valid_email)).check()

            validator.submit(editTextPasword)
                    .checkEmpty().errorMessage(getString(R.string.validation_empty_password))
                    .check()
            return true
        } catch (e: ApplicationException) {
            hideKeyBoard()
            showMessage(e.message)
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
        parameter.deviceToken = this.token!!
        parameter.latitude = latLngToUse?.latitude.toString()
        parameter.longitude = latLngToUse?.longitude.toString()
        if (Common.WHICH_LANGUAGE_SELECTED.toString().isNotEmpty())
            parameter.appLanguage = Common.WHICH_LANGUAGE_SELECTED
        else
            parameter.appLanguage = "en"
        return parameter
    }

    private fun handleSignInResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        when {
            responseBody.responseCode == 1 -> {
                session.userSession = responseBody.data!!.token
                session.user = responseBody.data
                appPref.putBoolean(PARAMETERS.KEY_IS_LOGIN, true)
                navigator.loadActivity(HomeActivity::class.java).byFinishingCurrent().start()
            }
            responseBody.responseCode == 4 -> {
                session.user = responseBody.data
                navigator.load(SignupVerifyPhoneNumberFragment::class.java).replace(true)
            }
            responseBody.responseCode == 8 -> {
                session.user = responseBody.data
                navigator.load(SignUpVehicleInfoFragment::class.java).replace(true)
            }
            responseBody.responseCode == 9 -> {
                session.user = responseBody.data
                navigator.load(UploadVehicleDocumentFragment::class.java).replace(true)
            }
            responseBody.responseCode == 10 -> showMessage(responseBody.message)
            else -> showMessage(responseBody.message)
        }
    }

    /**
     * Sign In API calling stuff
     * */

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
                        Toast.makeText(requireContext(), "Go to Permissions and enable Location permission.", Toast.LENGTH_LONG).show()
                        openPermissionSettings()
                    }
                    .onShouldShowRationale { _, permissionNonce ->
                        permissionNonce.use()
                    }
                    .send()
        }
    }


    private fun openPermissionSettings() {
        val intent = android.content.Intent()
        intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = android.net.Uri.fromParts("package", getString(R.string.default_notification_channel_id), null)
        startActivity(intent)
    }
}
