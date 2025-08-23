// ui/screens/profile/SettingsViewModel.kt
package com.abhishek.smartexpensetracker.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.smartexpensetracker.core.datastore.BusinessMode
import com.abhishek.smartexpensetracker.core.datastore.ThemeType
import com.abhishek.smartexpensetracker.core.datastore.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserPreferences(
    val themeMode: ThemeType = ThemeType.LIGHT,
    val isBusinessMode: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Combine both flows into a single UI state
    val userPreferences: StateFlow<UserPreferences> =
        combine(
            userPreferencesRepository.themeMode,      // Flow<ThemeType>
            userPreferencesRepository.businessMode    // Flow<BusinessMode>
        ) { theme, business ->
            UserPreferences(
                themeMode = theme,
                isBusinessMode = business == BusinessMode.Business
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserPreferences() // defaults
        )

    fun setTheme(mode: ThemeType) {
        viewModelScope.launch {
            userPreferencesRepository.setThemeMode(mode)
        }
    }

    fun setBusinessMode(enabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setBusinessMode(enabled)
        }
    }
}
