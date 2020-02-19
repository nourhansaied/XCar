package com.victoria.driver.ui.authentication.fragment


import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.victoria.driver.R
import com.victoria.driver.core.Validator
import com.victoria.driver.data.pojo.ResponseBody
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.exception.ApplicationException
import com.victoria.driver.ui.authentication.adapter.CarTypesAdapter
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.model.VehicleList
import com.victoria.driver.ui.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_edit_vehicle_info_layout.*
import kotlinx.android.synthetic.main.toolbar_with_close.*
import javax.inject.Inject


open class EditVehicleInfoFragment : BaseFragment(), CarTypesAdapter.CallBackForCarTypeSelect {

    @Inject
    lateinit var validator: Validator

    private lateinit var carTypeList: ArrayList<VehicleList>
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private var vehicleIdUpdated = ""

    override fun createLayout(): Int = R.layout.fragment_edit_vehicle_info_layout

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val signUpViewModel: SignUpViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[SignUpViewModel::class.java]
    }

    override fun bindData() {

        toolBarText.text = getString(R.string.edit_vehicle_info)
        carTypeList = ArrayList()

        observeVehicleListResponse()
        observeUpdateVehicleClickResponse()

        setDataFromSession()

        signUpViewModel.vehicleListApi()


        textInputLayoutCarType.setOnClickListener { onViewClick(it) }
        editTextCarType.setOnClickListener { onViewClick(it) }
        imageViewBack.setOnClickListener { onViewClick(it) }
        buttonViewNext.setOnClickListener { onViewClick(it) }
    }

    private fun setDataFromSession() {

        editTextCarMake.setText(session.user?.vehicleBrand)
        editTextCarModel.setText(session.user?.vehicleModel)
        editTextCarColor.setText(session.user?.vehicleColor)
        editTextPlateNumber.setText(session.user?.vehicleNumber)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)

        when (view.id) {
            R.id.textInputLayoutCarType -> {
                showCarTypeDialog()
            }

            R.id.editTextCarType -> {
                showCarTypeDialog()
            }

            R.id.buttonViewNext -> {
                if (checkValidation()) {
                    navigator.toggleLoader(true)

                    signUpViewModel.updateVehicleDocumentApi(getEditVehicleDocumentParams())
                }
            }
            R.id.imageViewBack -> {
                navigator.goBack()
            }
        }
    }


    override fun carSelectCallBack(position: Int) {
        super.carSelectCallBack(position)
        vehicleIdUpdated = carTypeList[position].vehicleId.toString()

        editTextCarType.setText(carTypeList[position].vehicle)
        mBottomSheetDialog!!.dismiss()
    }


    private fun showCarTypeDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_car_type, null)
        mBottomSheetDialog = BottomSheetDialog(context!!)

        val recyclerView = view.findViewById(R.id.recyclerViewCarType) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(activity)


        val carTypesAdapter = CarTypesAdapter(carTypeList, this.activity!!, this)
        recyclerView.adapter = carTypesAdapter

        mBottomSheetDialog!!.setContentView(view)
        BottomSheetBehavior.from(view.parent as View)
        mBottomSheetDialog!!.show()
    }

    private fun checkValidation(): Boolean {

        try {
            validator.submit(editTextCarMake).checkEmpty().errorMessage(getString(R.string.validation_empty_car_make))
                    .check()

            validator.submit(editTextCarModel).checkEmpty().errorMessage(getString(R.string.validation_empty_car_model))
                    .check()

            validator.submit(editTextCarColor).checkEmpty().errorMessage(getString(R.string.validation_empty_car_color))
                    .check()

            validator.submit(editTextPlateNumber).checkEmpty().errorMessage(getString(R.string.validation_empty_plat_no))
                    .check()
            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showMessage(e.message)
        }
        return false
    }

    /**
     * Get vehicle list stuff
     * */

    private fun observeVehicleListResponse() {
        signUpViewModel.vehicleListData.observe(this, { responseBody ->
            signUpViewModel.vehicleListData.removeObservers(this)
            handleVehicleListResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun handleVehicleListResponse(responseBody: ResponseBody<ArrayList<VehicleList>>) {
        navigator.toggleLoader(false)

        if (responseBody.responseCode == 1) {
            carTypeList.addAll(responseBody.data!!)
            for (i in carTypeList.indices) {
                if (carTypeList[i].vehicleId == session.user?.vehicleId) {
                    editTextCarType.setText(carTypeList[i].vehicle)
                    break
                }
            }

        } else {
            showMessage(responseBody.message)
        }
    }

    /**
     * Get vehicle list stuff
     * */

    /**
     * Update vehicle detail stuff
     * */

    private fun observeUpdateVehicleClickResponse() {
        signUpViewModel.updateVehicleDocument.observe(this, { responseBody ->
            signUpViewModel.updateVehicleDocument.removeObservers(this)
            handleUpdateVehicleClickResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun getEditVehicleDocumentParams(): Parameter {
        val parameter = Parameter()
        val user = session.user
        if (vehicleIdUpdated.isNotEmpty()) {
            user?.vehicleId = vehicleIdUpdated.toInt()
            parameter.vehicle_id = vehicleIdUpdated
        } else {
            parameter.vehicle_id = session.user?.vehicleId.toString()
        }

        parameter.vehicle_brand = editTextCarMake.text.toString()
        parameter.vehicle_model = editTextCarModel.text.toString()
        parameter.vehicle_number = editTextPlateNumber.text.toString()
        parameter.vehicle_color = editTextCarColor.text.toString()

        return parameter
    }

    private fun handleUpdateVehicleClickResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        showMessage(responseBody.message)
        if (responseBody.responseCode == 1) {
            session.user = responseBody.data
            navigator.goBack()
        } else {
            showMessage(responseBody.message)
        }
    }

    /**
     * Update vehicle detail stuff
     * */
}