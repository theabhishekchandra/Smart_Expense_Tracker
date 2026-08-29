package com.abhishek.spendly.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.spendly.core.datastore.AppPreferencesRepository
import com.abhishek.spendly.core.datastore.BusinessMode
import com.abhishek.spendly.core.datastore.Currency
import com.abhishek.spendly.data.model.ExpenseStatus
import com.abhishek.spendly.data.repository.ExpenseRepository
import com.abhishek.spendly.ui.screens.staff.Role
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.List

// Different types of Home Screen State
sealed class HomeUiState {
    object Loading : HomeUiState()
    data class PersonalDashboard(
        val userName: String,
        val totalExpense: Double,
        val monthlyExpense: Double,
        val monthlyIncome: Double,
        val categories: List<String>,
        val categoryBreakdown: List<CategorySlice> = emptyList(),
        val budgetProgressList: List<BudgetProgress> = emptyList(),
        val improvementIdeasList: List<ImprovementIdea> = emptyList(),
        val aiTipList: List<AiTip> = emptyList(),
        val recentExpenses: List<ExpenseItem> = emptyList()
    ) : HomeUiState()

    data class BusinessDashboard(
        val userName: String,
        val totalExpense: Double,
        val staffCount: Int,
        val monthlyExpense: Double,
        val monthlyIncome: Double,
        val weeklyTrend: List<DailyPoint> = emptyList(),
        val approvalRecordList: List<ApprovalRecord> = emptyList(),
        val staffSpendingList : List<StaffSpending> = emptyList(),
        val improvementIdeasList: List<ImprovementIdea> = emptyList(),
        val aiTipList: List<AiTip> = emptyList(),
        val recentExpenses: List<ExpenseItem> = emptyList()
    ) : HomeUiState()

    data class Error(val message: String) : HomeUiState()
}

val HomeUiState.userNameOrNull: String?
    get() = when (this) {
        is HomeUiState.PersonalDashboard -> this.userName
        is HomeUiState.BusinessDashboard -> this.userName
        else -> null
    }

val HomeUiState.AITips: List<AiTip>?
    get() = when (this) {
        is HomeUiState.PersonalDashboard -> this.aiTipList
        is HomeUiState.BusinessDashboard -> this.aiTipList
        else -> null
    }

val HomeUiState.ImprovementIdeas: List<ImprovementIdea>?
    get() = when (this) {
        is HomeUiState.PersonalDashboard -> this.improvementIdeasList
        is HomeUiState.BusinessDashboard -> this.improvementIdeasList
        else -> null
    }

val HomeUiState.recent: List<ExpenseItem>
    get() = when (this) {
        is HomeUiState.PersonalDashboard -> this.recentExpenses
        is HomeUiState.BusinessDashboard -> this.recentExpenses
        else -> emptyList()
    }


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val expenseRep: ExpenseRepository,
    private val appPref: AppPreferencesRepository
) : ViewModel() {
    // TODO: Delete when real data is available.
    val sample = HomeUiStateA(
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
            ApprovalRecord("1", "Rohit", "Cab from client visit", 540.0, true),
            ApprovalRecord("2", "Sneha", "Lunch with vendor", 920.0, true)
        ),
        recent = listOf(
            ExpenseItem("1", "Printer ink", "Utility", 780.0, "10:24 AM"),
            ExpenseItem("2", "Team lunch", "Food", 2450.0, "Yesterday"),
            ExpenseItem("3", "Airport taxi", "Travel", 820.0, "Yesterday")
        ),
        staffLeaderboard = listOf(
            StaffSpending(
                id = 1,
                staffId = 101,
                staffName = "Ravi Sharma",
                role = Role.Admin,
                amount = 1200.0,
                category = "Travel",
                description = "Taxi to client office",
                date = System.currentTimeMillis(),
                status = ExpenseStatus.PENDING
            ),
            StaffSpending(
                id = 2,
                staffId = 102,
                staffName = "Neha Gupta",
                role = Role.EntryOnly,
                amount = 500.0,
                category = "Food",
                description = "Lunch with client",
                date = System.currentTimeMillis() - 86400000, // 1 day ago
                status = ExpenseStatus.APPROVED,
                approverId = 201,
                approverName = "Amit Verma",
                notes = "Valid expense"
            ),
            StaffSpending(
                id = 3,
                staffId = 103,
                staffName = "Amit Verma",
                role = Role.Approver,
                amount = 3000.0,
                category = "Office Supplies",
                description = "Stationery purchase",
                date = System.currentTimeMillis() - 2 * 86400000, // 2 days ago
                status = ExpenseStatus.REJECTED,
                approverId = 201,
                approverName = "Ravi Sharma",
                notes = "Need prior approval"
            ),
            StaffSpending(
                id = 4,
                staffId = 104,
                staffName = "Priya Singh",
                role = Role.Viewer,
                amount = 1500.0,
                category = "Travel",
                description = "Flight ticket booking",
                date = System.currentTimeMillis() - 3 * 86400000, // 3 days ago
                status = ExpenseStatus.APPROVED,
                approverId = 201,
                approverName = "Amit Verma",
                notes = "Approved for client visit"
            )
        )
    )

    // Screen state
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Currency
    private val _currency = MutableStateFlow<Currency?>(null)
    val currency: StateFlow<Currency?> = _currency.asStateFlow()

    // UserName
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    private val _isBusiness = MutableStateFlow(false)
    val isBusiness: StateFlow<Boolean> = _isBusiness.asStateFlow()

    private val _borrowRecord = MutableStateFlow<List<BorrowerRecord>>(emptyList())
    val borrowRecord: StateFlow<List<BorrowerRecord>> = _borrowRecord.asStateFlow()


    init {
        loadInitialData()
        setBorrowData()
        observeUserName()
    }

    /**
     * The dashboard's user name is otherwise loaded once in [loadInitialData]. This keeps it
     * live so an edit made on the Profile screen (which writes to the same preference) is
     * reflected here too, without a full app restart.
     */
    private fun observeUserName() {
        viewModelScope.launch {
            appPref.userNameFlow.collect { newName ->
                _userName.value = newName
                _uiState.update { state ->
                    when (state) {
                        is HomeUiState.PersonalDashboard -> state.copy(userName = newName)
                        is HomeUiState.BusinessDashboard -> state.copy(userName = newName)
                        else -> state
                    }
                }
            }
        }
    }

    fun setBorrowData(){
        val dummyBorrowers = listOf(
            BorrowerRecord(
                id = 1,
                borrowerName = "Ravi Sharma",
                contactNumber = "9876543210",
                email = "ravi.sharma@example.com",
                address = "Delhi, India",
                borrowedAmount = 5000.0,
                amountPaid = 2000.0,
                dueDate = "2025-09-10",
                interestRate = 5.0f,
                notes = "Personal loan for bike purchase",
                reminderType = "SMS",
                reminderFrequency = "Weekly"
            ),
            BorrowerRecord(
                id = 2,
                borrowerName = "Anjali Gupta",
                contactNumber = "9123456780",
                borrowedAmount = 15000.0,
                amountPaid = 15000.0,
                dueDate = "2025-08-20",
                notes = "Wedding expenses",
                reminderType = "Notification",
                reminderFrequency = "Before Due Date",
                isSettled = true
            ),
            BorrowerRecord(
                id = 3,
                borrowerName = "Mohit Verma",
                contactNumber = "9898989898",
                email = "mohitv@gmail.com",
                borrowedAmount = 8000.0,
                amountPaid = 1000.0,
                dueDate = "2025-09-30",
                interestRate = 10.0f,
                notes = "Business investment",
                reminderType = "WhatsApp",
                reminderFrequency = "Daily"
            ),
            BorrowerRecord(
                id = 4,
                borrowerName = "Priya Singh",
                borrowedAmount = 12000.0,
                amountPaid = 0.0,
                dueDate = "2025-10-15",
                notes = "Friend loan",
                reminderEnabled = false
            ),
            BorrowerRecord(
                id = 5,
                borrowerName = "Amit Kumar",
                contactNumber = "7001234567",
                borrowedAmount = 2000.0,
                amountPaid = 500.0,
                dueDate = "2025-08-28",
                interestRate = 2.5f,
                notes = "Medical emergency",
                reminderType = "Notification",
                reminderFrequency = "Daily"
            )
        )
        _borrowRecord.value = dummyBorrowers

    }
    fun loadInitialData() {
        viewModelScope.launch {
            try {
                // 1. Load user details
                val name = appPref.getUserNameOnce()
                _userName.value = name

                val currencyPref = appPref.getCurrencyOnce()
                _currency.value = currencyPref

                val isBusiness = appPref.businessMode.first()
                _isBusiness.value = isBusiness is BusinessMode.Business

                val isPremium = appPref.getIsPremiumOnce()
                _isPremium.value = isPremium

                // 2. Apply logic based on mode
                if (isBusiness is BusinessMode.Business) {
                    // TODO: Replace with real repository calls later
                    _uiState.value = HomeUiState.BusinessDashboard(
                        userName = name,
                        totalExpense = 45000.0,
                        staffCount = if (isPremium) 10 else 3, // premium unlocks more staff
                        approvalRecordList = sample.pendingApprovals,
                        monthlyExpense = 20000.0,
                        monthlyIncome = 25000.0,
                        weeklyTrend = sample.weeklyTrend,
                        staffSpendingList = sample.staffLeaderboard,
                        aiTipList = sample.aiTips,
                        improvementIdeasList = sample.improvements,
                        recentExpenses = sample.recent
                    )
                } else {
                    _uiState.value = HomeUiState.PersonalDashboard(
                        userName = name,
                        totalExpense = 12000.0,
                        monthlyExpense = 3500.0,
                        monthlyIncome = 25000.0,
                        categories = listOf("Food", "Travel", "Bills"),
                        categoryBreakdown = sample.categoryBreakdown,
                        budgetProgressList = sample.budgets,
                        aiTipList = sample.aiTips,
                        recentExpenses = sample.recent,
                        improvementIdeasList = sample.improvements,
                    )
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Failed to load data: ${e.message}")
            }
        }
    }

    // Example: Refresh data on pull-to-refresh
    fun refreshHome() {
        loadInitialData()
    }

    fun approveApproval(id: String) {
        _uiState.update { state ->
            if (state is HomeUiState.BusinessDashboard) {
                state.copy(approvalRecordList = state.approvalRecordList.filterNot { it.id == id })
            } else state
        }
    }

    fun rejectApproval(id: String) {
        _uiState.update { state ->
            if (state is HomeUiState.BusinessDashboard) {
                state.copy(approvalRecordList = state.approvalRecordList.filterNot { it.id == id })
            } else state
        }
    }
}
