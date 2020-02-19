package com.victoria.driver.ui.authentication.fragment

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.text.InputType
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fondesa.kpermissions.extension.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.iid.FirebaseInstanceId
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.core.Validator
import com.victoria.driver.data.pojo.ResponseBody
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.exception.ApplicationException
import com.victoria.driver.ui.authentication.dialog.CountryCodeDialog
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.fragment.WebViewFragment
import com.victoria.driver.ui.interfaces.ISelectCountry
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.SignUpViewModel
import com.victoria.driver.util.*
import kotlinx.android.synthetic.main.fragment_signup_layout.*
import java.util.*
import javax.inject.Inject


class SignupFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    @Inject
    lateinit var locationManager: LocationManager

    private lateinit var countryCodeDialog: CountryCodeDialog
    private var token: String? = null
    private var latLngToUse: LatLng? = null

    private lateinit var genderSelected: String

    override fun createLayout(): Int {
        return R.layout.fragment_signup_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val signUpViewModel: SignUpViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[SignUpViewModel::class.java]
    }

    override fun bindData() {
        observeSignUpClickResponse()
        getDeviceToken()
        radioButtonMale.visibility = View.GONE
        countryCodeDialog = CountryCodeDialog()

        val locale = Locale.getDefault().country

        for (countryCOde in CountryUtils.readCountryJson(activity)) {

            if (locale.equals(countryCOde.iso2Cc, ignoreCase = true)) {

                editCountryCode.setText("+" + countryCOde.e164Cc)
                editCountryCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, CountryUtils.getFlagDrawableResId(countryCOde), 0)
                break
            }
        }
        editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        /*val stringBuilderTerms = SpannableStringBuilder(getString(R.string.terms_condition))
        stringBuilderTerms.setSpan(ForegroundColorSpan(ContextCompat.getColor(this.activity!!, R.color.text_pink)), 15, 33, 0)
        stringBuilderTerms.setSpan(termsSpan, 15, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        stringBuilderTerms.setSpan(ForegroundColorSpan(ContextCompat.getColor(this.activity!!, R.color.text_pink)), 37, stringBuilderTerms.length, 0)
        stringBuilderTerms.setSpan(privacySpan, 38, stringBuilderTerms.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)*/
        textViewTermsConditions.text = getString(R.string.label_iam_agree_with) + " " + getString(R.string.label_term_condition) + " " + getString(R.string.label_or) + " " + getString(R.string.label_privacy_policy_)

        TextDecorator.decorate(textViewTermsConditions, textViewTermsConditions.text.toString())
                .makeTextClickable(OnTextClickListener { view, text ->
                    when (text) {
                        "Terms & Conditions" -> {
                            var bundle = Bundle()
                            bundle.putString(PARAMETERS.PARAM_SCREEN, getString(R.string.toolbar_title_terms_condition))
                            navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
                        }
                        "البنود و الظروف" -> {
                            var bundle = Bundle()
                            bundle.putString(PARAMETERS.PARAM_SCREEN, getString(R.string.toolbar_title_terms_condition))
                            navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
                        }

                        "Privacy Policy" -> {
                            var bundle = Bundle()
                            bundle.putString(PARAMETERS.PARAM_SCREEN, getString(R.string.toolbar_title_privacy_policy))
                            navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
                        }

                        "سياسة خاصة" -> {
                            var bundle = Bundle()
                            bundle.putString(PARAMETERS.PARAM_SCREEN, getString(R.string.toolbar_title_privacy_policy))
                            navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
                        }

                    }
                }, false, getString(R.string.label_term_condition), getString(R.string.label_privacy_policy_))
                .setTextColor(R.color.text_pink, getString(R.string.label_term_condition), getString(R.string.label_privacy_policy_))
                .build()


        //textViewTermsConditions.text = stringBuilderTerms
        textViewTermsConditions.movementMethod = LinkMovementMethod.getInstance()


        /*radioGroup.setOnCheckedChangeListener { _, checkedId ->
            genderSelected = when (checkedId) {
                R.id.radioButtonMale -> Common.MALE!!
                else -> Common.FEMALE!!
            }
        }*/

        radioButtonFeMale.setOnClickListener {
            genderSelected = Common.FEMALE!!
        }

        radioButtonMale.setOnClickListener {
            genderSelected = Common.MALE!!
        }

        imageViewNext.setOnClickListener(this::onViewClick)
        imageViewClose.setOnClickListener(this::onViewClick)
        editCountryCode.setOnClickListener(this::onViewClick)
    }


    private fun triggerLocation() {
        locationManager.triggerLocation(object : LocationManager.LocationListener {
            override fun onLocationAvailable(latLng: LatLng?) {
                latLngToUse = latLng
                locationManager.stop()
                navigator.toggleLoader(true)
                signUpViewModel.signUpUser(getSignUpData())
            }

            override fun onFail(status: LocationManager.LocationListener.Status?) {
            }

        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_layout, container, false)
    }

    private var termsSpan: ClickableSpan = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = true
        }

        override fun onClick(textView: View) {
            val bundle = Bundle()
            bundle.putString(Common.screenText, getString(R.string.toolbar_title_terms_condition))
            navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)

        }
    }

    private var privacySpan: ClickableSpan = object : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = true
        }

        override fun onClick(textView: View) {
            val bundle = Bundle()
            bundle.putString(Common.screenText, getString(R.string.toolbar_title_privacy_policy))
            navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)

        }
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
                countryCodeDialog.setCallback(ISelectCountry { countryCode, _, id ->
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


            if (/*!radioButtonMale.isChecked && */!radioButtonFeMale.isChecked) {
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


    /**
     * Observer
     * */
    private fun observeSignUpClickResponse() {
        signUpViewModel.signUpLiveData.observe(this, { responseBody ->
            navigator.toggleLoader(false)
            signUpViewModel.signUpLiveData.removeObservers(this)
            handleSignUpResponse(responseBody)
        }, {
            signUpViewModel.signUpLiveData.removeObservers(this)
            navigator.toggleLoader(false);true
        })
    }

    private fun handleSignUpResponse(responseBody: ResponseBody<User>) {

        if (responseBody.responseCode == 1) {

            session.user = responseBody.data
            navigator.load(SignupVerifyPhoneNumberFragment::class.java).replace(true)
        } else {
            showMessage(responseBody.message)
        }
    }

    private fun getDeviceToken() {
        token = FirebaseInstanceId.getInstance().token
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
                        Toast.makeText(requireContext(), "Go to Permissions and enable Location permission.", Toast.LENGTH_LONG).show()
                        openPermissionSettings()
                    }
                    .onShouldShowRationale { strings, permissionNonce ->
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
