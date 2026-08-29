package com.abhishek.spendly.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.EntryPointAccessors
import androidx.compose.ui.platform.LocalContext
import com.abhishek.spendly.core.di.NavManagerEntryPoint
import com.abhishek.spendly.ui.theme.Motion

// Root NavHost that connects NavController and sets up screen graphs
@Composable
fun AppNavGraph() {
    val context = LocalContext.current.applicationContext
    val navManager = remember {
        EntryPointAccessors.fromApplication(
            context,
            NavManagerEntryPoint::class.java
        ).navManager()
    }

    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        navManager.setNavController(navController)
    }

    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.Splash.route,
        enterTransition = Motion.screenEnter,
        exitTransition = Motion.screenExit,
        popEnterTransition = Motion.screenPopEnter,
        popExitTransition = Motion.screenPopExit
    ) {
        splashNavGraph(navManager)
        authNavGraph(navManager)
        mainNavGraph(navManager)
        staffNavGraph(navManager)
        reportNavGraph(navManager)
    }
}
