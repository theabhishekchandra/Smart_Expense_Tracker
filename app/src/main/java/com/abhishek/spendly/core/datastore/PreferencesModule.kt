package com.abhishek.spendly.core.datastore

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataSource(
        @ApplicationContext context: Context
    ): AppPreferencesDataSource = AppPreferencesDataSource(context)

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        dataSource: AppPreferencesDataSource
    ): AppPreferencesRepository = AppPreferencesRepository(dataSource)
}
