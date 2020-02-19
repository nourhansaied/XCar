package com.victoria.customer.ui.authentication.fragments

import android.view.View
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Validator
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_signup_1_layout.*
import javax.inject.Inject


class SignupBasicDetail1Fragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    override fun createLayout(): Int {
        return R.layout.fragment_signup_1_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }


    override fun bindData() {
        imageViewNext.setOnClickListener(this::onViewClick)
        imageViewClose.setOnClickListener(this::onViewClick)
    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewNext -> {
            }

            R.id.imageViewClose -> {
                navigator.goBack()
            }

        }
    }

    private fun checkValidation(): Boolean {

        try {
            validator.submit(editTextFirstName).checkEmpty().errorMessage(getString(R.string.validation_empty_first_name))
                    .matchPatter(Common.FIRST_NAME_REGEX).errorMessage(getString(R.string.validation_valid_first_name)).check()

            validator.submit(editTextLastName).checkEmpty().errorMessage(getString(R.string.validation_empty_last_name))
                    .matchPatter(Common.FIRST_NAME_REGEX).errorMessage(getString(R.string.validation_valid_last_name)).check()

            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showSnackBar(e.message)
        }
        return false
    }

}
