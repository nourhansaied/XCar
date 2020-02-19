package com.victoria.customer.di.module;

import com.victoria.customer.di.PerFragment
import com.victoria.customer.ui.base.BaseFragment
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
