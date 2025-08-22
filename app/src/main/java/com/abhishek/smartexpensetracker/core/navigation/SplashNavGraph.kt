package com.abhishek.smartexpensetracker.core.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.abhishek.smartexpensetracker.ui.screens.splash.SplashScreen

fun NavGraphBuilder.splashNavGraph(navManager: NavManager) {
    composable(
        route = ScreenRoutes.Splash.route
    ) { navBackStackEntry ->
//        val viewModel = hiltViewModel<SplashViewModel>(navBackStackEntry)
        SplashScreen(
            navManager
//            viewModel
        )
    }
    composable(
        route = ScreenRoutes.OnBoarding.route
    ) { navBackStackEntry ->
//        val viewModel = hiltViewModel<OnboardingViewModel>(navBackStackEntry)
//        OnboardingScreen(
//            navManager,
//            viewModel
//        )
    }
}


/*fun NavGraphBuilder.splashNavGraph(navController: NavController) {
    navigation(
        startDestination = ScreenRoutes.Splash.route,  // "splash_screen"
        route = ScreenRoutes.SplashGraph.route         // "splash_graph"
    ) {
        composable(ScreenRoutes.Splash.route) {
            SplashRoute(
                onNavigateToHome = {
                    navController.navigate(ScreenRoutes.Home.route) {
                        popUpTo(ScreenRoutes.SplashGraph.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(ScreenRoutes.Login.route) {
                        popUpTo(ScreenRoutes.SplashGraph.route) { inclusive = true }
                    }
                }
            )
        }
    }
}*/

