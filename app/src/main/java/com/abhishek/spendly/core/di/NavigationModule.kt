package com.abhishek.spendly.core.di

import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.core.navigation.DefaultNavManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavManager(
        impl: DefaultNavManager
    ): NavManager
}