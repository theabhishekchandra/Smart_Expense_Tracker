package com.abhishek.spendly.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.abhishek.spendly.ui.screens.report.PreviewReportsScreen
import com.abhishek.spendly.ui.screens.staff.PendingApprovalsScreen
import com.abhishek.spendly.ui.screens.staff.ProcessedExpensesScreen
import com.abhishek.spendly.ui.screens.staff.QuickViewScreen
import com.abhishek.spendly.ui.screens.staff.Role
import com.abhishek.spendly.ui.screens.staff.Staff
import com.abhishek.spendly.ui.screens.staff.StaffDashboardScreen
import com.abhishek.spendly.ui.screens.staff.StaffManagementScreen
import com.abhishek.spendly.ui.screens.staff.StaffProfileScreen

fun NavGraphBuilder.staffNavGraph(navManager: NavManager) {
    navigation(
        startDestination = ScreenRoutes.StaffDashboard.route,
        route = RoutesConst.STAFF_GRAPH
    ) {
        composable(ScreenRoutes.StaffDashboard.route) {
            // Staff Dashboard Screen
            // Example: StaffDashboardScreen(navManager)
            StaffDashboardScreen()
        }
        composable(ScreenRoutes.StaffManagement.route) {
            // Staff Management Screen
            StaffManagementScreen()
        }
        composable(ScreenRoutes.Approval.route) {
            // Approval Screen
            PendingApprovalsScreen()
        }
        composable(ScreenRoutes.StaffReports.route) {
            // Staff Reports Screen

        }

        composable(ScreenRoutes.StaffProfile.route){
            StaffProfileScreen(
                Staff(
                    id = 1,
                    staffId = "Staff002",
                    name = "John Doe",
                    email = "john@example.com",
                    totalExpense = 1200.0,
                    role = Role.EntryOnly
                )
            )
        }

        composable(ScreenRoutes.QuickView.route){
            QuickViewScreen(
                listOf(),
                false
            )
        }

        composable(ScreenRoutes.ProcessedExpense.route){
            ProcessedExpensesScreen()
        }
    }
}
