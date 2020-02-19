package com.victoria.customer.di.module;
import com.victoria.customer.di.PerActivity
import com.victoria.customer.ui.base.BaseActivity
import com.victoria.customer.ui.manager.FragmentHandler
import com.victoria.customer.ui.manager.Navigator
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
    internal fun fragmentManager(baseActivity: BaseActivity): android.support.v4.app.FragmentManager {
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
    internal fun fragmentHandler(fragmentManager: com.victoria.customer.ui.manager.FragmentManager): FragmentHandler {
        return fragmentManager
    }


}
