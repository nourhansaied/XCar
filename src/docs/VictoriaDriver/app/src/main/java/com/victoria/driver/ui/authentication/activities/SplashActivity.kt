package com.victoria.driver.ui.authentication.activities

import android.os.Bundle
import android.os.Handler
import com.victoria.driver.R
import com.victoria.driver.core.AppPreferences
import com.victoria.driver.di.component.ActivityComponent
import com.victoria.driver.ui.base.BaseActivity
import com.victoria.driver.ui.home.activity.HomeActivity
import com.victoria.driver.util.PARAMETERS
import javax.inject.Inject

class SplashActivity : BaseActivity() {


    @Inject
    lateinit var appPreferences: AppPreferences


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
        setContentView(R.layout.activity_splash_layout)

        //ExceptionHandler.register(this, Common.ERRORLINK)

        Handler().postDelayed({

            if (appPreferences.getBoolean(PARAMETERS.KEY_IS_LOGIN)) {
                loadActivity(HomeActivity::class.java).byFinishingCurrent().start()
            } else {
                loadActivity(AuthenticationActivity::class.java).byFinishingCurrent().start()

            }

        }, 3000)

    }


}
