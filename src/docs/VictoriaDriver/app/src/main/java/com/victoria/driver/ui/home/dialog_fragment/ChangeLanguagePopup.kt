package com.victoria.driver.ui.home.dialog_fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.victoria.driver.R
import com.victoria.driver.core.AppPreferences
import com.victoria.driver.core.Common
import com.victoria.driver.core.Session
import com.victoria.driver.di.App
import com.victoria.driver.ui.authentication.activities.AuthenticationActivity
import com.victoria.driver.ui.home.activity.HomeActivity
import com.victoria.driver.ui.manager.Navigator
import kotlinx.android.synthetic.main.dialog_ride_request.*
import kotlinx.android.synthetic.main.fragment_select_language.*
import java.util.*


@SuppressLint("ValidFragment")
/**
 * Created by hlink53 on 19/5/16.
 */
class ChangeLanguagePopup @SuppressLint("ValidFragment") constructor
(var callBackForLanguageSelect: CallBackForLanguageSelect,
 var appPreferences: AppPreferences, var session: Session,
 var navigator: Navigator) : DialogFragment() {
    internal lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_select_language, container, false)
        ButterKnife.bind(this, view)
        val wmlp = dialog.window!!.attributes
        dialog.window!!.requestFeature(STYLE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }

    override fun onStart() {
        super.onStart()
        /*val params = dialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        linearLayoutEnglish.setOnClickListener {
            context?.let { changeLang("", it) }

        }

        linearLayoutArabic.setOnClickListener {
            context?.let { changeLang(Common.Language.ARABIC, it) }

        }
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

        navigator.loadActivity(AuthenticationActivity::class.java).byFinishingAll().start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    interface CallBackForLanguageSelect {
        fun dissmissDialog()
    }
}
