package com.abhishek.spendly.core.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.abhishek.spendly.data.model.UserRole
import com.abhishek.spendly.ui.screens.report.ReportsScreen
import com.abhishek.spendly.ui.screens.report.ReportsViewModel

fun NavGraphBuilder.reportNavGraph(navManager: NavManager) {
    navigation(
        startDestination = ScreenRoutes.Reports.route,
        route = RoutesConst.REPORT_GRAPH
    ) {
        composable(ScreenRoutes.Reports.route) { navBackStackEntry ->

            val viewModel : ReportsViewModel = hiltViewModel(navBackStackEntry)
            ReportsScreen(
                navManager = navManager,
                isBusinessMode = true,
                userRole = UserRole.ADMIN,
                reportsViewModel = viewModel

            )

        }
        composable(ScreenRoutes.ExportReport.route) {
//             ExportReportScreen(navManager)
        }
        composable(ScreenRoutes.AIInsights.route) {
            // AIInsightsScreen(navManager)
        }
        composable(ScreenRoutes.ReceiptScanner.route) {
            // ReceiptScannerScreen(navManager)
        }
        composable(ScreenRoutes.AIChat.route) {
            // AIChatScreen(navManager)
        }
    }
}
