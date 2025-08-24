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

    val premiumType: Flow<String?> = dataStore.data.map { prefs ->
        prefs[PreferencesKeys.PREMIUM_TYPE]
    }


    val themeMode: Flow<ThemeType> = dataStore.data.map { prefs ->
        ThemeType.fromValue(prefs[THEME_MODE] ?: ThemeType.LIGHT.value)
    }

    val businessMode: Flow<BusinessMode> = dataStore.data.map { prefs ->
        if (prefs[BUSINESS_MODE] == true) BusinessMode.Business else BusinessMode.Personal
    }

    override suspend fun setThemeMode(mode: ThemeType) {
        dataStore.edit { prefs -> prefs[THEME_MODE] = mode.value }
    }

    override suspend fun setBusinessMode(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[BUSINESS_MODE] = enabled }
    }

    // ----- One-time reads -----
    override suspend fun getIsPremiumOnce(): Boolean =
        dataStore.data.map { it[IS_PREMIUM] ?: false }.first()

    override suspend fun getPremiumTypeOnce(): PremiumType {
        val value = dataStore.data.map { it[PreferencesKeys.PREMIUM_TYPE] }.first()
        return PremiumType.entries.find { it.value == value } ?: PremiumType.BASIC
    }
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
}