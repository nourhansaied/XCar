package com.victoria.driver.ui.authentication.fragment

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.AppCompatImageView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.ridewithme.util.ImagePicker
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.data.pojo.ResponseBody
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.model.CarDocuments
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_upload_vehicle_documents.*
import kotlinx.android.synthetic.main.toolbar_with_close.*


class EditVehicleDocumentFragment : BaseFragment(), ImagePicker.ImagePickerResult {

    private var pathCarBack: String = ""
    private var pathLicence: String = ""
    private var pathRegistration: String = ""
    private var pathCarFront: String = ""
    private val parameter = HashMap<String, String>()

    private var listOfPath = ArrayList<String>()
    private var listOfKey = ArrayList<String>()

    override fun createLayout(): Int {
        return R.layout.fragment_upload_vehicle_documents
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val signUpViewModel: SignUpViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[SignUpViewModel::class.java]
    }

    override fun bindData() {
        observeUploadDocumentClick()
        observeUpdateDocument()

        listOfPath = ArrayList()
        listOfKey = ArrayList()

        toolBarText.text = getString(R.string.edit_vehicle_doc)

        imageViewLicense.setOnClickListener { onViewClick(it) }
        imageViewCarRegistration.setOnClickListener { onViewClick(it) }
        imageViewCarFrontPhoto.setOnClickListener { onViewClick(it) }
        imageViewCarBackPhoto.setOnClickListener { onViewClick(it) }
        buttonViewNext.setOnClickListener { onViewClick(it) }
        imageViewBack.setOnClickListener { onViewClick(it) }

        setDataFromSession()
    }

    private fun setDataFromSession() {

        setRoundedCornerImage(imageViewLicense, session.user?.drivingLicense.toString())
        setRoundedCornerImage(imageViewCarRegistration, session.user?.registrationImage.toString())
        setRoundedCornerImage(imageViewCarFrontPhoto, session.user?.carFrontimage.toString())
        setRoundedCornerImage(imageViewCarBackPhoto, session.user?.carBackimage.toString())
    }

    private var selected: Int = 0
    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewLicense -> {

                selected = 1
                val imagePicker = ImagePicker()
                imagePicker.setImagePickerResult(this)
                imagePicker.show(activity?.supportFragmentManager, "1")

            }
            R.id.imageViewCarRegistration -> {

                selected = 2
                val imagePicker = ImagePicker()
                imagePicker.setImagePickerResult(this)
                imagePicker.show(activity?.supportFragmentManager, "2")

            }

            R.id.imageViewCarFrontPhoto -> {

                selected = 3
                val imagePicker = ImagePicker()
                imagePicker.setImagePickerResult(this)
                imagePicker.show(activity?.supportFragmentManager, "3")

            }
            R.id.imageViewCarBackPhoto -> {

                selected = 4
                val imagePicker = ImagePicker()
                imagePicker.setImagePickerResult(this)
                imagePicker.show(activity?.supportFragmentManager, "4")

            }

            R.id.buttonViewNext -> {

                when {
                    pathLicence != "" -> parameter.put(Common.DRIVING_LICENCE, pathLicence)
                    pathRegistration != "" -> parameter.put(Common.REGISTRATION_IMAGE, pathRegistration)
                    pathCarFront != "" -> parameter.put(Common.CAR_FRONT_IMAGE, pathCarFront)
                    pathCarBack != "" -> parameter.put(Common.CAR_BACK_IMAGE, pathCarBack)
                }

                if (parameter.isNotEmpty()) {
                    navigator.toggleLoader(true)

                    signUpViewModel.updateDocumentApi(parameter)
                } else {
                    navigator.goBack()
                }
            }

            R.id.imageViewBack -> {
                navigator.goBack()
            }
        }
    }


    override fun onResult(path: String) {
        when (selected) {
            1 -> {

                pathLicence = path
                setRoundedCornerImage(imageViewLicense, path)
            }
            2 -> {

                pathRegistration = path
                setRoundedCornerImage(imageViewCarRegistration, path)
            }
            3 -> {

                pathCarFront = path
                setRoundedCornerImage(imageViewCarFrontPhoto, path)
            }
            else -> {

                pathCarBack = path
                setRoundedCornerImage(imageViewCarBackPhoto, path)
            }
        }
    }


    private fun setRoundedCornerImage(imageView: AppCompatImageView, path: Any) {

        Glide.with(context!!)
                .load(path)
                .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(40)))
                .into(imageView)
    }


    /**
     * Upload document stuff
     * */

    private fun observeUploadDocumentClick() {

        signUpViewModel.updateDocument.observe(this, { responseBody ->
            signUpViewModel.updateDocument.removeObservers(this)
            handleUploadDocResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
        //driver_id, bank_name, account_holder_name, account_number, routing_number
    }

    private fun handleUploadDocResponse(responseBody: ResponseBody<CarDocuments>) {

        navigator.toggleLoader(false)

        if (responseBody.responseCode == 1) {
            val parameter = Parameter()
            if (responseBody.data?.drivingLicense != null)
                parameter.driving_license = responseBody.data.drivingLicense!!
            if (responseBody.data?.carFrontimage != null)
                parameter.car_frontimage = responseBody.data.carFrontimage!!
            if (responseBody.data?.carBackimage != null)
                parameter.car_backimage = responseBody.data.carBackimage!!
            if (responseBody.data?.registrationImage != null)
                parameter.registration_image = responseBody.data.registrationImage!!
            navigator.toggleLoader(true)
            signUpViewModel.updateDocumentEdit(parameter)
        } else {
            showMessage(responseBody.message)
        }
    }

    private fun observeUpdateDocument() {

        signUpViewModel.updateDocumentEdit.observe(this, { responseBody ->
            signUpViewModel.updateDocumentEdit.removeObservers(this)
            handleUpdateDocumentResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
        //driver_id, bank_name, account_holder_name, account_number, routing_number
    }

    private fun handleUpdateDocumentResponse(responseBody: ResponseBody<User>) {

        navigator.toggleLoader(false)

        if (responseBody.responseCode == 1) {
            session.user = responseBody.data
            showMessage(responseBody.message)
            navigator.goBack()
        } else {
            showMessage(responseBody.message)
        }
    }
    /**
     * Upload document stuff
     * */

}
