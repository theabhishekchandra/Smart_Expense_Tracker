package com.abhishek.spendly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.abhishek.spendly.core.datastore.BusinessMode

import com.abhishek.spendly.core.datastore.ThemeType
import com.abhishek.spendly.core.datastore.AppPreferencesRepository
import com.abhishek.spendly.core.datastore.PremiumType
import com.abhishek.spendly.core.navigation.AppNavGraph
import com.abhishek.spendly.ui.theme.SpendlyTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import com.abhishek.spendly.core.voice.VoiceManager
import com.abhishek.spendly.ui.theme.AppFlavor
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesRepository: AppPreferencesRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Collect theme + business mode directly
            val themeMode by preferencesRepository.themeMode.collectAsState(initial = ThemeType.LIGHT)
            val businessMode by preferencesRepository.businessMode.collectAsState(initial = BusinessMode.Personal)
            val premiumType by preferencesRepository.premiumTypeFlow.collectAsState(initial = PremiumType.BASIC)

            val appFlavor = if (businessMode is BusinessMode.Business) {
                when (premiumType) {
                    PremiumType.BASIC -> AppFlavor.BUSINESS_BASIC
                    PremiumType.MONTHLY -> AppFlavor.BUSINESS_PREMIUM
                    PremiumType.YEARLY -> AppFlavor.BUSINESS_PREMIUM
                }
            } else {
                when (premiumType) {
                    PremiumType.BASIC -> AppFlavor.PERSONAL_BASIC
                    PremiumType.MONTHLY -> AppFlavor.PERSONAL_PREMIUM
                    PremiumType.YEARLY -> AppFlavor.PERSONAL_PREMIUM
                }
            }


            val isDark = themeMode == ThemeType.DARK

            SpendlyTheme(
                darkTheme = isDark,
                appFlavor = appFlavor,
            ) {
                AppSetup(darkTheme = isDark)
            }
        }

        VoiceManager.init(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        VoiceManager.stopListening()
    }
}
@Composable
fun AppSetup(darkTheme: Boolean){
    val systemUiController = rememberSystemUiController()
    val backgroundColor = MaterialTheme.colorScheme.background

    LaunchedEffect(darkTheme, backgroundColor) {
        systemUiController.setStatusBarColor(
            color = backgroundColor,
            darkIcons = !darkTheme
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        AppNavGraph()
    }
}
