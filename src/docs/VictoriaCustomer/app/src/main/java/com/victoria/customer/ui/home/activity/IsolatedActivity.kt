package com.victoria.customer.ui.home.activity

import android.os.Bundle

import com.victoria.customer.R
import com.victoria.customer.core.AppPreferences
import com.victoria.customer.di.component.ActivityComponent
import com.victoria.customer.ui.base.BaseActivity
import com.victoria.customer.ui.base.BaseFragment
import com.victoria.customer.ui.manager.ActivityStarter
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
