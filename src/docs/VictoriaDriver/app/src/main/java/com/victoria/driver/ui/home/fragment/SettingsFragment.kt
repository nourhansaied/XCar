package com.victoria.driver.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.victoria.driver.R
import com.victoria.driver.core.Common.screenText
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.authentication.fragment.EditBankAccountInfoFragment
import com.victoria.driver.ui.authentication.fragment.EditVehicleDocumentFragment
import com.victoria.driver.ui.authentication.fragment.EditVehicleInfoFragment
import com.victoria.driver.ui.base.BaseFragment
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
        imageViewMenu.setOnClickListener(this::onViewClick)
        imageViewMenu.setImageResource(R.drawable.arrow_back)

        textViewChangePassword.setOnClickListener(this::onViewClick)
        textViewEditBankInfo.setOnClickListener(this::onViewClick)
        textViewEditVehicleInfo.setOnClickListener(this::onViewClick)
        textViewEditVehicleDocument.setOnClickListener(this::onViewClick)
        textViewAboutUs.setOnClickListener(this::onViewClick)
        textViewContactUs.setOnClickListener(this::onViewClick)
        textViewTermsConditions.setOnClickListener(this::onViewClick)
        textViewFaq.setOnClickListener(this::onViewClick)
        textViewPrivacyPolicy.setOnClickListener(this::onViewClick)
        textViewChangeLanguage.setOnClickListener(this::onViewClick)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_layout, container, false)


    }


    override fun onViewClick(view: View) {

        when (view.id) {

            R.id.imageViewMenu -> {
                // openDrawer()
                navigator.load(HomeFragment::class.java).replace(false)


            }

            R.id.textViewChangePassword -> {
                navigator.load(ChangePasswordFragment::class.java).replace(true)

            }
            R.id.textViewChangeLanguage -> {
                navigator.load(LanguageChangeFragment::class.java).replace(true)
            }
            R.id.textViewEditBankInfo -> {
                navigator.load(EditBankAccountInfoFragment::class.java).replace(true)

            }
            R.id.textViewEditVehicleInfo -> {
                navigator.load(EditVehicleInfoFragment::class.java).replace(true)
            }

            R.id.textViewEditVehicleDocument -> {
                navigator.load(EditVehicleDocumentFragment::class.java).replace(true)
            }

            R.id.textViewAboutUs -> {
                var bundle: Bundle = Bundle()
                bundle.putString(screenText, getString(R.string.toolbar_title_about_us))
                navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)
                //navigator.load(ContactUsFragment::class.java).replace(true)

            }
            R.id.textViewContactUs -> {
                navigator.load(ContactUsFragment::class.java).replace(true)


            }
            R.id.textViewTermsConditions -> {
                var bundle: Bundle = Bundle()
                bundle.putString(screenText, getString(R.string.toolbar_title_terms_condition))
                navigator.load(WebViewFragment::class.java).setBundle(bundle).replace(true)

            }

            R.id.textViewFaq -> {
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
