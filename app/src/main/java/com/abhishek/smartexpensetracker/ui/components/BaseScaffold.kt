package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.core.voice.VoiceManager

@Composable
fun BaseScaffold(
    topBar: @Composable () -> Unit = {},
    navManager: NavManager?,
    currentRoute: String,
    content: @Composable (PaddingValues) -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
) {
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
                        navManager?.navigate(route)
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

