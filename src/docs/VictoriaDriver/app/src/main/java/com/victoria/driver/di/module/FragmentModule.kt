package com.victoria.driver.di.module

import com.victoria.driver.di.PerFragment
import com.victoria.driver.ui.base.BaseFragment
import dagger.Module
import dagger.Provides

/**
 * Created by hlink21 on 31/5/16.
 */
@Module
class FragmentModule(private val baseFragment: BaseFragment) {

    @Provides
    @PerFragment
    internal fun provideBaseFragment(): BaseFragment {
        return baseFragment
    }

}
