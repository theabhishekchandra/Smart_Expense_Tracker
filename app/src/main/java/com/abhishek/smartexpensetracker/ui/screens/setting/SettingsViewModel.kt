package com.abhishek.smartexpensetracker.ui.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.smartexpensetracker.core.datastore.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserPreferences(
    val themeMode: ThemeType = ThemeType.LIGHT,
    val isBusinessMode: Boolean = false,
    val language: Language = Language.ENGLISH,
    val currency: Currency = Currency.RUPEE,
    val exportFormat: ExportFormat = ExportFormat.PDF,
    val syncWith: SyncWith = SyncWith.GOOGLE_DRIVE,
    val syncFrequency: SyncFrequency = SyncFrequency.DAILY,
    val pushNotifications: Boolean = true,
    val emailAlerts: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPref: AppPreferencesRepository
) : ViewModel() {

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    private val _loader = MutableStateFlow(false)
    val loader: StateFlow<Boolean> = _loader

    // Exception handler
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch { _toastMessage.emit("Error: ${exception.localizedMessage}") }
        _loader.value = false
    }
    val isPremium = appPref.isPremiumFlow
    val premiumType = appPref.premiumTypeFlow

    // Combine all preferences into a single state
    val userPreferences: StateFlow<UserPreferences> =
        combine(
            appPref.themeMode,
            appPref.businessMode,
            appPref.languageFlow,
            appPref.currencyFlow,
            appPref.exportFormatFlow,
            appPref.syncWithFlow,
            appPref.syncFrequencyFlow,
            appPref.pushNotificationsFlow,
            appPref.emailAlertsFlow
        ) { arrayOfValues ->
            // arrayOfValues[0] -> theme
            // arrayOfValues[1] -> business
            // arrayOfValues[2] -> language
            // arrayOfValues[3] -> currency
            // arrayOfValues[4] -> exportFormat
            // arrayOfValues[5] -> syncWith
            // arrayOfValues[6] -> frequency
            // arrayOfValues[7] -> push
            // arrayOfValues[8] -> email

            UserPreferences(
                themeMode = arrayOfValues[0] as ThemeType,
                isBusinessMode = (arrayOfValues[1] as BusinessMode) == BusinessMode.Business,
                language = arrayOfValues[2] as Language,
                currency = arrayOfValues[3] as Currency,
                exportFormat = arrayOfValues[4] as ExportFormat,
                syncWith = arrayOfValues[5] as SyncWith,
                syncFrequency = arrayOfValues[6] as SyncFrequency,
                pushNotifications = arrayOfValues[7] as Boolean,
                emailAlerts = arrayOfValues[8] as Boolean
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserPreferences()
        )

    // ---- Setters ----
    fun setTheme(mode: ThemeType) = updatePreference { appPref.setThemeMode(mode); "App theme changed to ${mode.value}." }
    fun setBusinessMode(enabled: Boolean) = updatePreference { appPref.setBusinessMode(enabled); "App mode changed to ${if (enabled) "Business" else "Personal"}" }
    fun setLanguage(language: Language) = updatePreference { appPref.setLanguage(language); "Language changed to ${language.value}" }
    fun setCurrency(currency: Currency) = updatePreference { appPref.setCurrency(currency); "Currency changed to ${currency.value}" }
    fun setExportFormat(format: ExportFormat) = updatePreference { appPref.setExportFormat(format); "Export format changed to ${format.value}" }
    fun setSyncWith(syncWith: SyncWith) = updatePreference { appPref.setSyncWith(syncWith); "Sync provider changed to ${syncWith.value}" }
    fun setSyncFrequency(freq: SyncFrequency) = updatePreference { appPref.setSyncFrequency(freq); "Sync frequency changed to ${freq.value}" }
    fun setPushNotifications(enabled: Boolean) = updatePreference { appPref.setPushNotifications(enabled); "Push notifications ${if (enabled) "enabled" else "disabled"}" }
    fun setEmailAlerts(enabled: Boolean) = updatePreference { appPref.setEmailAlerts(enabled); "Email alerts ${if (enabled) "enabled" else "disabled"}" }

    // Helper function to handle loader and toast
    private fun updatePreference(action: suspend () -> String) {
        viewModelScope.launch(exceptionHandler) {
            _loader.value = true
            val message = action()
            _toastMessage.emit(message)
            _loader.value = false
        }
    }

    fun setPremiumType(premiumType: PremiumType) {
        viewModelScope.launch(exceptionHandler) {
            _loader.value = true
            appPref.setPremiumType(premiumType)
            _toastMessage.emit("You now have ${premiumType.value} Premium.")
            _loader.value = false
        }
    }

    fun setPremium(type: PremiumType, enabled: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            _loader.value = true
            appPref.setPremium(enabled, type)
            _toastMessage.emit("You now have ${type.value} Premium.")
            _loader.value = false
        }
    }


}

