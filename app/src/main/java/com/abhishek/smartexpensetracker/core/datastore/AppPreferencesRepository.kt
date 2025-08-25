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
    val premiumTypeFlow : Flow<PremiumType> = dataSource.premiumType

    val languageFlow: Flow<Language> = dataSource.languageFlow
    val currencyFlow: Flow<Currency> = dataSource.currencyFlow
    val exportFormatFlow: Flow<ExportFormat> = dataSource.exportFormatFlow
    val syncWithFlow: Flow<SyncWith> = dataSource.syncWithFlow
    val syncFrequencyFlow: Flow<SyncFrequency> = dataSource.syncFrequencyFlow
    val pushNotificationsFlow: Flow<Boolean> = dataSource.pushNotificationsFlow
    val emailAlertsFlow: Flow<Boolean> = dataSource.emailAlertsFlow


    override suspend fun setThemeMode(mode: ThemeType) = dataSource.setThemeMode(mode)
    override suspend fun setBusinessMode(enabled: Boolean) = dataSource.setBusinessMode(enabled)

    override suspend fun getIsPremiumOnce(): Boolean = dataSource.getIsPremiumOnce()
    override suspend fun getPremiumTypeOnce(): PremiumType = dataSource.getPremiumTypeOnce()
    override suspend fun setPremiumType(type: PremiumType) = dataSource.setPremiumType(type)

    override suspend fun setPremium(enabled: Boolean, type: PremiumType) {
        type.let { dataSource.setPremium(enabled,type) }
    }
    override suspend fun setLanguage(language: Language) = dataSource.setLanguage(language)
    override suspend fun setCurrency(currency: Currency) = dataSource.setCurrency(currency)
    override suspend fun setExportFormat(format: ExportFormat) = dataSource.setExportFormat(format)
    override suspend fun setSyncWith(syncWith: SyncWith) = dataSource.setSyncWith(syncWith)
    override suspend fun setSyncFrequency(frequency: SyncFrequency) = dataSource.setSyncFrequency(frequency)
    override suspend fun setPushNotifications(enabled: Boolean) = dataSource.setPushNotifications(enabled)
    override suspend fun setEmailAlerts(enabled: Boolean) = dataSource.setEmailAlerts(enabled)
    override suspend fun setUserName(name: String) = dataSource.setUserName(name)

    override suspend fun getLanguageOnce(): Language = dataSource.getLanguageOnce()
    override suspend fun getCurrencyOnce(): Currency = dataSource.getCurrencyOnce()
    override suspend fun getExportFormatOnce(): ExportFormat = dataSource.getExportFormatOnce()
    override suspend fun getSyncWithOnce(): SyncWith = dataSource.getSyncWithOnce()
    override suspend fun getSyncFrequencyOnce(): SyncFrequency = dataSource.getSyncFrequencyOnce()
    override suspend fun getPushNotificationsOnce(): Boolean = dataSource.getPushNotificationsOnce()
    override suspend fun getEmailAlertsOnce(): Boolean = dataSource.getEmailAlertsOnce()
    override suspend fun getUserNameOnce(): String = dataSource.getUserNameOnce()

}
