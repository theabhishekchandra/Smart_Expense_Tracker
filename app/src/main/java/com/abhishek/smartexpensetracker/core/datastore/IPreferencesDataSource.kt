package com.abhishek.smartexpensetracker.core.datastore

interface IPreferencesDataSource {
    suspend fun setThemeMode(mode: ThemeType)
    suspend fun setBusinessMode(enabled: Boolean)

}