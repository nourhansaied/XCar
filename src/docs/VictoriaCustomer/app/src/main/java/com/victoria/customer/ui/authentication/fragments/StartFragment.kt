package com.victoria.customer.ui.authentication.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.view.View
import com.victoria.customer.R
import com.victoria.customer.core.Common
import com.victoria.customer.di.component.FragmentComponent
import com.victoria.customer.ui.authentication.activities.AuthenticationActivity
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.home.dialog.ChangeLanguagePopup
import kotlinx.android.synthetic.main.fragment_start_layout.*
import java.util.*


class StartFragment : BaseFragment(), ChangeLanguagePopup.CallBackForLanguageSelect {

    internal lateinit var sharedPreferences: SharedPreferences


    override fun createLayout(): Int {
        return R.layout.fragment_start_layout
    }

    override fun inject(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun bindData() {
        sharedPreferences = Objects.requireNonNull(activity)?.getSharedPreferences(Common.LANGUAGE_SELECTION
                , Context.MODE_PRIVATE)!!

        textViewSignin.setOnClickListener(this::onViewClick)
        textViewSignUp.setOnClickListener(this::onViewClick)
        appCompatTextViewLanguage.setOnClickListener(this::onViewClick)

        if (sharedPreferences.getBoolean(Common.WHICH_LANGUAGE, false)) {
            appCompatTextViewLanguage.text = getString(R.string.label_arabic)
            changeLang(Common.Language.ARABIC, context!!)
            navigator.loadActivity(AuthenticationActivity::class.java).byFinishingAll()
        } else {
            appCompatTextViewLanguage.text = getString(R.string.label_english)
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
    }

    lateinit var changeLanguagePopup: ChangeLanguagePopup

    override fun onViewClick(view: View) {
        super.onViewClick(view)
        when (view.id) {

            R.id.textViewSignin -> {
                navigator.load(SignInFragment::class.java).replace(true)
            }
            R.id.textViewSignUp -> {

                navigator.load(SignupFragment::class.java).replace(true)

            }

            R.id.appCompatTextViewLanguage -> {
                changeLanguagePopup = ChangeLanguagePopup(this, appPreferences, session, navigator)
                changeLanguagePopup.show(fragmentManager, "")
            }
        }
    }

    override fun dissmissDialog() {

    }

}
