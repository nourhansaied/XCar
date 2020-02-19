package com.victoria.driver.ui.home.fragment

import android.view.View
import com.victoria.customer.data.URLFactory
import com.victoria.driver.R
import com.victoria.driver.di.component.FragmentComponent
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.util.PARAMETERS
import kotlinx.android.synthetic.main.fragment_web_view.*
import kotlinx.android.synthetic.main.toolbar_with_close.*


class WebViewFragment : BaseFragment() {

    lateinit var title: String


    override fun createLayout(): Int {
        return R.layout.fragment_web_view
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        webview.settings.javaScriptEnabled = true
        webview.settings.javaScriptCanOpenWindowsAutomatically = true

        var bundle = arguments
        if (bundle != null) {
            if (bundle.getString(PARAMETERS.PARAM_SCREEN) == getString(R.string.toolbar_title_about_us)) {
                webview.loadUrl(URLFactory.ABOUT_US)
            } else if (bundle.getString(PARAMETERS.PARAM_SCREEN) == getString(R.string.toolbar_title_terms_condition)) {
                webview.loadUrl(URLFactory.TANDC)
            } else if (bundle.getString(PARAMETERS.PARAM_SCREEN) == getString(R.string.toolbar_title_faq)) {
                webview.loadUrl(URLFactory.FAQ)
            } else if (bundle.getString(PARAMETERS.PARAM_SCREEN) == getString(R.string.toolbar_title_privacy_policy)) {
                webview.loadUrl(URLFactory.PRIVACY_POLICY)
            }
            title = bundle.getString(PARAMETERS.PARAM_SCREEN)
        }

        toolBarText.text = title

        imageViewBack.setOnClickListener(this::onViewClick)
    }


    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewBack -> {

                navigator.goBack()
            }

        }
    }


}
