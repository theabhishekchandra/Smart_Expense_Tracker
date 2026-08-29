package com.abhishek.smartexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.abhishek.smartexpensetracker.core.datastore.BusinessMode

import com.abhishek.smartexpensetracker.core.datastore.ThemeType
import com.abhishek.smartexpensetracker.core.datastore.AppPreferencesRepository
import com.abhishek.smartexpensetracker.core.datastore.PremiumType
import com.abhishek.smartexpensetracker.core.navigation.AppNavGraph
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import com.abhishek.smartexpensetracker.core.voice.VoiceManager
import com.abhishek.smartexpensetracker.ui.theme.AppFlavor
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


            SmartExpenseTrackerTheme(
                darkTheme = themeMode == ThemeType.DARK,
                appFlavor = appFlavor,
            ) {
//                AppSetup()
                AppNavGraph()

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
fun AppSetup(){
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colorScheme.background

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = backgroundColor,
            darkIcons = useDarkIcons
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        AppNavGraph()
    }
}
