package com.victoria.customer.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Validator
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.dialog.ExpiryDateBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_add_credit_card_layout.*
import kotlinx.android.synthetic.main.toolbar_with_close.*
import javax.inject.Inject


class AddCreditCardFragment : BaseFragment() {

    @Inject
    lateinit var validator: Validator

    private var expiryDate = ""
    private var monthSet = ""
    private var yearSet = ""
    private var expiryDateBottomSheetFragment: ExpiryDateBottomSheetFragment? = null

    override fun createLayout(): Int {
        return R.layout.fragment_add_credit_card_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        toolBarText.text = getString(R.string.toolbar_title_add_credit_card)
        expiryDateBottomSheetFragment = ExpiryDateBottomSheetFragment(onSelectedListener)

        imageViewBack.setOnClickListener(this::onViewClick)
        buttonAddCard.setOnClickListener(this::onViewClick)
        editTextExpiryDate.setOnClickListener(this::onViewClick)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_credit_card_layout, container, false)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewBack -> {
                navigator.goBack()
            }

            R.id.buttonAddCard -> {
                if (checkValidation()) {
                    showMessage(getString(R.string.success_card_added))
                    navigator.goBack()
                }
            }

            R.id.editTextExpiryDate -> {
                editTextExpiryDate.setOnClickListener {
                    expiryDateBottomSheetFragment?.show(childFragmentManager, ExpiryDateBottomSheetFragment::class.java.simpleName)
                }
            }
        }
    }

    private val onSelectedListener = object : ExpiryDateBottomSheetFragment.OnSelectedListener {
        override fun onSelected(month: Int, year: Int) {
            if (month < 10) {
                expiryDate = "0$month/$year"
                monthSet = "0$month"
            } else {
                expiryDate = month.toString() + "/" + year
                monthSet = month.toString()
            }
            yearSet = year.toString()
            editTextExpiryDate.setText(expiryDate)
            editTextCvv.requestFocus()
        }
    }

    private fun checkValidation(): Boolean {
        try {
            validator.submit(editTextCardHolderName)
                    .checkEmpty().errorMessage(getString(R.string.card_holder_validation)).matchPatter(Common.CARD_NAME_REGEX).errorMessage(getString(R.string.validation_valid_card_holder_name))
                    .check()

            validator.submit(editTextCardNumber)
                    .checkEmpty().errorMessage(getString(R.string.card_number_validation)).checkMinDigits(16).errorMessage(getString(R.string.validation_valid_card))
                    .check()

            validator.submit(editTextExpiryDate)
                    .checkEmpty().errorMessage(getString(R.string.expiry_date_validation))
                    .check()

            validator.submit(editTextCvv)
                    .checkEmpty().errorMessage(getString(R.string.cvv_validation))
                    .check()
            return true
        } catch (e: ApplicationException) {
            hideKeyBoard()
            showSnackBar(e.message)
        }
        return false
    }
}
