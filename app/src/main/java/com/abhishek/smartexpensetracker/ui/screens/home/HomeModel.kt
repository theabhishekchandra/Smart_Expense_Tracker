package com.abhishek.smartexpensetracker.ui.screens.home




data class HomeUiState(
    val userName: String = "Abhishek",
    val currency: String = "₹",
    val todayTotal: Double = 0.0,
    val monthExpense: Double = 0.0,
    val monthIncome: Double = 0.0,
    val categoryBreakdown: List<CategorySlice> = emptyList(),
    val weeklyTrend: List<DailyPoint> = emptyList(),
    val budgets: List<BudgetProgress> = emptyList(),
    val aiTips: List<AiTip> = emptyList(),
    val improvements: List<ImprovementIdea> = emptyList(),
    val pendingApprovals: List<ApprovalItem> = emptyList(),
    val recent: List<ExpenseItem> = emptyList()
)


data class CategorySlice(val name: String, val amount: Double)


data class DailyPoint(val day: String, val amount: Double) // e.g., [Mon..Sun]


data class BudgetProgress(
    val category: String,
    val spent: Double,
    val limit: Double
)


data class AiTip(val title: String, val detail: String)


data class ImprovementIdea(val title: String, val actionLabel: String)


data class ApprovalItem(
    val id: String,
    val staffName: String,
    val title: String,
    val amount: Double,
    val hasReceipt: Boolean
)


data class ExpenseItem(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val time: String
)


// ---------- ACTIONS ----------


sealed interface HomeAction {
    data object AddExpense : HomeAction
    data object AddIncome : HomeAction
    data object ScanReceipt : HomeAction
    data object OpenReports : HomeAction
    data class Approve(val id: String) : HomeAction
    data class Reject(val id: String) : HomeAction
    data class OpenExpense(val id: String) : HomeAction
    data class ApplyImprovement(val title: String) : HomeAction
}