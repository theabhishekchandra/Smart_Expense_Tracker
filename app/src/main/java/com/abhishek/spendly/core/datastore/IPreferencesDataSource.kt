package com.abhishek.spendly.core.datastore

import androidx.datastore.preferences.core.edit

interface IPreferencesDataSource {
    suspend fun setThemeMode(mode: ThemeType)
    suspend fun setBusinessMode(enabled: Boolean)
    suspend fun getIsPremiumOnce(): Boolean
    suspend fun getPremiumTypeOnce(): PremiumType
    suspend fun setPremiumType(type: PremiumType)
    suspend fun setPremium(enabled: Boolean, type: PremiumType)

    suspend fun setLanguage(language: Language)
    suspend fun getLanguageOnce(): Language

    suspend fun setCurrency(currency: Currency)
    suspend fun getCurrencyOnce(): Currency

    suspend fun setExportFormat(format: ExportFormat)
    suspend fun getExportFormatOnce(): ExportFormat

    suspend fun setSyncWith(syncWith: SyncWith)
    suspend fun getSyncWithOnce(): SyncWith

    suspend fun setSyncFrequency(frequency: SyncFrequency)
    suspend fun getSyncFrequencyOnce(): SyncFrequency

    suspend fun setPushNotifications(enabled: Boolean)
    suspend fun getPushNotificationsOnce(): Boolean

    suspend fun setEmailAlerts(enabled: Boolean)
    suspend fun getEmailAlertsOnce(): Boolean

    suspend fun setUserName(name: String)
    suspend fun getUserNameOnce(): String
}