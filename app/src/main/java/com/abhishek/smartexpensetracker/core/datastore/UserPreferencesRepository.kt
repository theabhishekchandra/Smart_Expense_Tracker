package com.abhishek.smartexpensetracker.core.datastore

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataSource: UserPreferencesDataSource
) : IPreferencesDataSource {

    val themeMode: Flow<ThemeType> = dataSource.themeMode
    val businessMode: Flow<BusinessMode> = dataSource.businessMode

    override suspend fun setThemeMode(mode: ThemeType) = dataSource.setThemeMode(mode)
    override suspend fun setBusinessMode(enabled: Boolean) = dataSource.setBusinessMode(enabled)
}
