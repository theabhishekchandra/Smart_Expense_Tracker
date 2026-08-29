package com.abhishek.spendly.core.sharepref

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

class SharedIPreferenceStorage @Inject constructor(
    private val prefs: SharedPreferences
) : IPreferenceStorage {

    override var onBoardingCompleted: Boolean
        get() = prefs.getBoolean("onboarding", false)
        set(value) = prefs.edit { putBoolean("onboarding", value) }

    override var isUserLoggedIn: Boolean
        get() = prefs.getBoolean("is_user_logged_in", false)
        set(value) = prefs.edit { putBoolean("is_user_logged_in", value) }

    override var authToken: String?
        get() = prefs.getString("auth_token", null)
        set(value) = prefs.edit { putString("auth_token", value) }
    override var userId: String?
        get() = prefs.getString("user_id", null)
        set(value) {prefs.edit { putString("user_id", value) }}

    override fun clearAll() {
        prefs.edit { clear() }
    }
}

