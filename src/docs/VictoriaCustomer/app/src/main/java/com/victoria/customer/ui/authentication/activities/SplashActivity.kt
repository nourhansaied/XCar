package com.victoria.customer.ui.authentication.activities

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import com.victoria.customer.R
import com.victoria.customer.core.AppPreferences
import com.victoria.customer.core.Common
import com.victoria.customer.core.Session
import com.victoria.customer.di.component.ActivityComponent
import com.victoria.customer.ui.base.BaseActivity
import com.victoria.customer.ui.home.activity.HomeActivity
import com.victoria.customer.util.PARAMETERS
import java.util.*
import javax.inject.Inject

class SplashActivity : BaseActivity() {


    @Inject
    lateinit var appPreferences: AppPreferences
    internal lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var session: Session


    override fun findFragmentPlaceHolder(): Int {
        return 0

    }

    override fun findContentView(): Int {
        return R.layout.activity_splash_layout
    }

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = this.getSharedPreferences(Common.LANGUAGE_SELECTION, Context.MODE_PRIVATE)

        Handler().postDelayed({

            if (appPreferences.getBoolean(PARAMETERS.KEY_IS_LOGIN)) {

                if (sharedPreferences.getBoolean(Common.WHICH_LANGUAGE, false)) {
                    changeLang(Common.Language.ARABIC, this)
                    Common.WHICH_LANGUAGE_SELECTED = Common.ARABIC_LANGUAGE
                } else {
                    changeLang("", this)
                    Common.WHICH_LANGUAGE_SELECTED = ""
                }
                loadActivity(HomeActivity::class.java).byFinishingCurrent().start()

            } else {
                loadActivity(AuthenticationActivity::class.java).byFinishingCurrent().start()

            }

        }, 3000)
        setContentView(R.layout.activity_splash_layout)


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
