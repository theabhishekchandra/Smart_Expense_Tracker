package com.abhishek.smartexpensetracker.ui.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import com.abhishek.smartexpensetracker.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing
import com.abhishek.smartexpensetracker.ui.theme.heroGradientVertical
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navManager: NavManager?,
    viewModel: SplashViewModel = hiltViewModel()
) {
    var scale by remember { mutableFloatStateOf(0f) }
    var textVisible by remember { mutableFloatStateOf(0f) }
    var haloScale by remember { mutableFloatStateOf(0.6f) }

    val scaleAnim = animateFloatAsState(
        targetValue = scale,
        animationSpec = tween(
            durationMillis = 900,
            easing = { OvershootInterpolator(2f).getInterpolation(it) }
        ), label = "logoScale"
    )
    val textAlphaAnim = animateFloatAsState(
        targetValue = textVisible,
        animationSpec = tween(durationMillis = 500, delayMillis = 350),
        label = "textAlpha"
    )
    val haloScaleAnim = animateFloatAsState(
        targetValue = haloScale,
        animationSpec = tween(durationMillis = 1600),
        label = "haloScale"
    )

    // Trigger entrance animation, then navigate on.
    LaunchedEffect(true) {
        scale = 1f
        textVisible = 1f
        haloScale = 1f
        delay(1800)
        val destination = if (viewModel.isOnboardingCompleted) {
            ScreenRoutes.Login.route
        } else {
            ScreenRoutes.OnBoarding.route
        }
        navManager?.navigateToRoot(destination)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(heroGradientVertical()),
            contentAlignment = Alignment.Center
        ) {
            // Soft glowing halo behind the logo for depth, instead of a flat fill.
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .offset(y = (-8).dp)
                    .scale(haloScaleAnim.value)
                    .background(
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.12f),
                        shape = CircleShape
                    )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(140.dp)
                        .scale(scaleAnim.value)
                        .alpha(scaleAnim.value.coerceIn(0f, 1f))
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(top = AppSpacing.md)
                        .alpha(textAlphaAnim.value)
                        .offset(y = (12 * (1f - textAlphaAnim.value)).dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSplashScreen() {
    SplashScreen(null)
}
