package com.victoria.customer.di.component;
import com.victoria.customer.di.PerActivity
import com.victoria.customer.di.module.ActivityModule
import com.victoria.customer.di.module.FragmentModule
import com.victoria.customer.ui.authentication.activities.AuthenticationActivity
import com.victoria.customer.ui.authentication.activities.SplashActivity
import com.victoria.customer.ui.base.BaseActivity
import com.victoria.customer.ui.home.activity.HomeActivity
import com.victoria.customer.ui.home.activity.IsolatedActivity
import com.victoria.customer.ui.manager.Navigator
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

    fun inject(authenticationActivity: AuthenticationActivity)
    fun inject(authenticationActivity: SplashActivity)
    fun inject(homeActivity: HomeActivity) {

    }

    fun inject(isolatedActivity: IsolatedActivity){}


    @Component.Builder
    interface Builder {

        fun bindApplicationComponent(applicationComponent: ApplicationComponent): Builder

        @BindsInstance
        fun bindActivity(baseActivity: BaseActivity): Builder

        fun build(): ActivityComponent
    }
}
