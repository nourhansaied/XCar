package com.victoria.driver.ui.home.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.view.View
import com.ridewithme.util.ImagePicker
import com.squareup.picasso.Picasso
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.core.Validator
import com.victoria.driver.data.pojo.ResponseBody
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.exception.ApplicationException
import com.victoria.driver.ui.authentication.dialog.CountryCodeDialog
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.activity.HomeActivity
import com.victoria.driver.ui.interfaces.ISelectCountry
import com.victoria.driver.ui.model.CountryData
import com.victoria.driver.ui.model.ImageData
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.EditProfileViewModel
import com.victoria.driver.util.CircleTransform
import com.victoria.driver.util.CountryUtils
import kotlinx.android.synthetic.main.fragment_profile_driver.*
import kotlinx.android.synthetic.main.toolbar_profile.*
import java.io.File
import javax.inject.Inject

class DriverProfileFragmet : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    lateinit var imagePicker1: ImagePicker

    private lateinit var countryCodeDialog: CountryCodeDialog

    lateinit var homeActivity: HomeActivity
    lateinit var filePath: String

    override fun createLayout(): Int = R.layout.fragment_profile_driver

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }


    private val editProfileViewModel: EditProfileViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[EditProfileViewModel::class.java]
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is HomeActivity) {
            homeActivity = context
        }
    }


    override fun bindData() {
        toolBarText.text = getString(R.string.toolbar_title_profile)
        observeEditProfileClickResponse()
        observeGetProfileResponse()
        observeUploadProfileClickResponse()

        countryCodeDialog = CountryCodeDialog()

        appCompatImageView2.setImageDrawable(ContextCompat.getDrawable(this!!.context!!, R.drawable.arrow_back))


        textViewSave.setOnClickListener(this::onViewClick)
        imageViewEditProfile.setOnClickListener(this::onViewClick)
        editCountryCode.setOnClickListener(this::onViewClick)
        textViewRating.setOnClickListener(this::onViewClick)
        appCompatImageView2.setOnClickListener { navigator.goBack() }

    }

    private fun setProfileDataFromSession() {
        val user = session.user
        Picasso.get()
                .load(user?.profileImageThumb)
                .transform(CircleTransform())
                .into(imageViewProfile)
        editTextFirstName.setText(user?.firstName)
        editTextLastName.setText(user?.lastName)
        editTextEmail.setText(user?.email)
        textViewRating.text = ((user?.rating).toString())
        textViewUniqueId.text = ("Unique Id : " + session.user?.driverNo)
        editTextPlateNumber.setText(user?.vehicleNumber)
        for (countryCOde in CountryUtils.readCountryJson(activity)) {

            if (user?.countryCode?.subSequence(1, user.countryCode?.length!!)?.equals(countryCOde.e164Cc)!!) {

                editCountryCode.setText("+" + countryCOde.e164Cc)
                editCountryCode.setCompoundDrawablesWithIntrinsicBounds(0,
                        0, CountryUtils.getFlagDrawableResId(countryCOde), 0)

                break
            }
        }

        editTextPhoneNumber.setText(user?.phone)
        textViewUserName.text = user?.firstName + " " + user?.lastName
        editTextAddress.setText(user?.address.toString())
        if (user?.gender.equals(Common.MALE)) {
            radioButtonMale.isChecked = true
        } else if (user?.gender.equals(Common.FEMALE)) {
            radioButtonFeMale.isChecked = true
        }


        when {
            user?.allowGender.equals(Common.BOTH) -> {
                userRadioButtonFeMale.isChecked = true
                userRadioButtonMale.isChecked = true

            }
            user?.allowGender.equals(Common.MALE) -> userRadioButtonMale.isChecked = true
            user?.allowGender.equals(Common.FEMALE) -> userRadioButtonFeMale.isChecked = true
        }
    }

    override fun onViewClick(view: View) {

        when (view.id) {

            R.id.textViewSave -> {
                if (checkValidation()) {
                    navigator.toggleLoader(true)


                    if (::filePath.isInitialized && !filePath.isNullOrEmpty()) {
                        editProfileViewModel.uploadImage(filePath)
                    } else {
                        editProfileViewModel.editProfileApiCall(getEditProfileData(""))
                    }

                }
            }

            R.id.imageViewEditProfile -> {

                imagePicker1 = ImagePicker()
                imagePicker1.setImagePickerResult(object : ImagePicker.ImagePickerResult {

                    override fun onResult(path: String) {


                        filePath = path

                        Uri.fromFile(File(filePath))
                        var imageFile = File(filePath)
                        Picasso.get().load(imageFile).transform(CircleTransform()).into(imageViewProfile)

                    }

                })

                imagePicker1.show(fragmentManager, "")

            }
            R.id.editCountryCode -> {

                hideKeyBoard()
                countryCodeDialog = CountryCodeDialog()
                countryCodeDialog.setCallback(object : ISelectCountry {
                    override fun selectCountry(countryCode: String?, country: String?, id: CountryData?) {
                        editCountryCode.setText("+$countryCode")
                        editCountryCode.setCompoundDrawablesWithIntrinsicBounds(0, 0, CountryUtils.getFlagDrawableResId(id), 0)
                    }
                })
                countryCodeDialog.show(fragmentManager, "")
            }

            R.id.textViewRating -> {

                navigator.load(RatingListFragment::class.java).replace(true)
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

            validator.submit(editCountryCode).checkEmpty().errorMessage(getString(R.string.validation_country_code)).check()
            validator.submit(editTextPhoneNumber).checkEmpty().errorMessage(getString(R.string.validation_empty_phone))
                    .checkMinDigits(6).errorMessage(getString(R.string.validation_valid_phone_number)).check()

            if (!userRadioButtonMale.isChecked && !userRadioButtonFeMale.isChecked) {
                showMessage(getString(R.string.validation_rider_gender))
                return false
            }

            if (session.user?.gender.equals(Common.MALE) && !userRadioButtonMale.isChecked && userRadioButtonFeMale.isChecked) {

                showMessage(getString(R.string.validation_for_male_driver))
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
     * Edit Profile calling stuff
     * */
    private fun observeEditProfileClickResponse() {
        editProfileViewModel.editProfile.observe(this, { responseBody ->
            handleEditProfileResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    /**
     * Get Profile Details get stuff
     * */
    private fun observeGetProfileResponse() {
        editProfileViewModel.userData.observe(this, { responseBody ->
            navigator.toggleLoader(false)

            if (responseBody.responseCode == 1) {

                session.user = responseBody.data
                session.userId = responseBody.data?.driverId.toString()
                setProfileDataFromSession()
            }
        }, {
            navigator.toggleLoader(false);true
        })

        val parameter = Parameter()
        parameter.user_type = Common.DRIVER
        parameter.userId = session.user?.driverId.toString()

        navigator.toggleLoader(true)
        editProfileViewModel.userDetailApiCall(parameter)

    }

    private fun getEditProfileData(profileImage: String): Parameter {
        val parameter = Parameter()

        parameter.first_name = editTextFirstName.text.toString()
        parameter.last_name = editTextLastName.text.toString()
        parameter.address = editTextAddress.text.toString()
        if (radioButtonMale.isChecked) {
            parameter.gender = Common.MALE!!
        } else {
            parameter.gender = Common.FEMALE!!
        }

        if (userRadioButtonMale.isChecked && userRadioButtonFeMale.isChecked) {
            parameter.allowGender = Common.BOTH!!
        } else if (userRadioButtonMale.isChecked) {
            parameter.allowGender = Common.MALE!!
        } else if (userRadioButtonFeMale.isChecked) {
            parameter.allowGender = Common.FEMALE!!
        }

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
            navigator.load(HomeFragment::class.java).replace(false)
        }
    }

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
    /**
     * Upload Profile API calling stuff
     * */

}