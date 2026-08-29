package com.abhishek.spendly.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.abhishek.spendly.core.sharepref.IPreferenceStorage
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
