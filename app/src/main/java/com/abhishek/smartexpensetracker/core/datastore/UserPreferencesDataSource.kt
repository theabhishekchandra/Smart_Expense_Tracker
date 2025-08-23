package com.abhishek.smartexpensetracker.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.abhishek.smartexpensetracker.core.datastore.PreferencesKeys.USER_PREFERENCES_NAME
import com.abhishek.smartexpensetracker.core.datastore.PreferencesKeys.THEME_MODE
import com.abhishek.smartexpensetracker.core.datastore.PreferencesKeys.BUSINESS_MODE
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(USER_PREFERENCES_NAME)

@Singleton
class UserPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : IPreferencesDataSource {
    private val dataStore = context.dataStore

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
}
