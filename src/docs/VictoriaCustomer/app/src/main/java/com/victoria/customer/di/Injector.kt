package com.victoria.customer.di;
import android.app.Application
import com.victoria.customer.di.component.ApplicationComponent
import com.victoria.customer.di.component.DaggerApplicationComponent

/**
 * Created by hlink21 on 9/5/16.
 */
enum class Injector private constructor() {
    INSTANCE;

    lateinit var applicationComponent: ApplicationComponent
        internal set

    fun initAppComponent(application: Application, apiKey: String) {
        applicationComponent = DaggerApplicationComponent.builder()
                .application(application)
                .apiKey(apiKey)
                .build()
    }
}
