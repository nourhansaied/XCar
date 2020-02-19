package com.victoria.customer.ui.authentication.fragments

import android.content.Intent
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.core.Validator
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.exception.ApplicationException
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.activity.HomeActivity
import com.victoria.customer.util.PARAMETERS
import kotlinx.android.synthetic.main.fragment_signup_2_layout.*
import javax.inject.Inject


class SignupBasicDetail2Fragment : BaseFragment() {


    @Inject
    lateinit var validator: Validator



    override fun createLayout(): Int {
        return R.layout.fragment_signup_2_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {

        val spannable = SpannableString(getString(R.string.terms_condition))

        spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.text_pink)), 15, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.text_pink)), 37, 51, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }

            override fun onClick(widget: View) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Common.URL_GOOGLE)))
            }
        }, 15, 33, 0)

        spannable.setSpan(object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }

            override fun onClick(widget: View) {

                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Common.URL_GOOGLE)))

            }
        }, 37, 51, 0)
        textViewTermsConditions.text = spannable
        //checkBox.movementMethod = LinkMovementMethod()
        imageViewNext.setOnClickListener(this::onViewClick)
        imageViewClose.setOnClickListener(this::onViewClick)
    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewNext -> {

                if (checkValidation()) {
                    appPreferences.putBoolean(PARAMETERS.KEY_IS_LOGIN, true)

                    navigator.loadActivity(HomeActivity::class.java).byFinishingCurrent().start()
                }
            }

            R.id.imageViewClose -> {
                navigator.goBack()
            }


        }
    }

    private fun checkValidation(): Boolean {

        try {

            validator.submit(editTextEmail).checkEmpty().errorMessage(getString(R.string.validation_email))
                    .matchPatter(Common.EMAILREGEX).errorMessage(getString(R.string.validation_valid_email)).check()


            validator.submit(editTextPassword).checkEmpty().errorMessage(getString(R.string.validation_empty_password))
                    .checkMinDigits(4).errorMessage(getString(R.string.validation_valide_password)).check()



            if (!checkBox.isChecked) {
                showSnackBar(getString(R.string.validation_terms_condition))
                return false
            }

            return true

        } catch (e: ApplicationException) {
            hideKeyBoard()
            showSnackBar(e.message)
        }
        return false
    }


}
