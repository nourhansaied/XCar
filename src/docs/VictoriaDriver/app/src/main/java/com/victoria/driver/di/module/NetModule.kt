package com.victoria.driver.di.module


import com.victoria.customer.data.URLFactory
import com.victoria.driver.core.AESCryptoInterceptor
import com.victoria.driver.core.Common
import com.victoria.driver.core.Session

import com.victoria.driver.exception.AuthenticationException
import com.victoria.driver.exception.ServerError
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by hlink21 on 11/5/17.
 */


@Module(includes = [(ApplicationModule::class)])
class NetModule {

    @Singleton
    @Provides
    internal fun provideClient(@Named("header") headerInterceptor: Interceptor,
                               @Named("pre_validation") networkInterceptor: Interceptor,
                               @Named("aes") aesInterceptor: Interceptor): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(aesInterceptor)
                .addNetworkInterceptor(networkInterceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
                .baseUrl(URLFactory.provideHttpUrl())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    @Provides
    @Singleton
    @Named("header")
    internal fun provideHeaderInterceptor(session: Session): Interceptor {

        return Interceptor { chain ->
            val build = chain.request().newBuilder()
                    .addHeader(Session.API_KEY, session.apiKey)
                    .addHeader(Session.USER_SESSION, session.userSession)
                    .addHeader(Session.CONTENT_LANGUAGE, Common.WHICH_LANGUAGE_SELECTED)
                    .build()
            chain.proceed(build)
        }
    }

    @Provides
    @Singleton
    @Named("pre_validation")
    internal fun provideNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val code = response.code()
            if (code >= 500)
                throw ServerError("Unknown server error", response.body()!!.string())
            else if (code == 401 || code == 403)
                throw AuthenticationException()
            response
        }
    }

    @Provides
    @Singleton
    @Named("aes")
    internal fun provideAesCryptoInterceptor(aesCryptoInterceptor: AESCryptoInterceptor): Interceptor {
        return aesCryptoInterceptor
    }


}
