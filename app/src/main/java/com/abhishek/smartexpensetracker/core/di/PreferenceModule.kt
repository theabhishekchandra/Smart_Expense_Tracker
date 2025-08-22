package com.abhishek.smartexpensetracker.core.di

import android.content.Context
import android.content.SharedPreferences
import com.abhishek.smartexpensetracker.core.sharepref.IPreferenceStorage
import com.abhishek.smartexpensetracker.core.sharepref.SharedIPreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("kagaz_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferenceStorage(
        sharedPreferences: SharedPreferences
    ): IPreferenceStorage {
        return SharedIPreferenceStorage(sharedPreferences)
    }
}