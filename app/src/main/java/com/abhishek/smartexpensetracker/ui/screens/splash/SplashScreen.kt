package com.abhishek.smartexpensetracker.ui.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.abhishek.smartexpensetracker.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navManager: NavManager?,
) {
    var scale by remember { mutableFloatStateOf(0f) }

    val scaleAnim = animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(
            durationMillis = 2000,
            easing = { OvershootInterpolator(2f).getInterpolation(it) }
        ), label = "scaleAnimation"
    )

    // Trigger animation
    LaunchedEffect(true) {
        scale = 1f
        delay(2000) // stay on splash for 2 seconds
        // Navigate to next screen (Login or Home)
        navManager?.navigateToRoot(
            ScreenRoutes.Login.route
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(80.dp)
                    .fillMaxSize()
                    .scale(scaleAnim.value)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSplashScreen() {
    SplashScreen(null)
}