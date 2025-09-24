package com.raj.tngpractice.di.module

import com.raj.tngpractice.feature.user.adapter.UserAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object AdapterModule {

    @Provides
    fun provideUserAdapter() : UserAdapter {
        return UserAdapter()
    }

}