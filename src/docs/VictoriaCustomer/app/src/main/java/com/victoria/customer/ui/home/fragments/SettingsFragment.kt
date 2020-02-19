package com.victoria.customer.ui.home.fragments

import android.os.Bundle
import android.view.View
import com.google.android.gms.common.internal.service.Common
import com.victoria.customer.R
import com.victoria.customer.core.Common.screenText
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.settings.fragments.ChangePasswordFragment
import com.victoria.customer.ui.home.settings.fragments.ContactUsFragment
import com.victoria.customer.ui.home.settings.fragments.PaymentDetailsFragment
import com.victoria.customer.ui.home.settings.fragments.WebViewFragment
import kotlinx.android.synthetic.main.fragment_settings_layout.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*


class SettingsFragment : BaseFragment() {

    override fun createLayout(): Int {
        return R.layout.fragment_settings_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        toolBarText.text = getString(R.string.toolbar_title_settings)

        imageViewMenu.setImageResource(R.drawable.arrow_back)

        imageViewMenu.setOnClickListener(this::onViewClick)
        textViewChangePassword.setOnClickListener(this::onViewClick)
        textViewPaymentDetails.setOnClickListener(this::onViewClick)
        textViewAboutUs.setOnClickListener(this::onViewClick)
        textViewContactUs.setOnClickListener(this::onViewClick)
        textViewTermsConditions.setOnClickListener(this::onViewClick)
        textViewFaqs.setOnClickListener(this::onViewClick)
        textViewChangeLanguage.setOnClickListener(this::onViewClick)
        textViewPrivacyPolicy.setOnClickListener(this::onViewClick)
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewMenu -> {
                navigator.load(HomeStartFragment::class.java).replace(false)

                //  navigator.goBack()
                //openDrawer()

            }

            R.id.textViewChangePassword -> {
                navigator.load(ChangePasswordFragment::class.java).replace(true)

            }

            R.id.textViewPaymentDetails -> {
                navigator.load(PaymentDetailsFragment::class.java).setBundle(Bundle().apply {
                    putBoolean(com.victoria.customer.core.Common.IS_FROM_SETTING, true)
                }).replace(true)

            }

            R.id.textViewAboutUs -> {
                var bundle: Bundle = Bundle()
                bundle.putString(screenText, getString(R.string.toolbar_title_about_us))
                navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
            }

            R.id.textViewContactUs -> {
                navigator.load(ContactUsFragment::class.java).replace(true)

            }

            R.id.textViewTermsConditions -> {
                var bundle: Bundle = Bundle()
                bundle.putString(screenText, getString(R.string.toolbar_title_terms_condition))
                navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
            }

            R.id.textViewChangeLanguage -> {
                navigator.load(LanguageChangeFragment::class.java).replace(true)
            }

            R.id.textViewFaqs -> {
                var bundle: Bundle = Bundle()
                bundle.putString(screenText, getString(R.string.toolbar_title_faq))
                navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)

            }

            R.id.textViewPrivacyPolicy -> {
                var bundle: Bundle = Bundle()
                bundle.putString(screenText, getString(R.string.toolbar_title_privacy_policy))
                navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
            }
        }
    }
}
