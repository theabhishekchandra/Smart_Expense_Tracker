package com.abhishek.smartexpensetracker.utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFS_NAME = "smart_expense_prefs"
private val Context.dataStore by preferencesDataStore(PREFS_NAME)

class DataStoreManager(private val context: Context) {

    companion object {
        private val KEY_DARK = booleanPreferencesKey("pref_dark_mode")
        fun create(context: Context) = DataStoreManager(context.applicationContext)
    }

    fun isDarkTheme(): Flow<Boolean?> =
        context.dataStore.data.map { prefs -> prefs[KEY_DARK] }

    suspend fun setDarkTheme(value: Boolean) {
        context.dataStore.edit { prefs -> prefs[KEY_DARK] = value }
    }
}
