package com.abhishek.smartexpensetracker.ui.screens.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.data.model.ExpenseDM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class ReportsViewModel : ViewModel() {

    // Mutable state flow for UI
    private val _reportsState = MutableStateFlow(ReportsUiState())
    open val reportsState: StateFlow<ReportsUiState> = _reportsState.asStateFlow()

    // Mocked expense data
    private val allExpenses = mutableListOf(
        Expense(1, "Lunch", 250.0, "Food", "2025-08-23", "Staff A"),
        Expense(2, "Taxi", 500.0, "Travel", "2025-08-23", "Staff B"),
        Expense(3, "Office Supplies", 1200.0, "Utility", "2025-08-22", "Staff A"),
        Expense(4, "Client Dinner", 2000.0, "Food", "2025-08-21", "Staff B")
    )

    init {
        loadReports()
    }

    private fun loadReports() {
        viewModelScope.launch {
            // Initially show all expenses (filtered later)
            val initialState = ReportsUiState(
                selectedPeriod = ReportPeriod.WEEKLY,
                selectedStaff = null,
                expenses = allExpenses,
                aiInsights = generateAIInsights(allExpenses)
            )
            _reportsState.value = initialState
        }
    }

    // --- Functions for UI interactions ---

    fun selectPeriod(period: ReportPeriod) {
        val filteredExpenses = filterExpensesByPeriod(period, _reportsState.value.selectedStaff)
        _reportsState.value = _reportsState.value.copy(
            selectedPeriod = period,
            expenses = filteredExpenses,
            aiInsights = generateAIInsights(filteredExpenses)
        )
    }

    fun selectStaff(staff: String) {
        val filteredExpenses = filterExpensesByPeriod(_reportsState.value.selectedPeriod, staff)
        _reportsState.value = _reportsState.value.copy(
            selectedStaff = staff,
            expenses = filteredExpenses,
            aiInsights = generateAIInsights(filteredExpenses)
        )
    }

    fun onAddExpenseClick() {
        // Navigate to AddExpense screen or open dialog (handled in Compose)
    }

    fun exportReport() {
        // Trigger export PDF / CSV functionality
        // Implement actual file generation & share logic
    }

    // --- Helper Functions ---

    private fun filterExpensesByPeriod(period: ReportPeriod, staff: String?): List<Expense> {
        val filteredByStaff = if (!staff.isNullOrEmpty() && staff != "All Staff") {
            allExpenses.filter { it.title == staff }
        } else {
            allExpenses
        }

        val filteredByDate = when (period) {
            ReportPeriod.WEEKLY -> filteredByStaff.filter { it.timestamp >= System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000 }
            ReportPeriod.MONTHLY -> filteredByStaff.filter { it.timestamp >= System.currentTimeMillis() - 30 * 24 * 60 * 60 * 1000 }
            ReportPeriod.QUARTERLY -> filteredByStaff.filter { it.timestamp >= System.currentTimeMillis() - 180 * 24 * 60 * 60 * 1000 }
        }

        return filteredByDate
    }

    private fun generateAIInsights(expenses: List<Expense>): List<String> {
        // Mocked AI insights; replace with real AI/ML logic if needed
        if (expenses.isEmpty()) return listOf("No expenses found for selected period/staff.")
        val totalTravel = expenses.filter { it.category == "Travel" }.sumOf { it.amount }
        val totalFood = expenses.filter { it.category == "Food" }.sumOf { it.amount }
        val insights = mutableListOf<String>()
        if (totalTravel > 1000) insights.add("You spent ₹$totalTravel on Travel, which is higher than usual.")
        if (totalFood > 1000) insights.add("Food category is ₹$totalFood, consider controlling dining expenses.")
        return insights
    }
}
