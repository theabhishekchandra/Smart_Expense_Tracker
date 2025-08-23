package com.abhishek.smartexpensetracker.core.datastore

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
    ): UserPreferencesDataSource = UserPreferencesDataSource(context)

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        dataSource: UserPreferencesDataSource
    ): UserPreferencesRepository = UserPreferencesRepository(dataSource)
}
