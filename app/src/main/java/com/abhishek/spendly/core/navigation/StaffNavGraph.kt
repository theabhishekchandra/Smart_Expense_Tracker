package com.abhishek.spendly.core.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.abhishek.spendly.ui.screens.staff.PendingApprovalsScreen
import com.abhishek.spendly.ui.screens.staff.ProcessedExpensesScreen
import com.abhishek.spendly.ui.screens.staff.QuickViewScreen
import com.abhishek.spendly.ui.screens.staff.StaffDashboardScreen
import com.abhishek.spendly.ui.screens.staff.StaffManagementScreen
import com.abhishek.spendly.ui.screens.staff.StaffProfileScreen
import com.abhishek.spendly.ui.screens.staff.StaffReportsScreen
import com.abhishek.spendly.ui.screens.staff.sampleContributions
import com.abhishek.spendly.ui.screens.staff.sampleStaffList

fun NavGraphBuilder.staffNavGraph(navManager: NavManager) {
    navigation(
        startDestination = ScreenRoutes.StaffDashboard.route,
        route = RoutesConst.STAFF_GRAPH
    ) {
        composable(ScreenRoutes.StaffDashboard.route) {
            // Staff Dashboard Screen
            StaffDashboardScreen(navManager = navManager)
        }
        composable(ScreenRoutes.StaffManagement.route) {
            // Staff Management Screen
            StaffManagementScreen(navManager = navManager)
        }
        composable(ScreenRoutes.Approval.route) {
            // Approval Screen
            PendingApprovalsScreen(navManager = navManager)
        }
        composable(ScreenRoutes.StaffReports.route) {
            // Staff Reports Screen
            StaffReportsScreen(navManager = navManager)
        }

        composable(
            route = ScreenRoutes.StaffProfile.route,
            arguments = RoutesConst.STAFF_DETAIL_ARGUMENT
        ) { navBackStackEntry ->
            val staffId = navBackStackEntry.arguments?.getString(RoutesConst.STAFF_ID)?.toIntOrNull()
            val staff = sampleStaffList.find { it.id == staffId }
            if (staff != null) {
                StaffProfileScreen(staff = staff, navManager = navManager)
            } else {
                LaunchedEffect(Unit) { navManager.navigateBack() }
            }
        }

        composable(ScreenRoutes.QuickView.route) {
            QuickViewScreen(
                contributions = sampleContributions,
                isAdmin = false,
                navManager = navManager
            )
        }

        composable(ScreenRoutes.ProcessedExpense.route) {
            ProcessedExpensesScreen(navManager = navManager)
        }
    }
}
