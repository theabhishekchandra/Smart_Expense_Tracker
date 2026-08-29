package com.abhishek.smartexpensetracker.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.abhishek.smartexpensetracker.core.sharepref.IPreferenceStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferenceStorage: IPreferenceStorage
) : ViewModel() {
    fun completeOnboarding() {
        preferenceStorage.onBoardingCompleted = true
    }
}
