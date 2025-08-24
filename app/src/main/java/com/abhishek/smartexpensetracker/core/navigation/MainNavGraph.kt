package com.abhishek.smartexpensetracker.core.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.data.model.ExpenseDM
import com.abhishek.smartexpensetracker.data.model.ExpenseStatus
import com.abhishek.smartexpensetracker.data.model.UserRole
import com.abhishek.smartexpensetracker.ui.screens.expense.AddExpenseScreen
import com.abhishek.smartexpensetracker.ui.screens.expense.ExpenseDetailScreen
import com.abhishek.smartexpensetracker.ui.screens.home.AiTip
import com.abhishek.smartexpensetracker.ui.screens.home.ApprovalItem
import com.abhishek.smartexpensetracker.ui.screens.home.BudgetProgress
import com.abhishek.smartexpensetracker.ui.screens.home.CategorySlice
import com.abhishek.smartexpensetracker.ui.screens.home.DailyPoint
import com.abhishek.smartexpensetracker.ui.screens.home.ExpenseItem
import com.abhishek.smartexpensetracker.ui.screens.home.HomeScreen
import com.abhishek.smartexpensetracker.ui.screens.home.HomeUiState
import com.abhishek.smartexpensetracker.ui.screens.home.ImprovementIdea
import com.abhishek.smartexpensetracker.ui.screens.listscreen.ExpenseListScreen
import com.abhishek.smartexpensetracker.ui.screens.login.viewmodel.ExpenseViewModel
import com.abhishek.smartexpensetracker.ui.screens.profile.ProfileScreen
import com.abhishek.smartexpensetracker.ui.screens.report.ReportsScreen
import com.abhishek.smartexpensetracker.ui.screens.report.ReportsViewModel
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
    val sample = HomeUiState(
        userName = "Abhishek",
        todayTotal = 2350.0,
        monthExpense = 45210.0,
        monthIncome = 75000.0,
        categoryBreakdown = listOf(
            CategorySlice("Travel", 12000.0),
            CategorySlice("Food", 9800.0),
            CategorySlice("Staff", 18800.0),
            CategorySlice("Utility", 4600.0)
        ),
        weeklyTrend = listOf(
            DailyPoint("Mon", 4200.0),
            DailyPoint("Tue", 1800.0),
            DailyPoint("Wed", 2600.0),
            DailyPoint("Thu", 3900.0),
            DailyPoint("Fri", 1600.0),
            DailyPoint("Sat", 5100.0),
            DailyPoint("Sun", 2200.0)
        ),
        budgets = listOf(
            BudgetProgress("Travel", 12000.0, 10000.0),
            BudgetProgress("Food", 9800.0, 12000.0),
            BudgetProgress("Staff", 18800.0, 20000.0)
        ),
        aiTips = listOf(
            AiTip("Travel is trending high", "Consider switching to monthly passes. You’re 20% above last month."),
            AiTip("Food spike on weekends", "Batch-order supplies midweek to avoid surge pricing.")
        ),
        improvements = listOf(
            ImprovementIdea("Set Travel budget", "Set"),
            ImprovementIdea("Enable daily reminders", "Enable"),
            ImprovementIdea("Scan receipts", "Scan")
        ),
        pendingApprovals = listOf(
            ApprovalItem("1", "Rohit", "Cab from client visit", 540.0, true),
            ApprovalItem("2", "Sneha", "Lunch with vendor", 920.0, true)
        ),
        recent = listOf(
            ExpenseItem("1", "Printer ink", "Utility", 780.0, "10:24 AM"),
            ExpenseItem("2", "Team lunch", "Food", 2450.0, "Yesterday"),
            ExpenseItem("3", "Airport taxi", "Travel", 820.0, "Yesterday")
        )
    )

    composable(
        route = ScreenRoutes.Home.route
    ) {
        HomeScreen(
            navManager = navManager,
            state = sample
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


    composable(ScreenRoutes.Profile.route) {
        ProfileScreen(
            navManager = navManager,
            name = "Abhishek Chandra",
            email = "ac927920@gmail.com",
            profileImage = "https://via.placeholder.com/150",
            isPremium = false,
            onEditProfile = {},
            onEditBusinessDetails = {}
        )
    }

    composable(ScreenRoutes.Settings.route) {
        SettingsScreen(
            onBackClick = {},
            navManager = navManager
        )
    }

    composable(ScreenRoutes.Subscription.route){ navBackStackEntry ->

        val viewModel : SettingsViewModel = hiltViewModel(navBackStackEntry)

        SubscriptionPlansScreen(
            navManager = navManager,
            settingsViewModel = viewModel
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

