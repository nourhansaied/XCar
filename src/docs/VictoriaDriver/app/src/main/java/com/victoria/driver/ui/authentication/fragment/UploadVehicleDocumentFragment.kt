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
import kotlinx.android.synthetic.main.fragment_edit_vehicle_documents.*
import okhttp3.MediaType
import okhttp3.RequestBody


class UploadVehicleDocumentFragment : BaseFragment(), ImagePicker.ImagePickerResult {

    private var pathLicense: String = ""
    private var pathRegistration: String = ""
    private var pathCarFront: String = ""
    private var pathCarBack: String = ""
    private var selected: Int = 0


    override fun createLayout(): Int {
        return R.layout.fragment_edit_vehicle_documents
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val signUpViewModel: SignUpViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[SignUpViewModel::class.java]
    }

    override fun bindData() {

        observeUploadDocumentClick()
        observeAddDocumentClick()

        imageViewLicense.setOnClickListener { onViewClick(it) }
        imageViewCarRegistration.setOnClickListener { onViewClick(it) }
        imageViewCarFrontPhoto.setOnClickListener { onViewClick(it) }
        imageViewCarBackPhoto.setOnClickListener { onViewClick(it) }
        imageViewNext.setOnClickListener { onViewClick(it) }
        imageViewClose.setOnClickListener { onViewClick(it) }
    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewClose -> {
                //driving_license,registration_image,car_frontimage,car_backimage
                navigator.goBack()

            }

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

            R.id.imageViewNext -> {
                selected = 4
                if (pathLicense == "" || pathRegistration == "" || pathCarFront == "" || pathCarBack == "") {
                    showMessage(getString(R.string.validation_car_doc_))
                } else {
                    navigator.toggleLoader(true)

                    val hashMap: HashMap<String, RequestBody> = HashMap()
                    hashMap[Common.DRIVER_ID] = create(session.user?.driverId.toString())
                    signUpViewModel.uploadDocumentApi(hashMap, pathLicense, pathRegistration, pathCarFront, pathCarBack)
                }
            }
        }
    }

    internal fun create(s: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), s)
    }

    override fun onResult(path: String) {
        when (selected) {
            1 -> {
                pathLicense = path;
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
        signUpViewModel.uploadDocData.observe(this, { responseBody ->
            signUpViewModel.uploadDocData.removeObservers(this)
            handleUploadDocResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
        //driver_id, bank_name, account_holder_name, account_number, routing_number
    }

    private fun handleUploadDocResponse(responseBody: ResponseBody<CarDocuments>) {
        navigator.toggleLoader(false)

        if (responseBody.responseCode == 1) {
            navigator.toggleLoader(true)
            signUpViewModel.addDocumentDataApi(getAddDocumentsParams(responseBody.data))
            //session.user = responseBody.data
            //navigator.load(BankAccountInfoFragment::class.java).replace(true)
        } else {
            showMessage(responseBody.message)
        }
    }
    /**
     * Upload document stuff
     * */

    /**
     * Add document stuff
     * */

    private fun observeAddDocumentClick() {
        signUpViewModel.addDocumentData.observe(this, { responseBody ->
            signUpViewModel.addDocumentData.removeObservers(this)
            handleAddDocResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
        //driver_id, bank_name, account_holder_name, account_number, routing_number
    }

    private fun getAddDocumentsParams(data: CarDocuments?): Parameter {
        val parameter = Parameter()
        //driver_id,,,,
        parameter.driver_id = session.user?.driverId.toString()
        parameter.driving_license = data?.drivingLicense!!
        parameter.registration_image = data.registrationImage!!
        parameter.car_backimage = data.carBackimage!!
        parameter.car_frontimage = data.carFrontimage!!

        return parameter
    }

    private fun handleAddDocResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        showMessage(responseBody.message)

        if (responseBody.responseCode == 1) {
            session.userSession = responseBody.data!!.token
            session.user = responseBody.data
            navigator.load(BankAccountInfoFragment::class.java).replace(true)
        }
    }
    /**
     * Add document stuff
     * */
}
