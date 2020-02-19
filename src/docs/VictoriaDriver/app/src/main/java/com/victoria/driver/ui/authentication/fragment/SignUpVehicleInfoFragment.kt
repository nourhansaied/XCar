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
import kotlinx.android.synthetic.main.fragment_signup_vehicle_info_layout.*
import javax.inject.Inject

open class SignUpVehicleInfoFragment : BaseFragment(), CarTypesAdapter.CallBackForCarTypeSelect {

    @Inject
    lateinit var validator: Validator

    private lateinit var carTypeList: ArrayList<VehicleList>

    private var mBottomSheetDialog: BottomSheetDialog? = null
    private var vehicleId = ""

    override fun createLayout(): Int = R.layout.fragment_signup_vehicle_info_layout

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val signUpViewModel: SignUpViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[SignUpViewModel::class.java]
    }

    override fun bindData() {
        observeVehicleListResponse()
        observeAddVehicleDetailClick()

        signUpViewModel.vehicleListApi()
        carTypeList = ArrayList()

        textInputLayoutCarType.setOnClickListener { onViewClick(it) }
        editTextCarType.setOnClickListener { onViewClick(it) }
        imageViewNext.setOnClickListener { onViewClick(it) }
        imageViewClose.setOnClickListener { onViewClick(it) }
    }


    override fun onViewClick(view: View) {
        when (view.id) {
            R.id.imageViewClose -> navigator.goBack()

            R.id.textInputLayoutCarType -> {
                showCarTypeDialog()
            }

            R.id.editTextCarType -> {
                showCarTypeDialog()
            }

            R.id.imageViewNext -> {
                if (checkValidation()) {
                    navigator.toggleLoader(true)

                    signUpViewModel.addVehicleDataApi(getVehicleData())
                }
            }
        }
    }

    override fun carSelectCallBack(position: Int) {
        super.carSelectCallBack(position)
        vehicleId = carTypeList[position].vehicleId.toString()
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

            validator.submit(editTextCarType).checkEmpty().errorMessage(getString(R.string.validation_empty_car_type))
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
            //editTextCarType.setText(carTypeList[0].vehicle)
        } else {
            showMessage(responseBody.message)
        }
    }

    /**
     * Get vehicle list stuff
     * */

    /**
     * Add vehicle list stuff
     * */

    private fun observeAddVehicleDetailClick() {
        signUpViewModel.addVehicleDetailData.observe(this, { responseBody ->
            signUpViewModel.addVehicleDetailData.removeObservers(this)
            handleAddVehicleDataResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun handleAddVehicleDataResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)

        if (responseBody.responseCode == 1) {
            showMessage(responseBody.message)
            session.user = responseBody.data
            navigator.load(UploadVehicleDocumentFragment::class.java).replace(true)
        } else {
            showMessage(responseBody.message)
        }
    }

    private fun getVehicleData(): Parameter {
        val parameter = Parameter()
        val user = session.user
        parameter.driver_id = user?.driverId.toString()
        parameter.vehicle_id = vehicleId
        parameter.vehicle_brand = editTextCarMake.text.toString()
        parameter.vehicle_model = editTextCarModel.text.toString()
        parameter.vehicle_number = editTextPlateNumber.text.toString()
        parameter.vehicle_color = editTextCarColor.text.toString()

        return parameter
    }


    /**
     * Add vehicle list stuff
     * */
}
