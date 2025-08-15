package com.abhishek.smartexpensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.data.repository.IExpenseRepository
import com.abhishek.smartexpensetracker.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class DateFilter {
    TODAY, YESTERDAY, LAST_7_DAYS, ALL
}

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val totalForDay: Double = 0.0,
    val selectedDayStart: Long = DateUtils.startOfDayMillis(System.currentTimeMillis()),
    val groupByCategory: Boolean = false,
    val message: String? = null,
    val isLoading: Boolean = false,
    val dateFilter: DateFilter = DateFilter.TODAY
)

class ExpenseViewModel(private val repo: IExpenseRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    // **Single state for last 7 days**
    private val _last7DaysExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val last7DaysExpenses: StateFlow<List<Expense>> = _last7DaysExpenses.asStateFlow()

    init {
        setDateFilter(DateFilter.TODAY)
        loadLast7DaysExpenses()
    }

    private fun loadLast7DaysExpenses() {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val start7 = DateUtils.startOfDayMillis(now - 6 * 24 * 60 * 60 * 1000L)
            val endNow = DateUtils.endOfDayMillis(now)
            repo.getForDayRange(start7, endNow)
                .collect { list ->
                    _last7DaysExpenses.value = list
                }
        }
    }

    private fun observeDayRange(startMs: Long, endMs: Long) {
        _uiState.update { it.copy(isLoading = true) }
        repo.getForDayRange(startMs, endMs)
            .onEach { list ->
                val total = list.sumOf { it.amount }
                _uiState.update { it.copy(expenses = list, totalForDay = total, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    /** Set date filter (TODAY, YESTERDAY, LAST_7_DAYS, ALL) */
    fun setDateFilter(filter: DateFilter) {
        _uiState.update { it.copy(isLoading = true, dateFilter = filter) }
        val now = System.currentTimeMillis()
        val (start, end) = when (filter) {
            DateFilter.TODAY -> DateUtils.startOfDayMillis(now) to DateUtils.endOfDayMillis(now)
            DateFilter.YESTERDAY -> {
                val yesterday = now - 24*60*60*1000L
                DateUtils.startOfDayMillis(yesterday) to DateUtils.endOfDayMillis(yesterday)
            }
            DateFilter.LAST_7_DAYS -> {
                val start7 = now - 6*24*60*60*1000L // last 7 days including today
                DateUtils.startOfDayMillis(start7) to DateUtils.endOfDayMillis(now)
            }
            else -> DateUtils.startOfDayMillis(0) to DateUtils.endOfDayMillis(now)
        }

        repo.getForDayRange(start, end)
            .onEach { list ->
                val total = list.sumOf { it.amount }
                _uiState.update { it.copy(expenses = list, totalForDay = total, isLoading = false, selectedDayStart = start) }
            }
            .launchIn(viewModelScope)
    }

    /** --- New function to get last 7 days totals --- */
    fun last7DaysExpenses(): StateFlow<List<Expense>> {
        val _last7Days = MutableStateFlow<List<Expense>>(emptyList())
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val start7 = DateUtils.startOfDayMillis(now - 6 * 24 * 60 * 60 * 1000L) // 7 days including today
            val endNow = DateUtils.endOfDayMillis(now)
            repo.getForDayRange(start7, endNow)
                .collect { list ->
                    _last7Days.value = list
                }
        }
        return _last7Days
    }

    /** --- existing functions --- */
    fun toggleGrouping() {
        _uiState.update { it.copy(groupByCategory = !it.groupByCategory) }
    }

    fun addExpense(
        title: String,
        amount: Double,
        category: String,
        notes: String?,
        receiptUri: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (title.isBlank() || amount <= 0.0) {
                _uiState.update { it.copy(message = "Title required and amount > 0") }
                return@launch
            }

            val now = System.currentTimeMillis()
            val expense = Expense(
                title = title.trim(),
                amount = amount,
                category = category.ifBlank { "Other" },
                notes = notes?.take(100),
                receiptUri = receiptUri,
                timestamp = now,
                synced = false
            )

            val todays = repo.getForDayRange(
                DateUtils.startOfDayMillis(now),
                DateUtils.endOfDayMillis(now)
            ).firstOrNull() ?: emptyList()

            val duplicate = todays.any {
                it.title.equals(expense.title, ignoreCase = true) &&
                        kotlin.math.abs(it.amount - expense.amount) < 0.01 &&
                        kotlin.math.abs(it.timestamp - expense.timestamp) < 2 * 60 * 1000L
            }

            if (duplicate) {
                _uiState.update { it.copy(message = "Duplicate expense detected — not added") }
                return@launch
            }

            repo.insert(expense)
            _uiState.update { it.copy(message = "Expense added") }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.delete(expense)
            _uiState.update { it.copy(message = "Expense deleted") }
        }
    }

    suspend fun lastNDaysTotals(n: Int = 7): List<Pair<Long, Double>> {
        val results = mutableListOf<Pair<Long, Double>>()
        val now = System.currentTimeMillis()
        for (i in 0 until n) {
            val day = now - i * 24 * 60 * 60 * 1000L
            val start = DateUtils.startOfDayMillis(day)
            val end = DateUtils.endOfDayMillis(day)
            val total = repo.totalForDayRange(start, end)
            results.add(start to total)
        }
        return results.reversed()
    }

    fun allExpensesSnapshot(): List<Expense> = _uiState.value.expenses
}


class ExpenseViewModelFactory(private val repo: IExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
