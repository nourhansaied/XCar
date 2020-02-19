package com.victoria.customer.ui.authentication.fragments

import android.arch.lifecycle.ViewModelProviders
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Validator
import com.victoria.customer.data.pojo.ResponseBody
import com.victoria.customer.data.pojo.User
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.authentication.viewmodel.ForgotPasswordViewModel
import com.victoria.customer.ui.base.BaseFragment
import kotlinx.android.synthetic.main.dialog_forgot_password_layout.*
import javax.inject.Inject


class ForgotPasswordFragment : BaseFragment() {


    @Inject
    lateinit var validator: Validator


    override fun createLayout(): Int {
        return R.layout.dialog_forgot_password_layout
    }


    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val forgotPasswordViewModel: ForgotPasswordViewModel  by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[ForgotPasswordViewModel::class.java]
    }

    override fun bindData() {
        observeForgotPasswordClickResponse()

        buttonReset.setOnClickListener(this::onViewClick)
        imageViewClose.setOnClickListener(this::onViewClick)
    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {
            R.id.buttonReset -> {
                if (checkValidation()) {
                    navigator.toggleLoader(true)
                    forgotPasswordViewModel.forgotPasswordApiCall(getForgotPasswordData())
                }
            }

            R.id.imageViewClose -> {
                hideKeyBoard()
                navigator.goBack()
            }
        }
    }

    private fun checkValidation(): Boolean {
        try {

            validator.submit(editTextEmail).checkEmpty().errorMessage(getString(R.string.validation_email))
                    .matchPatter(Common.EMAILREGEX).errorMessage(getString(R.string.validation_valid_email)).check()

            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showSnackBar(e.message)
        }
        return false

    }

    /**
     * Fp API calling stuff
     * */
    private fun observeForgotPasswordClickResponse() {
        forgotPasswordViewModel.forgotPassword.observe(this, { responseBody ->
            handleForgotPasswordResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun getForgotPasswordData(): Parameter {
        val parameter = Parameter()

        parameter.email = editTextEmail.text.toString()
        return parameter
    }

    private fun handleForgotPasswordResponse(responseBody: ResponseBody<User>) {
        navigator.toggleLoader(false)
        showMessage(responseBody.message)
        if (responseBody.responseCode == 1) {
            hideKeyBoard()
            navigator.goBack()
        }
    }
    /**
     * Fp API calling stuff
     * */
}
