package com.abhishek.smartexpensetracker.core.navigation

sealed class ScreenRoutes(val route: String) {

    // Graphs
    object Auth : ScreenRoutes(RoutesConst.AUTH_GRAPH)
    object Main : ScreenRoutes(RoutesConst.MAIN_GRAPH)
    object SplashGraph : ScreenRoutes(RoutesConst.SPLASH_GRAPH)
    object StaffGraph : ScreenRoutes(RoutesConst.STAFF_GRAPH)
    object ReportGraph : ScreenRoutes(RoutesConst.REPORT_GRAPH)

    // Splash & Onboarding
    object Splash : ScreenRoutes(RoutesConst.SPLASH_SCREEN)
    object OnBoarding : ScreenRoutes(RoutesConst.ONBOARDING_SCREEN)

    // Auth Screens
    object Login : ScreenRoutes(RoutesConst.LOGIN_SCREEN)
    object SignUp : ScreenRoutes(RoutesConst.SIGNUP_SCREEN)
    object ForgotPassword : ScreenRoutes(RoutesConst.FORGOT_PASSWORD_SCREEN)
    object ResetPassword : ScreenRoutes(RoutesConst.RESET_PASSWORD_SCREEN)
    object VerifyEmail : ScreenRoutes(RoutesConst.VERIFY_EMAIL_SCREEN)
    object VerifyOTP : ScreenRoutes(RoutesConst.VERIFY_OTP_SCREEN)
    object ChangePassword : ScreenRoutes(RoutesConst.CHANGE_PASSWORD_SCREEN)
    object SetNewPassword : ScreenRoutes(RoutesConst.SET_NEW_PASSWORD_SCREEN)

    // Main
    object Home : ScreenRoutes(RoutesConst.HOME_SCREEN)
    object Dashboard : ScreenRoutes(RoutesConst.DASHBOARD_SCREEN)
    object Notification : ScreenRoutes(RoutesConst.NOTIFICATION_SCREEN)
    object Voice : ScreenRoutes(RoutesConst.VOICE_SCREEN)

    // Expense & Income
    object AddExpense : ScreenRoutes(RoutesConst.ADD_EXPENSE_SCREEN)
    object AddIncome : ScreenRoutes(RoutesConst.ADD_INCOME_SCREEN)
    object ExpenseList : ScreenRoutes(RoutesConst.EXPENSE_LIST_SCREEN)
    object ExpenseDetail : ScreenRoutes("${RoutesConst.EXPENSE_DETAIL_SCREEN}/{${RoutesConst.EXPENSE_ID}}") {
        fun passExpenseId(expenseId: String) = "${RoutesConst.EXPENSE_DETAIL_SCREEN}/$expenseId"
    }

    // Budget & Reports
    object BudgetSetup : ScreenRoutes(RoutesConst.BUDGET_SETUP_SCREEN)
    object BudgetTracking : ScreenRoutes(RoutesConst.BUDGET_TRACKING_SCREEN)
    object Reports : ScreenRoutes(RoutesConst.REPORTS_SCREEN)
    object ExportReport : ScreenRoutes(RoutesConst.EXPORT_REPORT_SCREEN)

    // AI Features
    object AIInsights : ScreenRoutes(RoutesConst.AI_INSIGHTS_SCREEN)
    object ReceiptScanner : ScreenRoutes(RoutesConst.RECEIPT_SCANNER_SCREEN)
    object AIChat : ScreenRoutes(RoutesConst.AI_CHAT_SCREEN)

    // Staff Management
    object StaffManagement : ScreenRoutes(RoutesConst.STAFF_MANAGEMENT_SCREEN)
    object StaffDashboard : ScreenRoutes(RoutesConst.STAFF_DASHBOARD_SCREEN)
    object Approval : ScreenRoutes(RoutesConst.APPROVAL_SCREEN)
    object StaffReports : ScreenRoutes(RoutesConst.STAFF_REPORTS_SCREEN)

    // Profile & Settings
    object Profile : ScreenRoutes(RoutesConst.PROFILE_SCREEN)
    object Settings : ScreenRoutes(RoutesConst.SETTINGS_SCREEN)
    object Backup : ScreenRoutes(RoutesConst.BACKUP_SCREEN)
    object Subscription : ScreenRoutes(RoutesConst.SUBSCRIPTION_SCREEN)

    // Support
    object Help : ScreenRoutes(RoutesConst.HELP_SCREEN)
    object FAQ : ScreenRoutes(RoutesConst.FAQ_SCREEN)
    object ContactUs : ScreenRoutes(RoutesConst.CONTACT_US_SCREEN)
    object AboutUs : ScreenRoutes(RoutesConst.ABOUT_US_SCREEN)
    object TermsAndConditions : ScreenRoutes(RoutesConst.TERMS_AND_CONDITIONS_SCREEN)
    object PrivacyPolicy : ScreenRoutes(RoutesConst.PRIVACY_POLICY_SCREEN)
    object Feedback : ScreenRoutes(RoutesConst.FEEDBACK_SCREEN)
}
