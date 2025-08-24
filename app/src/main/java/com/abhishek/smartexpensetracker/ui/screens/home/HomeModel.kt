package com.abhishek.smartexpensetracker.ui.screens.home

import com.abhishek.smartexpensetracker.data.model.ExpenseStatus
import com.abhishek.smartexpensetracker.ui.screens.staff.Role


data class HomeUiStateA(
    val userName: String = "Abhishek",
    val currency: String = "₹",
    val todayTotal: Double = 0.0,
    val monthExpense: Double = 0.0,
    val monthIncome: Double = 0.0,
    val isBusiness : Boolean = false,
    val categoryBreakdown: List<CategorySlice> = emptyList(),
    val weeklyTrend: List<DailyPoint> = emptyList(),
    val budgets: List<BudgetProgress> = emptyList(),
    val aiTips: List<AiTip> = emptyList(),
    val improvements: List<ImprovementIdea> = emptyList(),
    val pendingApprovals: List<ApprovalRecord> = emptyList(),
    val recent: List<ExpenseItem> = emptyList(),
    val staffLeaderboard : List<StaffSpending> = emptyList()
)

data class StaffSpending(
    val id: Int,                      // Unique ID for the expense
    val staffId: Int,                  // User ID of staff
    val staffName: String,             // Display name of staff
    val role: Role,               // Role (Admin, Approver, Viewer, EntryOnly)
    val amount: Double,                // Expense amount
    val category: String,              // e.g., Travel, Food, Office Supplies
    val description: String,           // Short description of expense
    val date: Long,                    // Timestamp (System.currentTimeMillis())
    val status: ExpenseStatus,         // Pending, Approved, Rejected
    val approverId: Int? = null,       // Who approved/rejected
    val approverName: String? = null,  // Name of approver
    val notes: String? = null          // Optional admin/approver notes
)

data class CategorySlice(val name: String, val amount: Double)


data class DailyPoint(val day: String, val amount: Double)


data class BudgetProgress(
    val category: String,
    val spent: Double,
    val limit: Double
)


data class AiTip(val title: String, val detail: String)


data class ImprovementIdea(val title: String, val actionLabel: String)


data class ApprovalRecord(
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