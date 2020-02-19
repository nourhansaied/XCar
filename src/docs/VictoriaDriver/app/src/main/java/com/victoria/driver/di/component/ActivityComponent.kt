package com.victoria.driver.di.component


import com.victoria.driver.di.PerActivity
import com.victoria.driver.di.module.ActivityModule
import com.victoria.driver.di.module.FragmentModule
import com.victoria.driver.ui.authentication.activities.AuthenticationActivity
import com.victoria.driver.ui.authentication.activities.SplashActivity
import com.victoria.driver.ui.base.BaseActivity
import com.victoria.driver.ui.home.activity.HomeActivity
import com.victoria.driver.ui.home.activity.IsolatedActivity
import com.victoria.driver.ui.manager.Navigator
import dagger.BindsInstance
import dagger.Component

/**
 * Created by hlink21 on 9/5/16.
 */
@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun activity(): BaseActivity

    fun navigator(): Navigator


    operator fun plus(fragmentModule: FragmentModule): FragmentComponent

    fun inject(splashActivity: SplashActivity)
    fun inject(homeActivity: HomeActivity)
    fun inject(authenticationActivity: AuthenticationActivity)
    fun inject(isolatedActivity: IsolatedActivity) {}

    @Component.Builder
    interface Builder {

        fun bindApplicationComponent(applicationComponent: ApplicationComponent): Builder

        @BindsInstance
        fun bindActivity(baseActivity: BaseActivity): Builder

        fun build(): ActivityComponent
    }
}
