package com.victoria.driver.di.module

import android.support.v4.app.FragmentManager
import com.victoria.driver.di.PerActivity
import com.victoria.driver.ui.base.BaseActivity
import com.victoria.driver.ui.manager.FragmentHandler
import com.victoria.driver.ui.manager.Navigator
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by hlink21 on 9/5/16.
 */
@Module
class ActivityModule {

    @Provides
    @PerActivity
    internal fun navigator(activity: BaseActivity): Navigator {
        return activity
    }

    @Provides
    @PerActivity
    internal fun fragmentManager(baseActivity: BaseActivity): FragmentManager {
        return baseActivity.supportFragmentManager
    }

    @Provides
    @PerActivity
    @Named("placeholder")
    internal fun placeHolder(baseActivity: BaseActivity): Int {
        return baseActivity.findFragmentPlaceHolder()
    }

    @Provides
    @PerActivity
    internal fun fragmentHandler(fragmentManager: com.victoria.driver.ui.manager.FragmentManager): FragmentHandler {
        return fragmentManager
    }


}
