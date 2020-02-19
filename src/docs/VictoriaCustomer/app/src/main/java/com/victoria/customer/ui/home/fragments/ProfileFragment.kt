package com.victoria.customer.ui.home.fragments

import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.view.View
import com.squareup.picasso.Picasso
import com.victoria.customer.core.Common
import com.victoria.customer.core.Validator
import com.victoria.customer.data.pojo.ResponseBody
import com.victoria.customer.data.pojo.User
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.model.ImageData
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.dialog.CountryCodeDialog
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.activity.HomeActivity
import com.victoria.customer.ui.home.settings.viewmodel.EditProfileViewModel
import com.victoria.customer.util.CircleTransform
import com.victoria.customer.util.CircleTransformPicasso
import com.victoria.customer.util.CountryUtils
import com.victoria.customer.util.ImagePicker1
import kotlinx.android.synthetic.main.fragment_profile_layout.*
import kotlinx.android.synthetic.main.toolbar_profile.*
import java.io.File
import javax.inject.Inject


class ProfileFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    lateinit var imagePicker1: ImagePicker1

    lateinit var filePath: String

    private lateinit var countryCodeDialog: CountryCodeDialog

    lateinit var homeActivity: HomeActivity
    lateinit var genderSelected: String

    override fun createLayout(): Int {
        return com.victoria.customer.R.layout.fragment_profile_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val editProfileViewModel: EditProfileViewModel  by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[EditProfileViewModel::class.java]
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is HomeActivity) {
            homeActivity = context
        }
    }

    override fun bindData() {
        observeGetProfileResponse()
        observeEditProfileClickResponse()
        observeUploadProfileClickResponse()
        appCompatImageView2.setImageDrawable(ContextCompat.getDrawable(this!!.context!!, com.victoria.customer.R.drawable.arrow_back))
        toolBarText.text = getString(com.victoria.customer.R.string.toolbar_title_profile)
        //  setProfileDataFromSession()

        countryCodeDialog = CountryCodeDialog()


        val locale = activity?.resources?.configuration?.locale?.country

        /* radioGroup.setOnCheckedChangeListener { group, checkedId ->
             when (checkedId) {
                 R.id.radioButtonMale -> genderSelected=Common.MALE!!
                 else -> genderSelected=Common.FEMALE!!
             }
         }*/



        textViewSave.setOnClickListener(this::onViewClick)
        imageViewEditProfile.setOnClickListener(this::onViewClick)
        editCountryCode.setOnClickListener(this::onViewClick)
        textViewRating.setOnClickListener(this::onViewClick)
        appCompatImageView2.setOnClickListener { navigator.goBack() }
        imageViewShare.setOnClickListener { shareTextUrl() }
    }

    private fun setProfileDataFromSession() {
        var user = session.user
        Picasso.get()
                .load(user?.profileImageThumb)
                .transform(CircleTransform())
                .into(imageViewProfile)
        editTextFirstName.setText(user?.firstName)
        editTextLastName.setText(user?.lastName)
        editTextEmail.setText(user?.email)
        for (countryCOde in CountryUtils.readCountryJson(activity)) {

            if (user?.countryCode?.subSequence(1, user?.countryCode?.length!!)?.equals(countryCOde.e164Cc)!!) {

                editCountryCode.setText("+" + countryCOde.e164Cc)
                editCountryCode.setCompoundDrawablesWithIntrinsicBounds(0,
                        0, CountryUtils.getFlagDrawableResId(countryCOde), 0)

                break
            }
        }

        if (user?.gender.equals(Common.MALE)) {
            radioButtonMale.isChecked = true
            genderSelected = Common.MALE!!
        } else {
            radioButtonFeMale.isChecked = true
            genderSelected = Common.FEMALE!!
        }

        textViewRating.text = user?.rating.toString()
        editTextPhoneNumber.setText(user?.phone)
        textViewReferralCode.text = user?.referralCode
        textViewUserName.text = user?.firstName + " " + user?.lastName
        textViewWalletBalance.text = "EGP " + user?.wallet.toString()
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            com.victoria.customer.R.id.textViewSave -> {
                if (checkValidation()) {
                    navigator.toggleLoader(true)

                    if (::filePath.isInitialized && !filePath.isNullOrEmpty()) {
                        editProfileViewModel.uploadImage(filePath)
                    } else {
                        editProfileViewModel.editProfileApiCall(getEditProfileData(""))
                    }
                }
            }

            com.victoria.customer.R.id.imageViewEditProfile -> {

                imagePicker1 = ImagePicker1()
                imagePicker1.setImagePickerResult(object : ImagePicker1.ImagePickerResult {

                    override fun onResult(path: String) {
                        filePath = path
                        Uri.fromFile(File(filePath))
                        var imageFile = File(filePath)
                        Picasso.get().load(imageFile).transform(CircleTransformPicasso()).into(imageViewProfile)

                    }

                    override fun onError(path: String, dialog: Dialog) {

                    }


                })

                imagePicker1.show(fragmentManager, "")

            }
            com.victoria.customer.R.id.editCountryCode -> {

                hideKeyBoard()
                /*countryCodeDialog = CountryCodeDialog()
                countryCodeDialog.setCallback(ISelectCountry { countryCode, country, id ->
                    editCountryCode.setText("+$countryCode")
                    editCountryCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, CountryUtils.getFlagDrawableResId(id), 0)
                })
                countryCodeDialog.show(fragmentManager, "")*/
            }
            com.victoria.customer.R.id.textViewRating -> {

                navigator.load(RatingListFragment::class.java).replace(true)
            }


        }
    }

    private fun checkValidation(): Boolean {

        try {

            validator.submit(editTextFirstName).checkEmpty().errorMessage(getString(com.victoria.customer.R.string.validation_empty_first_name))
                    .matchPatter(Common.FIRST_NAME_REGEX).errorMessage(getString(com.victoria.customer.R.string.validation_valid_first_name)).check()
            validator.submit(editTextLastName).checkEmpty().errorMessage(getString(com.victoria.customer.R.string.validation_empty_last_name))
                    .matchPatter(Common.FIRST_NAME_REGEX).errorMessage(getString(com.victoria.customer.R.string.validation_valid_last_name)).check()
            validator.submit(editTextEmail).checkEmpty().errorMessage(getString(com.victoria.customer.R.string.validation_email))
                    .matchPatter(Common.EMAILREGEX).errorMessage(getString(com.victoria.customer.R.string.validation_valid_email)).check()
            validator.submit(editCountryCode).checkEmpty().errorMessage(getString(com.victoria.customer.R.string.validation_country_code)).check()
            validator.submit(editTextPhoneNumber).checkEmpty().errorMessage(getString(com.victoria.customer.R.string.validation_empty_phone))
                    .checkMinDigits(6).errorMessage(getString(com.victoria.customer.R.string.validation_valid_phone_number)).check()


            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showSnackBar(e.message)
        }
        return false
    }

    /**
     * Get Profile Details get stuff
     * */
    private fun observeGetProfileResponse() {
        editProfileViewModel.userData.observe(this, { responseBody ->
            navigator.toggleLoader(false)

            if (responseBody.responseCode == 1) {

                session.user = responseBody.data
                session.userId = responseBody.data?.customerId.toString()
                setProfileDataFromSession()
            }
        }, {
            navigator.toggleLoader(false);true
        })

        val parameter = Parameter()
        parameter.user_type = Common.CUSTOMER
        parameter.userId = session.user?.customerId.toString()

        navigator.toggleLoader(true)
        editProfileViewModel.userDetailApiCall(parameter)

    }


    /**
     * Edit Profile calling stuff
     * */

    private fun observeEditProfileClickResponse() {
        editProfileViewModel.editProfile.observe(this, { responseBody ->
            handleEditProfileResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun getEditProfileData(profileImage: String): Parameter {
        val parameter = Parameter()
        //first_name,last_name,profile_image

        parameter.first_name = editTextFirstName.text.toString()
        parameter.last_name = editTextLastName.text.toString()
        //parameter.gender=genderSelected

        if (!profileImage.isNullOrEmpty())
            parameter.profileImage = profileImage
        return parameter
    }

    private fun handleEditProfileResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        showMessage(responseBody.message)
        if (responseBody.responseCode == 1) {
            session.user = responseBody.data
            homeActivity.updateDetail()
            navigator.load(HomeStartFragment::class.java).clearHistory().replace(false)
        }
    }

    /**
     * Edit Profile API calling stuff
     * */

    /**
     * Upload Profile calling stuff
     * */

    private fun observeUploadProfileClickResponse() {
        editProfileViewModel.uploadImage.observe(this, { responseBody ->
            handleUploadProfileResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun handleUploadProfileResponse(responseBody: ResponseBody<ImageData>) {
        navigator.toggleLoader(false)
        if (responseBody.responseCode == 1) {
            navigator.toggleLoader(true)
            editProfileViewModel.editProfileApiCall(getEditProfileData(responseBody.data?.profile_image!!))
        }
    }


    private fun shareTextUrl() {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_SUBJECT, "Hey there ! Try this referral code to get discount")
        share.putExtra(Intent.EXTRA_TEXT, "Hey there ! Try this referral code to get discount in" + " http://play.google.com/store/apps/details?id=" + activity?.packageName + " Referral code is : " + textViewReferralCode.text.toString())

        startActivity(Intent.createChooser(share, "Share link!"))
    }


    /**
     * Upload Profile API calling stuff
     * */
}
