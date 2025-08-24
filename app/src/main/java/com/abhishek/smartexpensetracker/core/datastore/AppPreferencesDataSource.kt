package com.abhishek.smartexpensetracker.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.abhishek.smartexpensetracker.core.datastore.PreferencesKeys.USER_PREFERENCES_NAME
import com.abhishek.smartexpensetracker.core.datastore.PreferencesKeys.THEME_MODE
import com.abhishek.smartexpensetracker.core.datastore.PreferencesKeys.BUSINESS_MODE
import com.abhishek.smartexpensetracker.core.datastore.PreferencesKeys.IS_PREMIUM
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(USER_PREFERENCES_NAME)

@Singleton
class AppPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : IPreferencesDataSource {
    private val dataStore = context.dataStore

    // ----- Flow Observers -----
    val isPremium: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[IS_PREMIUM] ?: false
    }

    val premiumType: Flow<PremiumType> = dataStore.data.map { prefs ->
        PremiumType.fromValue(prefs[PreferencesKeys.PREMIUM_TYPE])
    }


    val themeMode: Flow<ThemeType> = dataStore.data.map { prefs ->
        ThemeType.fromValue(prefs[THEME_MODE] ?: ThemeType.LIGHT.value)
    }

    val businessMode: Flow<BusinessMode> = dataStore.data.map { prefs ->
        if (prefs[BUSINESS_MODE] == true) BusinessMode.Business else BusinessMode.Personal
    }

    val languageFlow: Flow<Language> = dataStore.data
        .map { prefs -> Language.fromValue(prefs[PreferencesKeys.LANGUAGE]) }

    val currencyFlow: Flow<Currency> = dataStore.data
        .map { prefs -> Currency.fromValue(prefs[PreferencesKeys.CURRENCY]) }

    val exportFormatFlow: Flow<ExportFormat> = dataStore.data
        .map { prefs -> ExportFormat.fromValue(prefs[PreferencesKeys.EXPORT_FORMAT]) }

    val syncWithFlow: Flow<SyncWith> = dataStore.data
        .map { prefs -> SyncWith.fromValue(prefs[PreferencesKeys.SYNC_WITH]) }

    val syncFrequencyFlow: Flow<SyncFrequency> = dataStore.data
        .map { prefs -> SyncFrequency.fromValue(prefs[PreferencesKeys.SYNC_FREQUENCY]) }

    val pushNotificationsFlow: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[PreferencesKeys.PUSH_NOTIFICATIONS] ?: true }

    val emailAlertsFlow: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[PreferencesKeys.EMAIL_ALERTS] ?: true }


    override suspend fun setThemeMode(mode: ThemeType) {
        dataStore.edit { prefs -> prefs[THEME_MODE] = mode.value }
    }

    override suspend fun setBusinessMode(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[BUSINESS_MODE] = enabled }
    }

    // ----- One-time reads -----
    override suspend fun getIsPremiumOnce(): Boolean =
        dataStore.data.map { it[IS_PREMIUM] ?: false }.first()

    override suspend fun getPremiumTypeOnce(): PremiumType =
        PremiumType.fromValue(dataStore.data.map { it[PreferencesKeys.PREMIUM_TYPE] }.first())

    override suspend fun setPremiumType(type: PremiumType) {
        dataStore.edit { prefs -> prefs[PreferencesKeys.PREMIUM_TYPE] = type.value }
    }

    override suspend fun setPremium(enabled: Boolean, type: PremiumType) {
        dataStore.edit { prefs ->
            prefs[IS_PREMIUM] = enabled
            if (enabled) {
                prefs[PreferencesKeys.PREMIUM_TYPE] = type.value
            } else {
                prefs.remove(PreferencesKeys.PREMIUM_TYPE)
            }
        }
    }


    override suspend fun setPushNotifications(enabled: Boolean) {
        dataStore.edit { it[PreferencesKeys.PUSH_NOTIFICATIONS] = enabled }
    }

    override suspend fun setEmailAlerts(enabled: Boolean) {
        dataStore.edit { it[PreferencesKeys.EMAIL_ALERTS] = enabled }
    }

    override suspend fun setLanguage(language: Language) {
        dataStore.edit { it[PreferencesKeys.LANGUAGE] = language.value }
    }

    override suspend fun getLanguageOnce(): Language =
        Language.fromValue(dataStore.data.map { it[PreferencesKeys.LANGUAGE] }.first())

    override suspend fun setCurrency(currency: Currency) {
        dataStore.edit { it[PreferencesKeys.CURRENCY] = currency.value }
    }

    override suspend fun getCurrencyOnce(): Currency =
        Currency.fromValue(dataStore.data.map { it[PreferencesKeys.CURRENCY] }.first())

    override suspend fun setExportFormat(format: ExportFormat) {
        dataStore.edit { it[PreferencesKeys.EXPORT_FORMAT] = format.value }
    }

    override suspend fun getExportFormatOnce(): ExportFormat =
        ExportFormat.fromValue(dataStore.data.map { it[PreferencesKeys.EXPORT_FORMAT] }.first())

    override suspend fun setSyncWith(syncWith: SyncWith) {
        dataStore.edit { it[PreferencesKeys.SYNC_WITH] = syncWith.value }
    }

    override suspend fun getSyncWithOnce(): SyncWith =
        SyncWith.fromValue(dataStore.data.map { it[PreferencesKeys.SYNC_WITH] }.first())

    override suspend fun setSyncFrequency(frequency: SyncFrequency) {
        dataStore.edit { it[PreferencesKeys.SYNC_FREQUENCY] = frequency.value }
    }

    override suspend fun getSyncFrequencyOnce(): SyncFrequency =
        SyncFrequency.fromValue(dataStore.data.map { it[PreferencesKeys.SYNC_FREQUENCY] }.first())


    override suspend fun getPushNotificationsOnce(): Boolean =
        dataStore.data.map { it[PreferencesKeys.PUSH_NOTIFICATIONS] ?: true }.first()

    override suspend fun getEmailAlertsOnce(): Boolean =
        dataStore.data.map { it[PreferencesKeys.EMAIL_ALERTS] ?: true }.first()
}