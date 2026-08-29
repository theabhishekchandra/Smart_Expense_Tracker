package com.abhishek.smartexpensetracker.core.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.abhishek.smartexpensetracker.data.model.UserRole
import com.abhishek.smartexpensetracker.ui.screens.business.EditBusinessDetailsScreen
import com.abhishek.smartexpensetracker.ui.screens.expense.AddExpenseScreen
import com.abhishek.smartexpensetracker.ui.screens.expense.ExpenseDetailScreen
import com.abhishek.smartexpensetracker.ui.screens.home.HomeScreen
import com.abhishek.smartexpensetracker.ui.screens.home.HomeViewModel
import com.abhishek.smartexpensetracker.ui.screens.expense.ExpenseListScreen
import com.abhishek.smartexpensetracker.ui.screens.login.viewmodel.ExpenseViewModel
import com.abhishek.smartexpensetracker.ui.screens.profile.EditProfileScreen
import com.abhishek.smartexpensetracker.ui.screens.profile.ProfileScreen
import com.abhishek.smartexpensetracker.ui.screens.setting.SettingsScreen
import com.abhishek.smartexpensetracker.ui.screens.setting.SettingsViewModel
import com.abhishek.smartexpensetracker.ui.screens.subscription.SubscriptionPlansScreen

fun NavGraphBuilder.mainNavGraph(navManager: NavManager) {

    composable(
        route = ScreenRoutes.Home.route
    ) { navBackStackEntry ->
        val viewModel : HomeViewModel = hiltViewModel(navBackStackEntry)
        HomeScreen(
            navManager = navManager,
            viewModel = viewModel
        )
    }

    composable(ScreenRoutes.ExpenseList.route){ navBackStackEntry ->
        val viewModel: ExpenseViewModel = hiltViewModel(navBackStackEntry)
        ExpenseListScreen(
            navManager = navManager,
            viewModel,
            userRole = UserRole.ADMIN,
            currentUserId = "user123"
        )
    }
    composable(ScreenRoutes.AddExpense.route) {
        AddExpenseScreen(
            navManager = navManager,
            userRole = UserRole.ADMIN,
            isBusinessMode = true
        )
    }
    composable(
        route = ScreenRoutes.ExpenseDetail.route,
        arguments = RoutesConst.EXPENSE_DETAIL_ARGUMENT
    ) { navBackStackEntry ->
        val viewModel: ExpenseViewModel = hiltViewModel(navBackStackEntry)
        val expenseId = navBackStackEntry.arguments?.getString(RoutesConst.EXPENSE_ID)?.toLongOrNull()
        val uiState by viewModel.uiState.collectAsState()
        val expense = uiState.expenses.find { it.id == expenseId }

        if (expense != null) {
            ExpenseDetailScreen(
                navManager = navManager,
                expense = expense,
                userRole = UserRole.ADMIN,
                viewModel = viewModel
            )
        }
    }

    composable(ScreenRoutes.Settings.route) { navBackStackEntry ->
        val viewModel: SettingsViewModel = hiltViewModel(navBackStackEntry)

        SettingsScreen(
            navManager = navManager,
            viewModel = viewModel
        )
    }

    composable(ScreenRoutes.Subscription.route){ navBackStackEntry ->

        val viewModel : SettingsViewModel = hiltViewModel(navBackStackEntry)

        SubscriptionPlansScreen(
            navManager = navManager,
            settingsViewModel = viewModel
        )
    }

    // Profile Section


    composable(ScreenRoutes.Profile.route) {
        ProfileScreen(
            navManager = navManager,
            name = "Abhishek Chandra",
            email = "ac927920@gmail.com",
            profileImage = "https://via.placeholder.com/150",
            onEditProfile = {},
            onEditBusinessDetails = {}
        )
    }

    composable(ScreenRoutes.EditProfile.route) {
        EditProfileScreen(
            navManager = navManager,
            currentName = "Abhishek Chandra",
            currentEmail = "ac927920@gmail.com",
            currentProfileImage = "https://via.placeholder.com/150",
            onSave = { name, email, profileUrl, phone, dob, gender, currency ->
                // Handle save logic here
            },
            onCancel = {
                // Handle cancel logic here
            }
        )
    }

    // Business Section
    composable(ScreenRoutes.EditBusinessDetails.route) {
        EditBusinessDetailsScreen(
            navManager = navManager,
            currentBusinessName = "Smart Traders",
            currentOwnerName = "Abhishek Chandra",
            currentBusinessLogo = "https://via.placeholder.com/150",
            currentEmail = "business@example.com",
            currentPhone = "+91 9876543210",
            onSave = { _, _, _, _, _, _, _ -> },
            onCancel = {}
        )
    }


    /*
    composable(
        route = "${ScreenRoutes.PaymentScreen.route}/{userId}/{service}/{amount}",
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType },
            navArgument("service") { type = NavType.StringType },
            navArgument("amount") { type = NavType.IntType }
        )
    ) { backStackEntry ->

        val userId = backStackEntry.arguments?.getString("userId") ?: ""
        val service = backStackEntry.arguments?.getString("service") ?: ""
        val amount = backStackEntry.arguments?.getInt("amount") ?: 0

//        PaymentScreen(
//            userId = userId,
//            service = service,
//            amount = amount,
//            navManager = navManager
//        )
    }

   */
}

