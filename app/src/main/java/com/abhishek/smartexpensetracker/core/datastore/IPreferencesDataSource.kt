package com.abhishek.smartexpensetracker.core.datastore

import androidx.datastore.preferences.core.edit

interface IPreferencesDataSource {
    suspend fun setThemeMode(mode: ThemeType)
    suspend fun setBusinessMode(enabled: Boolean)

    suspend fun getIsPremiumOnce(): Boolean

    suspend fun getPremiumTypeOnce(): PremiumType
    suspend fun setPremiumType(type: PremiumType)

    suspend fun setPremium(enabled: Boolean, type: PremiumType)
}