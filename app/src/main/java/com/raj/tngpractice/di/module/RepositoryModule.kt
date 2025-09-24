package com.raj.tngpractice.di.module

import android.content.Context
import com.raj.tngpractice.di.qualifiers.IODispatcher
import com.raj.tngpractice.feature.user.data.remote.UserAPI
import com.raj.tngpractice.feature.user.data.repository.UserRepository
import com.raj.tngpractice.feature.user.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(userAPI: UserAPI, @IODispatcher ioDispatcher: CoroutineDispatcher, @ApplicationContext context: Context) : UserRepository {
        return UserRepositoryImpl(userAPI, ioDispatcher, context)
    }

}