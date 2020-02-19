package com.victoria.driver.ui.home.activity

import android.os.Bundle

import com.victoria.driver.R
import com.victoria.driver.core.AppPreferences
import com.victoria.driver.di.component.ActivityComponent
import com.victoria.driver.ui.base.BaseActivity
import com.victoria.driver.ui.base.BaseFragment
import com.victoria.driver.ui.manager.ActivityStarter
import javax.inject.Inject


/**
 * Created by hlink21 on 3/10/17.
 */

class IsolatedActivity : BaseActivity() {
    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportActionBar != null)
            supportActionBar!!.setDisplayShowTitleEnabled(false)

        if (savedInstanceState == null) {
            showBackButton(true)
            val page = intent.getSerializableExtra(ActivityStarter.ACTIVITY_FIRST_PAGE) as Class<BaseFragment>
            if (page != null) {
                load(page)
                        .setBundle(intent.extras!!)
                        .add(false)
            }
        }
    }

    override fun findFragmentPlaceHolder(): Int {
        return R.id.placeHolder
    }

    override fun findContentView(): Int {
        return R.layout.activity_isolated
    }

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)

    }

    override fun closeDrawer() {
    }

    override fun openDrawer() {
    }
}
