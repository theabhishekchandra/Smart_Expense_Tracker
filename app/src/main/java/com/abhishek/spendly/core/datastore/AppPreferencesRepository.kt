package com.abhishek.spendly.core.datastore

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

    val userNameFlow: Flow<String> = dataSource.userNameFlow
    val userEmailFlow: Flow<String> = dataSource.userEmailFlow
    val userPhoneFlow: Flow<String> = dataSource.userPhoneFlow
    val userProfileImageFlow: Flow<String?> = dataSource.userProfileImageFlow
    val userDobFlow: Flow<String> = dataSource.userDobFlow
    val userGenderFlow: Flow<String> = dataSource.userGenderFlow

    val businessNameFlow: Flow<String> = dataSource.businessNameFlow
    val businessOwnerNameFlow: Flow<String> = dataSource.businessOwnerNameFlow
    val businessLogoFlow: Flow<String?> = dataSource.businessLogoFlow
    val businessEmailFlow: Flow<String> = dataSource.businessEmailFlow
    val businessPhoneFlow: Flow<String> = dataSource.businessPhoneFlow
    val businessTypeFlow: Flow<String> = dataSource.businessTypeFlow


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
    override suspend fun setUserEmail(email: String) = dataSource.setUserEmail(email)
    override suspend fun setUserPhone(phone: String) = dataSource.setUserPhone(phone)
    override suspend fun setUserProfileImage(uri: String?) = dataSource.setUserProfileImage(uri)
    override suspend fun setUserDob(dob: String) = dataSource.setUserDob(dob)
    override suspend fun setUserGender(gender: String) = dataSource.setUserGender(gender)

    override suspend fun setBusinessName(name: String) = dataSource.setBusinessName(name)
    override suspend fun setBusinessOwnerName(name: String) = dataSource.setBusinessOwnerName(name)
    override suspend fun setBusinessLogo(uri: String?) = dataSource.setBusinessLogo(uri)
    override suspend fun setBusinessEmail(email: String) = dataSource.setBusinessEmail(email)
    override suspend fun setBusinessPhone(phone: String) = dataSource.setBusinessPhone(phone)
    override suspend fun setBusinessType(type: String) = dataSource.setBusinessType(type)

    override suspend fun getLanguageOnce(): Language = dataSource.getLanguageOnce()
    override suspend fun getCurrencyOnce(): Currency = dataSource.getCurrencyOnce()
    override suspend fun getExportFormatOnce(): ExportFormat = dataSource.getExportFormatOnce()
    override suspend fun getSyncWithOnce(): SyncWith = dataSource.getSyncWithOnce()
    override suspend fun getSyncFrequencyOnce(): SyncFrequency = dataSource.getSyncFrequencyOnce()
    override suspend fun getPushNotificationsOnce(): Boolean = dataSource.getPushNotificationsOnce()
    override suspend fun getEmailAlertsOnce(): Boolean = dataSource.getEmailAlertsOnce()
    override suspend fun getUserNameOnce(): String = dataSource.getUserNameOnce()

}
