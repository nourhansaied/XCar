package com.victoria.customer.ui.authentication.fragments

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import com.fondesa.kpermissions.extension.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.iid.FirebaseInstanceId
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Validator
import com.victoria.customer.data.pojo.ResponseBody
import com.victoria.customer.data.pojo.User
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.dialog.CountryCodeDialog
import com.victoria.customer.ui.authentication.viewmodel.SignUpViewModel
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.settings.fragments.WebViewFragment
import com.victoria.customer.ui.interfaces.ISelectCountry
import com.victoria.customer.util.*
import kotlinx.android.synthetic.main.fragment_signup_2_layout.*
import javax.inject.Inject


class SignupFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    private lateinit var countryCodeDialog: CountryCodeDialog
    var token: String? = null
    private lateinit var latLngToUse: LatLng

    lateinit var genderSelected: String

    override fun createLayout(): Int {
        return R.layout.fragment_signup_2_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val signUpViewModel: SignUpViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[SignUpViewModel::class.java]
    }

    override fun bindData() {
        getDeviceToken()
        observeSignUpClickResponse()

        countryCodeDialog = CountryCodeDialog()

        val locale = activity?.resources?.configuration?.locale?.country

        for (countryCOde in CountryUtils.readCountryJson(activity)) {

            if (locale.equals(countryCOde.iso2Cc, ignoreCase = true)) {

                editCountryCode.setText("+" + countryCOde.e164Cc)
                editCountryCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, CountryUtils.getFlagDrawableResId(countryCOde), 0)

                break
            }
        }


        /*val spannable = SpannableString(getString(R.string.terms_condition))

        spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.text_pink)), 15, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.text_pink)), 38, 52, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }

            override fun onClick(widget: View) {

                var bundle: Bundle = Bundle()
                bundle.putString(PARAMETERS.PARAM_SCREEN, getString(R.string.toolbar_title_terms_condition))
                navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
            }
        }, 15, 33, 0)

        spannable.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }

            override fun onClick(widget: View) {
                var bundle: Bundle = Bundle()
                bundle.putString(PARAMETERS.PARAM_SCREEN, getString(R.string.toolbar_title_privacy_policy))
                navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)


            }
        }, 38, 52, 0)*/



        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonMale -> genderSelected = Common.MALE!!
                else -> genderSelected = Common.FEMALE!!
            }
        }
        textViewTermsConditions.text = getString(R.string.label_iam_agree_with) + " " +
                getString(R.string.label_term_condition) + " " +
                getString(R.string.label_or) + " " +
                getString(R.string.label_privacy_policy_)

        TextDecorator.decorate(textViewTermsConditions, textViewTermsConditions.text.toString())
                .makeTextClickable(OnTextClickListener { view, text ->
                    when (text) {
                        "Terms & Conditions" -> {
                            val bundle = Bundle()
                            bundle.putString(PARAMETERS.PARAM_SCREEN, getString(R.string.toolbar_title_terms_condition))
                            navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
                        }
                        "البنود و الظروف" -> {
                            val bundle = Bundle()
                            bundle.putString(PARAMETERS.PARAM_SCREEN, getString(R.string.toolbar_title_terms_condition))
                            navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
                        }

                        "Privacy Policy" -> {
                            val bundle = Bundle()
                            bundle.putString(PARAMETERS.PARAM_SCREEN, getString(R.string.toolbar_title_privacy_policy))
                            navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
                        }

                        "سياسة خاصة" -> {
                            val bundle = Bundle()
                            bundle.putString(PARAMETERS.PARAM_SCREEN, getString(R.string.toolbar_title_privacy_policy))
                            navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
                        }

                    }
                }, false, getString(R.string.label_term_condition), getString(R.string.label_privacy_policy_))
                .setTextColor(R.color.text_pink, getString(R.string.label_term_condition), getString(R.string.label_privacy_policy_))
                .build()

        //textViewTermsConditions?.text = spannable
        textViewTermsConditions.movementMethod = LinkMovementMethod.getInstance()

        imageViewNext.setOnClickListener(this::onViewClick)
        imageViewClose.setOnClickListener(this::onViewClick)
        editCountryCode.setOnClickListener(this::onViewClick)
    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewNext -> {
                hideKeyBoard()
                if (checkValidation()) {
                    createRequestForPermission()
                }

            }

            R.id.imageViewClose -> {
                navigator.goBack()
            }

            R.id.editCountryCode -> {

                hideKeyBoard()
                countryCodeDialog = CountryCodeDialog()
                countryCodeDialog.setCallback(ISelectCountry { countryCode, country, id ->
                    editCountryCode.setText("+$countryCode")
                    editCountryCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, CountryUtils.getFlagDrawableResId(id), 0)
                })
                countryCodeDialog.show(fragmentManager, "")
            }


        }
    }

    private fun checkValidation(): Boolean {

        try {
            validator.submit(editTextFirstName).checkEmpty().errorMessage(getString(R.string.validation_empty_first_name))
                    .matchPatter(Common.FIRST_NAME_REGEX).errorMessage(getString(R.string.validation_valid_first_name)).check()

            validator.submit(editTextLastName).checkEmpty().errorMessage(getString(R.string.validation_empty_last_name))
                    .matchPatter(Common.FIRST_NAME_REGEX).errorMessage(getString(R.string.validation_valid_last_name)).check()


            validator.submit(editTextEmail).checkEmpty().errorMessage(getString(R.string.validation_email))
                    .matchPatter(Common.EMAILREGEX).errorMessage(getString(R.string.validation_valid_email)).check()

            validator.submit(editTextNationalId).checkEmpty().errorMessage(getString(R.string.validation_empty_national_id))
                    .check()
            validator.submit(editTextPhoneNumber).checkEmpty().errorMessage(getString(R.string.validation_empty_phone))
                    .checkMinDigits(6).errorMessage(getString(R.string.validation_valid_phone_number)).check()
            validator.submit(editTextPassword).checkEmpty().errorMessage(getString(R.string.validation_empty_password))
                    .checkMinDigits(4).errorMessage(getString(R.string.validation_valide_password)).check()
            validator.submit(editCountryCode).checkEmpty().errorMessage(getString(R.string.validation_country_code)).check()

            if (!radioButtonMale.isChecked && !radioButtonFeMale.isChecked) {
                showMessage(getString(R.string.validation_please_select_gender))
                return false
            }

            if (!checkBox.isChecked) {
                showMessage(getString(R.string.validation_terms_condition))
                return false
            }


            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showMessage(e.message)
        }
        return false
    }


    /**
     * Sign up API calling stuff
     * */

    private fun getSignUpData(): Parameter {
        val parameter = Parameter()

        parameter.first_name = editTextFirstName.text.toString()
        parameter.last_name = editTextLastName.text.toString()
        parameter.email = editTextEmail.text.toString()
        parameter.password = editTextPassword.text.toString()
        parameter.countryCode = editCountryCode.text.toString()
        parameter.phone = editTextPhoneNumber.text.toString()
        parameter.national_id = editTextNationalId.text.toString()
        parameter.deviceToken = this.token!!
        parameter.devieType = Common.DEVICE_TYPE
        parameter.latitude = latLngToUse?.latitude.toString()
        parameter.longitude = latLngToUse?.longitude.toString()
        parameter.gender = genderSelected
        if (Common.WHICH_LANGUAGE_SELECTED.toString().isNotEmpty())
            parameter.appLanguage = Common.WHICH_LANGUAGE_SELECTED
        else
            parameter.appLanguage = "en"
        return parameter
    }

    private fun observeSignUpClickResponse() {
        signUpViewModel.signUpLiveData.observe(this, { responseBody ->
            handleSignUpResponse(responseBody)
        }, { throwable ->
            signUpViewModel.signUpLiveData.removeObservers(this);
            navigator.toggleLoader(false);true
        })
    }

    private fun handleSignUpResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)

        if (responseBody.responseCode == 1) {
            session.user = responseBody.data
            navigator.load(SignupVerifyPhoneNumberFragment::class.java).replace(true)
        } else {
            showMessage(responseBody.message)
        }

    }

    /**
     * Sign up API calling stuff
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
                    .onShouldShowRationale { strings, permissionNonce ->
                        permissionNonce.use()
                    }
                    .send()
        }
    }


    private fun triggerLocation() {
        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(latLng: LatLng) {
                latLngToUse = latLng
                locationManager.stop()
                navigator.toggleLoader(true)
                signUpViewModel.signUpUser(getSignUpData())
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

    private fun getDeviceToken() {
        token = FirebaseInstanceId.getInstance().token
    }
}
