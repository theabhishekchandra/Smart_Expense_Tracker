package com.abhishek.smartexpensetracker.ui.screens.login.viewmodel

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.smartexpensetracker.core.datastore.ThemeType
import com.abhishek.smartexpensetracker.core.datastore.UserPreferencesRepository
import com.abhishek.smartexpensetracker.data.model.DateFilter
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.data.model.ExpenseDM
import com.abhishek.smartexpensetracker.data.model.ExpenseUiState
import com.abhishek.smartexpensetracker.data.model.GroupMode
import com.abhishek.smartexpensetracker.data.repository.ExpenseRepository
import com.abhishek.smartexpensetracker.data.repository.IExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repo: IExpenseRepository,
    private val repository: UserPreferencesRepository
) : ViewModel() {

    val isDark = repository.themeMode
    init {
        val themeMode = repository.themeMode
            .stateIn(viewModelScope, SharingStarted.Lazily, ThemeType.LIGHT)

        val businessMode = repository.businessMode
            .stateIn(viewModelScope, SharingStarted.Lazily, false)
        Log.d("ExpenseViewModel", "themeMode: ${themeMode.value}")
        Log.d("ExpenseViewModel", "businessMode: ${businessMode.value}")
    }

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private var currentFilter: DateFilter = DateFilter.TODAY

    val demoExpenses = listOf(
        ExpenseDM(title = "Tea", amount = 10.0, category = "Food", notes = "Morning tea", receiptUri = null),
        ExpenseDM(title = "Bus", amount = 20.0, category = "Transport", notes = "Office travel", receiptUri = null),
        ExpenseDM(title = "Car", amount = 20.0, category = "Transport", notes = "Office travel", receiptUri = null),
        ExpenseDM(title = "Breakfast", amount = 20.0, category = "Food", notes = "Office travel", receiptUri = null),
        ExpenseDM(title = "Salary", amount = 20.0, category = "Staff", notes = "Office travel", receiptUri = null)
    )

    init {
        demoExpenses.forEach {
            addExpense(it)
        }
        loadExpenses(currentFilter)
    }

    /** 🔹 Load expenses by filter */
    fun loadExpenses(filter: DateFilter) {
        currentFilter = filter
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            repo.getExpensesByFilter(filter)
                .catch { e ->
                    _uiState.update { it.copy(loading = false, error = e.message) }
                }
                .collect { list ->
                    val searched = applySearch(list, _uiState.value.searchQuery)
//                    _uiState.update { it.copy(expenses = searched, loading = false) }
                }
        }
    }

    /** 🔹 Apply search filter */
    fun updateSearch(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            repo.getExpensesByFilter(currentFilter).firstOrNull()?.let { list ->
                val searched = applySearch(list, query)
//                _uiState.update { it.copy(expenses = searched) }
            }
        }
    }


    private fun applySearch(list: List<Expense>, query: String): List<Expense> {
        if (query.isBlank()) return list
        return list.filter { it.title.contains(query, ignoreCase = true) }
    }

    /** 🔹 Toggle grouping mode */
    fun toggleGroupMode() {
        val newMode =
            if (_uiState.value.groupMode == GroupMode.TIME) GroupMode.CATEGORY else GroupMode.TIME
        _uiState.update { it.copy(groupMode = newMode) }
    }

    /** 🔹 CRUD */
    /** 🔹 Add new expense */
    fun addExpense(expense: ExpenseDM) = viewModelScope.launch {
        try {
//            repo.insert(expense)
            _uiState.update { state ->
                state.copy(
                    expenses = (state.expenses + expense)
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }

    /** 🔹 Edit expense */
    fun editExpense(expense: ExpenseDM) = viewModelScope.launch {
        try {
//            repo.update(expense)
            // Reuse updateList logic
            updateList(
                ExpenseDM(
                    title = expense.title,
                    amount = expense.amount,
                    category = expense.category,
                    notes = expense.notes,
                    receiptUri = expense.receiptUri,
                    timestamp = expense.timestamp,
                    id = expense.id.toString(),
                )
            )
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }

    /** 🔹 Delete expense */
    fun deleteExpense(expense: ExpenseDM) = viewModelScope.launch {
        try {
            repo.delete(Expense(
                id = expense.id.toString().toLong(),
                title = expense.title,
                amount = expense.amount,
                category = expense.category,
                notes = expense.notes,
                receiptUri = expense.receiptUri.toString(),
                timestamp = expense.timestamp
            ))
            _uiState.update { state ->
                state.copy(
                    expenses = state.expenses.filter { it.id != expense.id }
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }

    /** 🔹 Update list item immutably */
    fun updateList(expenseDM: ExpenseDM) {
        _uiState.update { state ->
            state.copy(
                expenses = state.expenses.map { expense ->
                    if (expense.id == expenseDM.id) {
                        expense.copy(
                            title = expenseDM.title ?: expense.title,
                            amount = expenseDM.amount ?: expense.amount,
                            category = expenseDM.category ?: expense.category,
                            notes = expenseDM.notes ?: expense.notes,
                            receiptUri = expenseDM.receiptUri ?: expense.receiptUri,
                            timestamp = expenseDM.timestamp ?: expense.timestamp
                        )
                    } else expense
                }
            )
        }
    }
}