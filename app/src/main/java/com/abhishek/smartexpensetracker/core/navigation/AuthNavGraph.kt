package com.abhishek.smartexpensetracker.core.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.abhishek.smartexpensetracker.ui.screens.login.LoginScreen
import com.abhishek.smartexpensetracker.ui.screens.login.ResetPasswordScreen
import com.abhishek.smartexpensetracker.ui.screens.login.SignupScreen
import com.abhishek.smartexpensetracker.ui.screens.login.CreatePasswordScreen
import com.abhishek.smartexpensetracker.ui.screens.login.viewmodel.AuthViewModel

fun NavGraphBuilder.authNavGraph(navManager: NavManager) {
    navigation(
        startDestination = ScreenRoutes.Login.route,
        route = RoutesConst.AUTH_GRAPH
    ) {
        composable(
            route = ScreenRoutes.Login.route,
//            arguments = listOf(
//                navArgument(RoutesConst.LOGIN_EMAIL) {
//                    type = NavType.StringType
//                    nullable = true
//                    defaultValue = ""
//                }
//            )
        ) { backStackEntry ->
            val viewModel: AuthViewModel = hiltViewModel(backStackEntry)
            LoginScreen(navManager = navManager, viewModel = viewModel)
        }
        composable(
            route = RoutesConst.SIGNUP_SCREEN
        ){
            SignupScreen(navManager)
        }
//        composable(
//            route =RoutesConst.CREATE_NEW_PASSWORD_SCREEN
//        ) {
//            CreatePasswordScreen(navManager)
//        }
        composable(
            route = RoutesConst.RESET_PASSWORD_SCREEN
        ) {
            ResetPasswordScreen(navManager)
        }
        /*composable(RoutesConst.SIGNUP_SCREEN) {
            SignupScreen(
                onNavigateBack = { navManager.navigate(ScreenRoutes.Login.route) },
                onNavigate = { navManager.navigate(it) }
            )
        }*/

    }
}

