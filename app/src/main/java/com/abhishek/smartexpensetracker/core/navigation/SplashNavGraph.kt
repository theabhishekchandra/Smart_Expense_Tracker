package com.abhishek.smartexpensetracker.core.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.abhishek.smartexpensetracker.ui.screens.splash.OnboardingScreen
import com.abhishek.smartexpensetracker.ui.screens.splash.OnboardingViewModel
import com.abhishek.smartexpensetracker.ui.screens.splash.SplashScreen

fun NavGraphBuilder.splashNavGraph(navManager: NavManager) {
    composable(
        route = ScreenRoutes.Splash.route
    ) { navBackStackEntry ->
        SplashScreen(
            navManager
        )
    }
    composable(
        route = ScreenRoutes.OnBoarding.route
    ) { navBackStackEntry ->
        val viewModel = hiltViewModel<OnboardingViewModel>(navBackStackEntry)
        OnboardingScreen(
            onFinish = {
                viewModel.completeOnboarding()
                navManager.navigateToRoot(ScreenRoutes.Login.route)
            }
        )
    }
}
