package com.victoria.customer.di;
import com.victoria.customer.data.datasource.TripLiveDataSource
import com.victoria.customer.data.datasource.UserLiveDataSource
import com.victoria.customer.data.repository.TripRepository
import com.victoria.customer.data.repository.UserRepository
import com.victoria.customer.data.service.AuthenticationService
import com.victoria.customer.data.service.TripService
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
    fun provideTripRepository(tripLiveDataSource: TripLiveDataSource): TripRepository {
        return tripLiveDataSource
    }

    @Provides
    @Singleton
    fun provideAuthenticationService(retrofit: Retrofit): AuthenticationService {
        return retrofit.create(AuthenticationService::class.java)
    }

    @Provides
    @Singleton
    fun providTripService(retrofit: Retrofit): TripService {
        return retrofit.create(TripService::class.java)
    }


}