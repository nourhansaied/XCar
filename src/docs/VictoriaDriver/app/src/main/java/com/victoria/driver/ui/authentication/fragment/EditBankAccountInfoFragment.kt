package com.victoria.driver.ui.authentication.fragment


import android.arch.lifecycle.ViewModelProviders
import android.view.View
import com.victoria.driver.R
import com.victoria.driver.core.Validator
import com.victoria.driver.data.pojo.ResponseBody
import com.victoria.driver.data.pojo.User
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.exception.ApplicationException
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.model.Parameter
import com.victoria.driver.ui.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_edit_bank_detail.*
import kotlinx.android.synthetic.main.toolbar_with_close.*
import javax.inject.Inject


open class EditBankAccountInfoFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    override fun createLayout(): Int = R.layout.fragment_edit_bank_detail

    override fun inject(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)

    private val signUpViewModel: SignUpViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[SignUpViewModel::class.java]
    }

    override fun bindData() {

        observeAddBankDetailClick()
        setDataFromSession()

        toolBarText.text = getString(R.string.edit_bank_detail)
        buttonViewNext.setOnClickListener { onViewClick(it) }
        imageViewBack.setOnClickListener { onViewClick(it) }
    }

    private fun setDataFromSession() {

        editTextBankName.setText(session.user?.bankName)
        editTextAccountHolderNumber.setText(session.user?.accountHolderName)
        editTextAccountNumber.setText(session.user?.accountNumber)
        editTextRoutingNumber.setText(session.user?.routingNumber)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)

        when (view.id) {
            R.id.buttonViewNext -> {
                if (checkValidation()) {

                    navigator.toggleLoader(true)
                    signUpViewModel.updateBankDetailApi(getAddBankData())
                }
            }

            R.id.imageViewBack -> {
                navigator.goBack()
            }
        }
    }


    private fun checkValidation(): Boolean {
        try {
            validator.submit(editTextBankName).checkEmpty().errorMessage(getString(R.string.validation_empty_bank_name))
                    .check()

            validator.submit(editTextAccountHolderNumber).checkEmpty().errorMessage(getString(R.string.validation_empty_account_holder_number))
                    .check()

            validator.submit(editTextAccountNumber).checkEmpty().errorMessage(getString(R.string.validation_empty_account_number))
                    .checkMinDigits(7).errorMessage(getString(R.string.validation_account_no))
                    .check()

            validator.submit(editTextRoutingNumber).checkEmpty().errorMessage(getString(R.string.validation_empty_routing_number))
                    .checkMinDigits(9).errorMessage(getString(R.string.valid_validation_for_routing_number))
                    .check()
            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showMessage(e.message)
        }
        return false
    }


    /**
     * Add bank detail stuff
     * */


    private fun observeAddBankDetailClick() {

        signUpViewModel.updateBankData.observe(this, { responseBody ->
            signUpViewModel.updateBankData.removeObservers(this)
            handleAddBankDetailResponse(responseBody)
        }, { throwable ->
            navigator.toggleLoader(false);true
        })
    }

    private fun getAddBankData(): Parameter {

        val parameter = Parameter()
        val user = session.user
        parameter.driver_id = user?.driverId.toString()
        parameter.bank_name = editTextBankName.text.toString()
        parameter.account_holder_name = editTextAccountHolderNumber.text.toString()
        parameter.account_number = editTextAccountNumber.text.toString()
        parameter.routing_number = editTextRoutingNumber.text.toString()

        return parameter
    }

    private fun handleAddBankDetailResponse(responseBody: ResponseBody<User>) {

        navigator.toggleLoader(false)
        showMessage(responseBody.message)

        if (responseBody.responseCode == 1) {
            session.user = responseBody.data
            navigator.goBack()
        }
    }
    /**
     * Add bank detail stuff
     * */
}