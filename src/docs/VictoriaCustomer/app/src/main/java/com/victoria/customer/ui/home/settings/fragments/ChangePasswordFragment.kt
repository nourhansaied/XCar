package com.victoria.customer.ui.home.settings.fragments

import android.arch.lifecycle.ViewModelProviders
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.core.Validator
import com.victoria.customer.data.pojo.ResponseBody
import com.victoria.customer.data.pojo.User
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.settings.viewmodel.ChangePasswordViewModel
import kotlinx.android.synthetic.main.fragment_change_password_layout.*
import javax.inject.Inject


class ChangePasswordFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    override fun createLayout(): Int {
        return R.layout.fragment_change_password_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val changePasswordViewModel: ChangePasswordViewModel  by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[ChangePasswordViewModel::class.java]
    }

    override fun bindData() {

        observeChangePasswordClickResponse()

        imageViewClosed.setOnClickListener(this::onViewClick)
        buttonSetPassword.setOnClickListener(this::onViewClick)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.buttonSetPassword -> {
                if (checkValidation()) {
                    navigator.toggleLoader(true)
                    changePasswordViewModel.changePasswordApiCall(getChangePasswordData())
                }

            }

            R.id.imageViewClosed -> {

                navigator.goBack()
            }
        }
    }

    private fun checkValidation(): Boolean {

        try {

            validator.submit(editTextOldPassword).checkEmpty().errorMessage(getString(R.string.validation_empty_old_password)).check()


            validator.submit(editTextNewPassword).checkEmpty().errorMessage(getString(R.string.validation_empty_new_password))
                    .checkMinDigits(4).errorMessage(getString(R.string.validation_valid_new_password)).check()


            validator.submit(editTextConfirmPassword).checkEmpty().errorMessage(getString(R.string.confirm_password_validation))
                    .matchString(editTextNewPassword.text.toString()).errorMessage(getString(R.string.password_mismatch_validation)).check()

            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showSnackBar(e.message)
        }
        return false
    }

    /**
     * Cp API calling stuff
     * */
    private fun observeChangePasswordClickResponse() {
        changePasswordViewModel.changePassword.observe(this, { responseBody ->
            handleChangePasswordResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun getChangePasswordData(): Parameter {
        val parameter = Parameter()
        parameter.oldPassword = editTextOldPassword.text.toString()
        parameter.password = editTextConfirmPassword.text.toString()
        return parameter
    }

    private fun handleChangePasswordResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        showMessage(responseBody.message)
        if (responseBody.responseCode == 1) {
            navigator.goBack()
        }
    }
    /**
     * Cp API calling stuff
     * */

}
