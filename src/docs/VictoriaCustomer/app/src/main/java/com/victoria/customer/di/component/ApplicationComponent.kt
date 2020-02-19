package com.victoria.customer.di.component;
import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.res.Resources
import com.victoria.customer.core.AppPreferences
import com.victoria.customer.core.Session
import com.victoria.customer.core.Validator
import com.victoria.customer.core.map_route.MyGeocoder
import com.victoria.customer.data.repository.TripRepository
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.di.ServiceModule
import com.victoria.customer.di.VictoriaCustomer
import com.victoria.customer.di.module.ApplicationModule
import com.victoria.customer.di.module.NetModule
import com.victoria.customer.di.module.ViewModelModule
import com.victoria.customer.util.LocationManager
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

    fun inject(victoriaCustomerShell: VictoriaCustomer)

    fun provideUserRepository(): UserRepository

    fun provideTripRepository(): TripRepository

    fun appPreferences(): AppPreferences

    fun provideSession(): Session

    fun validator(): Validator

    fun locationManager(): LocationManager
    fun myGeocoder(): MyGeocoder

    @Component.Builder
    interface ApplicationComponentBuilder {

        @BindsInstance
        fun apiKey(@Named("api-key") apiKey: String): ApplicationComponentBuilder

        @BindsInstance
        fun application(application: Application): ApplicationComponentBuilder

        fun build(): ApplicationComponent
    }

}
