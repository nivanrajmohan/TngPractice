package com.raj.tngpractice.di.module

import com.raj.tngpractice.di.qualifiers.DefaultDispatcher
import com.raj.tngpractice.di.qualifiers.IODispatcher
import com.raj.tngpractice.di.qualifiers.MainDispatcher
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object AppModule {

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @IODispatcher
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

}