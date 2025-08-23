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
import com.abhishek.smartexpensetracker.core.datastore.PreferencesKeys
import com.abhishek.smartexpensetracker.core.datastore.ThemeType
import com.abhishek.smartexpensetracker.core.datastore.UserPreferencesRepository
import com.abhishek.smartexpensetracker.core.navigation.AppNavGraph
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import com.abhishek.smartexpensetracker.core.voice.VoiceManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesRepository: UserPreferencesRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Collect theme + business mode directly
            val themeMode by preferencesRepository.themeMode.collectAsState(initial = ThemeType.LIGHT)
            val businessMode by preferencesRepository.businessMode.collectAsState(initial = false)

            SmartExpenseTrackerTheme(
                darkTheme = themeMode == ThemeType.DARK,
                businessMode = businessMode is BusinessMode.Business
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
