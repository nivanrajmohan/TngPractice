package com.raj.tngpractice.di.module

import android.content.Context
import com.raj.tngpractice.BuildConfig
import com.raj.tngpractice.feature.general.interceptor.NetworkCheckInterceptor
import com.raj.tngpractice.feature.user.data.remote.UserAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {

    @Provides
    fun provideHttpClient(@ApplicationContext appContext : Context) : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(NetworkCheckInterceptor(appContext))
            //.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    fun provideUserApi(retrofit: Retrofit) : UserAPI {
        return retrofit.create(UserAPI::class.java)
    }

}