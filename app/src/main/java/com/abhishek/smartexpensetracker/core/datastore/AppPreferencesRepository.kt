package com.abhishek.smartexpensetracker.core.datastore

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesRepository @Inject constructor(
    private val dataSource: AppPreferencesDataSource
) : IPreferencesDataSource {

    val themeMode: Flow<ThemeType> = dataSource.themeMode
    val businessMode: Flow<BusinessMode> = dataSource.businessMode

    val isPremiumFlow : Flow<Boolean> = dataSource.isPremium
    val premiumTypeFlow : Flow<String?> = dataSource.premiumType
    override suspend fun setThemeMode(mode: ThemeType) = dataSource.setThemeMode(mode)
    override suspend fun setBusinessMode(enabled: Boolean) = dataSource.setBusinessMode(enabled)

    override suspend fun getIsPremiumOnce(): Boolean = dataSource.getIsPremiumOnce()
    override suspend fun getPremiumTypeOnce(): PremiumType = dataSource.getPremiumTypeOnce()
    override suspend fun setPremiumType(type: PremiumType) = dataSource.setPremiumType(type)

    override suspend fun setPremium(enabled: Boolean, type: PremiumType) {
        if (enabled) {
            type.let { dataSource.setPremiumType(it) }
        } else {
            dataSource.setPremiumType(type)
        }
    }
}
