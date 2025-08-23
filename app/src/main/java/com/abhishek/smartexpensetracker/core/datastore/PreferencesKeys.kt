package com.abhishek.smartexpensetracker.core.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    const val USER_PREFERENCES_NAME = "user_preferences"

    // Keys
    const val THEME_MODE_KEY = "theme_mode"
    const val BUSINESS_MODE_KEY = "business_mode"

    // Preferences
    val THEME_MODE = stringPreferencesKey(THEME_MODE_KEY)
    val BUSINESS_MODE = booleanPreferencesKey(BUSINESS_MODE_KEY)
}
