package com.victoria.driver.ui.authentication.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import com.victoria.driver.R
import com.victoria.driver.core.Common
import com.victoria.driver.core.Session
import com.victoria.driver.di.component.ActivityComponent
import com.victoria.driver.ui.authentication.fragment.StartFragment
import com.victoria.driver.ui.base.BaseActivity
import java.util.*
import javax.inject.Inject

class AuthenticationActivity : BaseActivity() {

    internal lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var session: Session

    override fun findFragmentPlaceHolder(): Int {

        return R.id.placeHolder

    }

    override fun findContentView(): Int {
        return R.layout.activity_authentication_layout
    }

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = Objects.requireNonNull(this)?.getSharedPreferences(Common.LANGUAGE_SELECTION
                , Context.MODE_PRIVATE)!!
        if (sharedPreferences.getBoolean(Common.WHICH_LANGUAGE, false)) {
            changeLang(Common.Language.ARABIC, this)
        }
        setContentView(R.layout.activity_authentication_layout)

        load(StartFragment::class.java).replace(false)

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
}
