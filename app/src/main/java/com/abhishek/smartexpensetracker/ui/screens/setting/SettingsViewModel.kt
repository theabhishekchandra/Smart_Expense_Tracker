// ui/screens/profile/SettingsViewModel.kt
package com.abhishek.smartexpensetracker.ui.screens.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.smartexpensetracker.core.datastore.BusinessMode
import com.abhishek.smartexpensetracker.core.datastore.PremiumType
import com.abhishek.smartexpensetracker.core.datastore.ThemeType
import com.abhishek.smartexpensetracker.core.datastore.AppPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserPreferences(
    val themeMode: ThemeType = ThemeType.LIGHT,
    val isBusinessMode: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPref: AppPreferencesRepository
) : ViewModel() {

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage

    private val _loader = MutableStateFlow(false)
    val loader: StateFlow<Boolean> = _loader

    val userPreferences: StateFlow<UserPreferences> =
        combine(
            appPref.themeMode,      // Flow<ThemeType>
            appPref.businessMode    // Flow<BusinessMode>
        ) { theme, business ->
            UserPreferences(
                themeMode = theme,
                isBusinessMode = business == BusinessMode.Business
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserPreferences()
        )
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        viewModelScope.launch {
            _toastMessage.emit("Error: ${exception.localizedMessage}")
        }
        _loader.value = false
    }

    fun setTheme(mode: ThemeType) {
        viewModelScope.launch(exceptionHandler) {
            _loader.value = true
            appPref.setThemeMode(mode)
            _toastMessage.emit("App theme changed to ${mode.value} mode.")
            _loader.value = false
        }
    }

    fun setBusinessMode(enabled: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            _loader.value = true
            appPref.setBusinessMode(enabled)
            _toastMessage.emit("App mode changed to ${if (enabled) "Business" else "Personal"}")
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

