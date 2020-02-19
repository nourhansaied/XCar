package com.victoria.driver.ui.home.fragment


import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.ridewithme.util.ImagePicker
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.core.Validator
import com.victoria.driver.data.pojo.ResponseBody
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.exception.ApplicationException
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.home.adapter.ImageListAdapter
import com.victoria.driver.ui.interfaces.OnItemClickListener
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.ContactUsViewModel
import kotlinx.android.synthetic.main.fragment_contact_us_layout.*
import kotlinx.android.synthetic.main.toolbar_with_close.*
import java.io.File
import javax.inject.Inject


class ContactUsFragment : BaseFragment(), OnItemClickListener<Int> {


    lateinit var imageList: ArrayList<File>

    lateinit var imageStringList: ArrayList<String>


    lateinit var imageListAdapter: ImageListAdapter

    @Inject
    lateinit var validator: Validator

    override fun createLayout(): Int {
        return R.layout.fragment_contact_us_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val contactUsViewModel: ContactUsViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[ContactUsViewModel::class.java]
    }

    override fun onResume() {

        super.onResume()
        activity!!.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity!!.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun bindData() {

        toolBarText.text = getString(R.string.toolbar_title_contact_us)
        imageStringList = ArrayList()
        observeContactUsImageUpload()
        observeContactUsDone()

        editTextCommentBox.setOnTouchListener { v, event ->
            if (v.id == R.id.editTextCommentBox) {
                v.parent.requestDisallowInterceptTouchEvent(true)
                when (event.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            false
        }

        imageViewBack.setOnClickListener(this::onViewClick)
        buttonSend.setOnClickListener(this::onViewClick)
        imageViewAddPhoto.setOnClickListener(this::onViewClick)

        setContactUsAdapter()

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_us_layout, container, false)


    }


    override fun onViewClick(view: View) {
        when (view.id) {

            R.id.imageViewBack -> {

                navigator.goBack()
            }

            R.id.buttonSend -> {
                if (checkValidation()) {
                    navigator.toggleLoader(true)
                    if (imageStringList.isNotEmpty())
                        contactUsViewModel.contactUsPhotoUploadApiCall(imageStringList)
                    else
                        contactUsViewModel.contactUsApiCall(getContactUsData())

                }
            }


            R.id.imageViewAddPhoto -> {

                var imagePicker1 = ImagePicker()

                imagePicker1.setImagePickerResult(object : ImagePicker.ImagePickerResult {

                    override fun onResult(path: String) {

                        var filePath = path

                        Uri.fromFile(File(filePath))
                        var imageFile = File(filePath)

                        imageList.add(imageFile)
                        imageStringList.add(path)
                        imageListAdapter.notifyDataSetChanged();
                    }

                    fun onError(path: String, dialog: Dialog) {


                    }


                })

                imagePicker1.show(fragmentManager, "")
            }
        }
    }


    private fun setContactUsAdapter() {
        imageList = ArrayList()
        val adapter = ImageListAdapter(imageList, context!!, this)
        this.imageListAdapter = adapter
        recycleVieImages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycleVieImages.adapter = adapter
    }

    private fun checkValidation(): Boolean {

        try {
            validator.submit(editTextSubject).checkEmpty().errorMessage(getString(R.string.validation_empty_subject)).check()

            validator.submit(editTextEmail).checkEmpty().errorMessage(getString(R.string.validation_email))
                    .matchPatter(Common.EMAILREGEX).errorMessage(getString(R.string.validation_valid_email)).check()


            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showMessage(e.message)
        }
        return false
    }

    override fun onItemClicked(t: Int) {

        if (fragmentManager != null) {
            var imagePicker1 = ImagePicker()

            imagePicker1.setImagePickerResult(object : ImagePicker.ImagePickerResult {

                override fun onResult(path: String) {

                    var filePath = path

                    Uri.fromFile(File(filePath))
                    var imageFile = File(filePath)

                    imageList.add(imageFile)
                    imageStringList.add(path)
                    imageListAdapter.notifyDataSetChanged();
                }

                fun onError(path: String, dialog: Dialog) {

                }


            })

            imagePicker1.show(fragmentManager, "")
        }
    }

    override fun onDeleteClicked(t: Int) {
    }

    override fun onItemClicked(adapterPosition: Int, checked: Int) {
    }

    override fun onDeleteClicked(adapterPosition: Int, b: Int) {
    }


    /**
     * Contact Us API calling stuff
     * */

    private fun observeContactUsDone() {
        contactUsViewModel.contactUsPhotoUpload.observe(this, { responseBody ->
            handleUploadImageResponse(responseBody)
        }, { throwable ->
            navigator.toggleLoader(false);true
        })
    }

    private fun handleUploadImageResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)

        if (responseBody.responseCode == 1) {
            navigator.toggleLoader(true)
            contactUsViewModel.contactUsApiCall(getContactUsData())
        } else {
            showMessage(responseBody.message)
        }
    }

    private fun observeContactUsImageUpload() {
        contactUsViewModel.contactUs.observe(this, { responseBody ->
            handleContactUsResponse(responseBody)
        }, { throwable ->
            navigator.toggleLoader(false);true
        })
    }

    private fun handleContactUsResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)

        if (responseBody.responseCode == 1) {
            showMessage(responseBody.message)
            navigator.goBack()
        } else {
            showMessage(responseBody.message)
        }
    }

    private fun getContactUsData(): Parameter {

        val parameter = Parameter()
        var user = session.user
        parameter.user_type = Common.DRIVER
        parameter.subject = editTextSubject.text.toString()
        parameter.email = editTextEmail.text.toString()
        parameter.message = editTextCommentBox.text.toString()

        return parameter
    }

    /**
     * Contact Us API calling stuff
     * */

}
