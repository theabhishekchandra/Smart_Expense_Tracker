package com.abhishek.spendly.ui.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.core.navigation.ScreenRoutes
import com.abhishek.spendly.core.voice.VoiceManager
import kotlinx.coroutines.delay

@Composable
fun BaseScaffold(
    topBar: @Composable () -> Unit = {},
    navManager: NavManager?,
    currentRoute: String,
    content: @Composable (PaddingValues) -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
) {
    val context = LocalContext.current
    var backPressedOnce by remember { mutableStateOf(false) }
    val current = navManager?.getCurrentRoute()

    // Handle back press
    BackHandler {
        when (current) {
            ScreenRoutes.Home.route -> {
                if (backPressedOnce) {
                    // Exit app
                    (context as? android.app.Activity)?.finish()
                } else {
                    backPressedOnce = true
                    Toast.makeText(context, "Please Press again to close {APP}", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // Go back to Home
                navManager?.navigateToRoot(ScreenRoutes.Home.route)
            }
        }
    }

    // Reset backPressedOnce after 2 seconds using LaunchedEffect in composable scope
    if (backPressedOnce) {
        LaunchedEffect(backPressedOnce) {
            delay(2000)
            backPressedOnce = false
        }
    }

    Scaffold(
        topBar = topBar,
        bottomBar = {
            BottomNavigationBar(
                selectedRoute = currentRoute,
                onItemSelected = { route ->
                    if (route == ScreenRoutes.Voice.route) {
                        VoiceManager.toggleListening()
                    } else {
                        VoiceManager.stopListening()
                        navManager?.navigationForBottomBar(route)
                    }
                }
            )
        },
        floatingActionButton = floatingActionButton
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Main screen content
            content(padding)

            // Overlay sits above bottom bar but below screen content
            Box(modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp)) {
                VoiceOverlayHost()
            }
        }
    }
}

