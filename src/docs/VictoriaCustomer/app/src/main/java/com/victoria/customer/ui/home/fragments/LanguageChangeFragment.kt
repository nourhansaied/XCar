package com.victoria.customer.ui.home.fragments

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.model.Parameter
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.activity.HomeActivity
import com.victoria.customer.ui.home.settings.viewmodel.ChangePasswordViewModel
import kotlinx.android.synthetic.main.fragment_select_language.*
import kotlinx.android.synthetic.main.toolbar_with_menu.*
import java.util.*

class LanguageChangeFragment : BaseFragment() {

    internal lateinit var sharedPreferences: SharedPreferences

    override fun createLayout(): Int {
        return R.layout.fragment_select_language
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    private val changePasswordViewModel: ChangePasswordViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory)[ChangePasswordViewModel::class.java]
    }

    override fun bindData() {
        toolBarText.text = getString(R.string.label_change_language)

        imageViewMenu.setImageResource(R.drawable.arrow_back)

        imageViewMenu.setOnClickListener(this::onViewClick)
        linearLayoutEnglish.setOnClickListener(this::onViewClick)
        linearLayoutArabic.setOnClickListener(this::onViewClick)
        sharedPreferences = Objects.requireNonNull(activity)?.getSharedPreferences(Common.LANGUAGE_SELECTION, Context.MODE_PRIVATE)!!

        if (sharedPreferences.getBoolean(Common.WHICH_LANGUAGE, false)) {
            changeUi(true)
        } else {
            changeUi(false)
        }
    }

    fun changeUi(isArabic: Boolean) {
        if (isArabic) {
            imageViewArabic.visibility = View.VISIBLE
            imageViewEnglish.visibility = View.GONE
        } else {
            imageViewArabic.visibility = View.GONE
            imageViewEnglish.visibility = View.VISIBLE
        }
    }

    fun changeLang(language: String, context: Context) {

        var lang = Common.Language.ENGLISH

        if (!language.equals("", ignoreCase = true) && language.equals(Common.Language.ARABIC, ignoreCase = true)) {
            lang = Common.Language.ARABIC
        }

        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        if (!language.isEmpty()) {
            Common.WHICH_LANGUAGE_SELECTED = Common.ARABIC_LANGUAGE
            sharedPreferences.edit().putBoolean(Common.WHICH_LANGUAGE, true).apply()
            session.setLanguage(Common.ARABIC_LANGUAGE)
        } else {
            Common.WHICH_LANGUAGE_SELECTED = ""
            sharedPreferences.edit().putBoolean(Common.WHICH_LANGUAGE, false).apply()
            session.setLanguage("")
        }

        observeResponse()
        navigator.toggleLoader(true)
        changePasswordViewModel.changelanguage(getData())
    }

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.imageViewMenu -> {
                navigator.goBack()
                //navigator.load(HomeStartFragment::class.java).replace(false)
            }


            R.id.linearLayoutEnglish -> {
                context?.let { changeLang("", it) }
            }

            R.id.linearLayoutArabic -> {
                context?.let { changeLang(Common.Language.ARABIC, it) }
            }
        }
    }

    private fun observeResponse() {
        changePasswordViewModel.changelanguage.observe(this, { responseBody ->
            handleChangePasswordResponse(responseBody)
        }, {
            navigator.toggleLoader(false);true
        })
    }

    private fun getData(): Parameter {
        val parameter = Parameter()
        parameter.user_type = "Customer"
        if (Common.WHICH_LANGUAGE_SELECTED.isNotEmpty())
            parameter.appLanguage = Common.WHICH_LANGUAGE_SELECTED
        else
            parameter.appLanguage = "en"
        return parameter
    }

    private fun handleChangePasswordResponse(responseBody: com.victoria.customer.data.pojo.ResponseBody<Unit>) {
        navigator.toggleLoader(false)
        showMessage(responseBody.message)
        if (responseBody.responseCode == 1) {
            navigator.loadActivity(HomeActivity::class.java).byFinishingAll().start()
        }
    }
}
