package com.abhishek.spendly.core.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

object  RoutesConst {

    // Graphs
    const val AUTH_GRAPH = "AUTH_GRAPH"
    const val MAIN_GRAPH = "MAIN_GRAPH"
    const val SPLASH_GRAPH = "SPLASH_GRAPH"
    const val STAFF_GRAPH = "STAFF_GRAPH"
    const val REPORT_GRAPH = "REPORT_GRAPH"
    const val LENDER_GRAPH = "LENDER_GRAPH"

    // Splash & Onboarding
    const val SPLASH_SCREEN = "SPLASH_SCREEN"
    const val ONBOARDING_SCREEN = "ONBOARDING_SCREEN"

    // Auth Screens
    const val LOGIN_SCREEN = "LOGIN_SCREEN"
    const val REGISTER_SCREEN = "REGISTER_SCREEN"
    const val SIGNUP_SCREEN = "SIGNUP_SCREEN"
    const val LOGIN_WITH_OTP_SCREEN = "LOGIN_WITH_OTP_SCREEN"
    const val SIGNUP_WITH_OTP_SCREEN = "SIGNUP_WITH_OTP_SCREEN"
    const val FORGOT_PASSWORD_SCREEN ="FORGOT_PASSWORD_SCREEN"
    const val RESET_PASSWORD_SCREEN = "RESET_PASSWORD_SCREEN"
    const val VERIFY_EMAIL_SCREEN = "VERIFY_EMAIL_SCREEN"
    const val VERIFY_OTP_SCREEN = "VERIFY_OTP_SCREEN"
    const val CHANGE_PASSWORD_SCREEN = "CHANGE_PASSWORD_SCREEN"
    const val SET_NEW_PASSWORD_SCREEN = "SET_NEW_PASSWORD_SCREEN"

    // Main Screens With BottomBarNavigation
    const val HOME_SCREEN = "HOME_SCREEN"
    const val NOTIFICATION_SCREEN = "NOTIFICATION_SCREEN"
    const val VOICE_SCREEN = "VOICE_SCREEN"

    // Expense & Income
    const val ADD_EXPENSE_SCREEN = "ADD_EXPENSE_SCREEN"
    const val ADD_INCOME_SCREEN = "ADD_INCOME_SCREEN"
    const val EXPENSE_LIST_SCREEN = "EXPENSE_LIST_SCREEN"
    const val EXPENSE_DETAIL_SCREEN = "EXPENSE_DETAIL_SCREEN"

    // Lender / Borrower
    const val LENDER_LIST_SCREEN = "LENDER_LIST_SCREEN"
    const val LENDER_DETAILS_SCREEN = "LENDER_DETAILS_SCREEN"
    const val ADD_LENDER_SCREEN = "ADD_LENDER_SCREEN"
    const val EDIT_LENDER_SCREEN = "EDIT_LENDER_SCREEN"

    // Budget & Reports
    const val BUDGET_SETUP_SCREEN = "BUDGET_SETUP_SCREEN"
    const val BUDGET_TRACKING_SCREEN = "BUDGET_TRACKING_SCREEN"
    const val REPORTS_SCREEN = "REPORTS_SCREEN"
    const val EXPORT_REPORT_SCREEN = "EXPORT_REPORT_SCREEN"

    // AI & Smart Features
    const val AI_INSIGHTS_SCREEN = "AI_INSIGHTS_SCREEN"
    const val RECEIPT_SCANNER_SCREEN = "RECEIPT_SCANNER_SCREEN"
    const val AI_CHAT_SCREEN = "AI_CHAT_SCREEN"

    // Staff Management
    const val STAFF_MANAGEMENT_SCREEN = "STAFF_MANAGEMENT_SCREEN"
    const val STAFF_DASHBOARD_SCREEN = "STAFF_DASHBOARD_SCREEN"
    const val APPROVAL_SCREEN = "APPROVAL_SCREEN"
    const val STAFF_REPORTS_SCREEN = "STAFF_REPORTS_SCREEN"
    const val STAFF_PROFILE_SCREEN = "STAFF_PROFILE_SCREEN"
    const val QUICK_VIEW_SCREEN = "QUICK_VIEW_SCREEN"
    const val PROCESSED_EXPENSE_SCREEN = "PROCESSED_EXPENSE_SCREEN"

    // Profile & Settings
    const val PROFILE_SCREEN = "PROFILE_SCREEN"
    const val EDIT_PROFILE_SCREEN = "EDIT_PROFILE_SCREEN"
    const val BUSINESS_DETAILS_SCREEN = "BUSINESS_DETAILS_SCREEN"
    const val EDIT_BUSINESS_DETAILS_SCREEN = "EDIT_BUSINESS_DETAILS_SCREEN"
    const val SETTINGS_SCREEN = "SETTINGS_SCREEN"
    const val BACKUP_SCREEN = "BACKUP_SCREEN"
    const val SUBSCRIPTION_SCREEN = "SUBSCRIPTION_SCREEN"

    // Support & Others
    const val HELP_SCREEN = "HELP_SCREEN"
    const val FAQ_SCREEN = "FAQ_SCREEN"
    const val CONTACT_US_SCREEN = "CONTACT_US_SCREEN"
    const val ABOUT_US_SCREEN = "ABOUT_US_SCREEN"
    const val TERMS_AND_CONDITIONS_SCREEN = "TERMS_AND_CONDITIONS_SCREEN"
    const val PRIVACY_POLICY_SCREEN = "PRIVACY_POLICY_SCREEN"
    const val FEEDBACK_SCREEN = "FEEDBACK_SCREEN"

    // Arguments
    const val EXPENSE_ID = "expense_id"
    const val STAFF_ID = "staff_id"
    const val LENDER_ID = "lender_id"

    val EXPENSE_DETAIL_ARGUMENT = listOf(
        navArgument(EXPENSE_ID) { type = NavType.StringType }
    )

    val STAFF_DETAIL_ARGUMENT = listOf(
        navArgument(STAFF_ID) { type = NavType.StringType }
    )

    val LENDER_DETAIL_ARGUMENT = listOf(
        navArgument(LENDER_ID) { type = NavType.StringType }
    )
}
