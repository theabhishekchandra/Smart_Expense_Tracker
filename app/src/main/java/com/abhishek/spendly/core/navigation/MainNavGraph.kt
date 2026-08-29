package com.abhishek.spendly.core.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.abhishek.spendly.data.model.UserRole
import com.abhishek.spendly.ui.screens.business.EditBusinessDetailsScreen
import com.abhishek.spendly.ui.screens.expense.AddExpenseScreen
import com.abhishek.spendly.ui.screens.expense.ExpenseDetailScreen
import com.abhishek.spendly.ui.screens.home.HomeScreen
import com.abhishek.spendly.ui.screens.home.HomeViewModel
import com.abhishek.spendly.ui.screens.expense.ExpenseListScreen
import com.abhishek.spendly.ui.screens.legal.AboutUsScreen
import com.abhishek.spendly.ui.screens.legal.FaqScreen
import com.abhishek.spendly.ui.screens.legal.PrivacyPolicyScreen
import com.abhishek.spendly.ui.screens.legal.TermsAndConditionsScreen
import com.abhishek.spendly.ui.screens.login.viewmodel.ExpenseViewModel
import com.abhishek.spendly.ui.screens.profile.EditProfileScreen
import com.abhishek.spendly.ui.screens.profile.ProfileScreen
import com.abhishek.spendly.ui.screens.setting.SettingsScreen
import com.abhishek.spendly.ui.screens.setting.SettingsViewModel
import com.abhishek.spendly.ui.screens.subscription.SubscriptionPlansScreen

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
            onEditProfile = { navManager.navigate(ScreenRoutes.EditProfile.route) },
            onEditBusinessDetails = { navManager.navigate(ScreenRoutes.EditBusinessDetails.route) }
        )
    }

    composable(ScreenRoutes.EditProfile.route) { navBackStackEntry ->
        val viewModel: SettingsViewModel = hiltViewModel(navBackStackEntry)
        val profile by viewModel.profileInfo.collectAsState()
        val prefs by viewModel.userPreferences.collectAsState()

        EditProfileScreen(
            navManager = navManager,
            currentName = profile.name,
            currentEmail = profile.email,
            currentProfileImage = profile.profileImage,
            currentPhone = profile.phone,
            currentDob = profile.dob,
            currentGender = profile.gender,
            currentCurrency = prefs.currency.value,
            onSave = { name, email, profileUrl, phone, dob, gender, currency ->
                viewModel.saveProfile(name, email, profileUrl, phone, dob, gender, currency)
                navManager.navigateBack()
            },
            onCancel = {
                navManager.navigateBack()
            }
        )
    }

    // Business Section
    composable(ScreenRoutes.EditBusinessDetails.route) { navBackStackEntry ->
        val viewModel: SettingsViewModel = hiltViewModel(navBackStackEntry)
        val business by viewModel.businessInfo.collectAsState()
        val prefs by viewModel.userPreferences.collectAsState()

        EditBusinessDetailsScreen(
            navManager = navManager,
            currentBusinessName = business.businessName,
            currentOwnerName = business.ownerName,
            currentBusinessLogo = business.logoUrl,
            currentEmail = business.email,
            currentPhone = business.phone,
            currentBusinessType = business.businessType,
            currentCurrency = prefs.currency.value,
            onSave = { businessName, ownerName, logoUrl, email, phone, businessType, currency ->
                viewModel.saveBusinessProfile(businessName, ownerName, logoUrl, email, phone, businessType, currency)
                navManager.navigateBack()
            },
            onCancel = {
                navManager.navigateBack()
            }
        )
    }


    // Legal & Support
    composable(ScreenRoutes.PrivacyPolicy.route) {
        PrivacyPolicyScreen(navManager = navManager)
    }
    composable(ScreenRoutes.TermsAndConditions.route) {
        TermsAndConditionsScreen(navManager = navManager)
    }
    composable(ScreenRoutes.AboutUs.route) {
        AboutUsScreen(navManager = navManager)
    }
    composable(ScreenRoutes.FAQ.route) {
        FaqScreen(navManager = navManager)
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

