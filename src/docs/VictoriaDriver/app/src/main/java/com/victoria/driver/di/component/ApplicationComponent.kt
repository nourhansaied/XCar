package com.victoria.driver.di.component

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.res.Resources
import com.victoria.driver.core.AppPreferences
import com.victoria.driver.core.Session
import com.victoria.driver.core.Validator
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.di.App
import com.victoria.driver.di.module.ApplicationModule
import com.victoria.driver.di.module.NetModule
import com.victoria.driver.di.module.ServiceModule
import com.victoria.driver.di.module.ViewModelModule
import com.victoria.driver.util.LocationManager
import dagger.BindsInstance
import dagger.Component
import java.io.File
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by hlink21 on 9/5/16.
 */
@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class, NetModule::class, ServiceModule::class])
interface ApplicationComponent {

    fun context(): Context

    @Named("cache")
    fun provideCacheDir(): File

    fun provideResources(): Resources

    fun provideCurrentLocale(): Locale

    fun provideViewModelFactory(): ViewModelProvider.Factory

    fun inject(appShell: App)

    fun provideUserRepository(): UserRepository

    fun appPreferences(): AppPreferences

    fun provideSession(): Session

    fun validator(): Validator

    fun locationManager(): LocationManager

    @Component.Builder
    interface ApplicationComponentBuilder {

        @BindsInstance
        fun apiKey(@Named("api-key") apiKey: String): ApplicationComponentBuilder

        @BindsInstance
        fun application(application: Application): ApplicationComponentBuilder

        fun build(): ApplicationComponent
    }

}
