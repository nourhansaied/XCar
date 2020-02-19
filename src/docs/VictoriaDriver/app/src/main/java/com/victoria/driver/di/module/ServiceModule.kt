package com.victoria.driver.di.module

import com.victoria.driver.data.datasource.UserLiveDataSource
import com.victoria.driver.data.repository.UserRepository
import com.victoria.driver.data.service.AuthenticationService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
class ServiceModule {

    @Provides
    @Singleton
    fun provideUserRepository(userLiveDataSource: UserLiveDataSource): UserRepository {
        return userLiveDataSource
    }

    @Provides
    @Singleton
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

}