package com.abhishek.spendly.core.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    const val USER_PREFERENCES_NAME = "user_preferences"

    // Keys
    const val THEME_MODE_KEY = "theme_mode"
    const val BUSINESS_MODE_KEY = "business_mode"
    const val IS_PREMIUM_KEY = "is_premium"
    const val PREMIUM_TYPE_KEY = "premium_type"

    // Preferences
    val THEME_MODE = stringPreferencesKey(THEME_MODE_KEY)
    val BUSINESS_MODE = booleanPreferencesKey(BUSINESS_MODE_KEY)
    val IS_PREMIUM = booleanPreferencesKey(IS_PREMIUM_KEY)
    val PREMIUM_TYPE = stringPreferencesKey(PREMIUM_TYPE_KEY)

    val LANGUAGE = stringPreferencesKey("language") // Hindi, English
    val CURRENCY = stringPreferencesKey("currency") // Rupee, Dollar, Euro
    val EXPORT_FORMAT = stringPreferencesKey("export_format") // PDF, CSV, Excel
    val SYNC_WITH = stringPreferencesKey("sync_with") // Google Drive, App Drive, OneDrive
    val SYNC_FREQUENCY = stringPreferencesKey("sync_frequency") // Daily, Weekly, Monthly
    val PUSH_NOTIFICATIONS = booleanPreferencesKey("push_notifications")
    val EMAIL_ALERTS = booleanPreferencesKey("email_alerts")
    val SMS_ALERTS = booleanPreferencesKey("sms_alerts")
    val USER_NAME = stringPreferencesKey("user_name")
}
