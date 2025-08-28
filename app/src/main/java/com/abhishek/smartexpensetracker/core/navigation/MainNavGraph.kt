package com.abhishek.smartexpensetracker.core.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.abhishek.smartexpensetracker.data.model.ExpenseDM
import com.abhishek.smartexpensetracker.data.model.ExpenseStatus
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

    // TODO : Delete Dummy data.
    val expense = listOf<ExpenseDM>(
        ExpenseDM(title = "Tea", amount = 10.0, category = "Food", notes = "Morning tea", receiptUri = null),
        ExpenseDM(title = "Bus", amount = 20.0, category = "Transport", notes = "Office travel", receiptUri = null),
        ExpenseDM(title = "Lunch", userName = "Rohit Kumar", amount = 100.0, category = "Food", notes = "Lunch with friends", receiptUri = null, timestamp = 1624556800000, status = ExpenseStatus.APPROVED),
        ExpenseDM(title = "Dinner", amount = 150.0, category = "Food", notes = "Dinner with family", receiptUri = null),
        ExpenseDM(title = "Taxi", amount = 50.0, category = "Transport", notes = "Airport taxi", receiptUri = null),
        ExpenseDM(title = "Hotel", amount = 200.0, category = "Accommodation", notes = "Hotel stay", receiptUri = null),
        ExpenseDM(title = "Train", amount = 30.0, category = "Transport", notes = "Train ticket", receiptUri = null),
        ExpenseDM(title = "Car", amount = 100.0, category = "Transport", notes = "Car rental", receiptUri = null),
    )

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
            userRole = UserRole.ADMIN,
            isBusinessMode = true
        )
    }
    composable(ScreenRoutes.ExpenseDetail.route) { navBackStackEntry ->
        val viewModel: ExpenseViewModel = hiltViewModel(navBackStackEntry)

        ExpenseDetailScreen(
            navManager = navManager,
            expense = expense[2],
            userRole = UserRole.ADMIN,
            viewModel = viewModel
        )
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

