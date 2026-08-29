package com.abhishek.spendly.ui.screens.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhishek.spendly.core.datastore.AppPreferencesRepository
import com.abhishek.spendly.data.model.DateFilter
import com.abhishek.spendly.data.model.ExpenseDM
import com.abhishek.spendly.data.model.ExpenseUiState
import com.abhishek.spendly.data.model.GroupMode
import com.abhishek.spendly.data.local.room.entity.LendingTransactionEntity
import com.abhishek.spendly.data.model.DEFAULT_LOCAL_USER_ID
import com.abhishek.spendly.data.model.toDomain
import com.abhishek.spendly.data.model.toEntity
import com.abhishek.spendly.data.repository.IExpenseRepository
import com.abhishek.spendly.data.repository.local.ICategoryRepository
import com.abhishek.spendly.data.repository.local.IContactRepository
import com.abhishek.spendly.data.repository.local.ILendingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repo: IExpenseRepository,
    private val categoryRepo: ICategoryRepository,
    private val contactRepo: IContactRepository,
    private val lendingRepo: ILendingRepository,
    preferencesRepository: AppPreferencesRepository
) : ViewModel() {

    val isDark = preferencesRepository.themeMode

    private val _uiState = MutableStateFlow(ExpenseUiState())
    val uiState: StateFlow<ExpenseUiState> = _uiState.asStateFlow()

    private var currentFilter: DateFilter = DateFilter.ALL
    private var categoryNameById: Map<Long, String> = emptyMap()
    private var categoryIdByName: Map<String, Long> = emptyMap()

    init {
        viewModelScope.launch {
            refreshCategoryCache()
            loadExpenses(currentFilter)
        }
    }

    private suspend fun refreshCategoryCache() {
        val categories = categoryRepo.getAllCategories()
        categoryNameById = categories.associate { it.categoryId to it.name }
        categoryIdByName = categories.associate { it.name.lowercase() to it.categoryId }
    }

    /** Load expenses by filter */
    fun loadExpenses(filter: DateFilter) {
        currentFilter = filter
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            repo.getExpensesByFilter(filter)
                .catch { e ->
                    _uiState.update { it.copy(loading = false, error = e.message) }
                }
                .collect { entities ->
                    val domain = entities.map { it.toDomain(categoryNameById) }
                    _uiState.update {
                        it.copy(expenses = applySearch(domain, it.searchQuery), loading = false)
                    }
                }
        }
    }

    /** Apply search filter */
    fun updateSearch(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            repo.getExpensesByFilter(currentFilter).firstOrNull()?.let { list ->
                val domain = list.map { it.toDomain(categoryNameById) }
                _uiState.update { it.copy(expenses = applySearch(domain, query)) }
            }
        }
    }

    private fun applySearch(list: List<ExpenseDM>, query: String): List<ExpenseDM> {
        if (query.isBlank()) return list
        return list.filter { it.title.contains(query, ignoreCase = true) }
    }

    /** Toggle grouping mode */
    fun toggleGroupMode() {
        val newMode =
            if (_uiState.value.groupMode == GroupMode.TIME) GroupMode.CATEGORY else GroupMode.TIME
        _uiState.update { it.copy(groupMode = newMode) }
    }

    /** Resolve (or create) the category row backing a free-text category name */
    private suspend fun resolveCategoryId(name: String): Long? {
        if (name.isBlank()) return null
        categoryIdByName[name.lowercase()]?.let { return it }
        val id = categoryRepo.getOrCreateCategoryId(name)
        refreshCategoryCache()
        return id
    }

    /** Add new expense */
    fun addExpense(expense: ExpenseDM, onSaved: () -> Unit = {}) = viewModelScope.launch {
        try {
            val categoryId = resolveCategoryId(expense.category)
            repo.insert(expense.toEntity(categoryId))
            loadExpenses(currentFilter)
            onSaved()
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }

    /** Edit expense */
    fun editExpense(expense: ExpenseDM) = viewModelScope.launch {
        try {
            val categoryId = resolveCategoryId(expense.category)
            repo.update(expense.toEntity(categoryId))
            loadExpenses(currentFilter)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }

    /** Delete expense */
    fun deleteExpense(expense: ExpenseDM) = viewModelScope.launch {
        try {
            val categoryId = resolveCategoryId(expense.category)
            repo.delete(expense.toEntity(categoryId))
            loadExpenses(currentFilter)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }

    /** Approve a pending expense */
    fun approveExpense(expense: ExpenseDM) = viewModelScope.launch {
        try {
            repo.updateApprovalStatus(expense.id, "Approved", null)
            loadExpenses(currentFilter)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }

    /** Reject a pending expense */
    fun rejectExpense(expense: ExpenseDM) = viewModelScope.launch {
        try {
            repo.updateApprovalStatus(expense.id, "Rejected", null)
            loadExpenses(currentFilter)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }

    fun findExpenseById(id: Long): ExpenseDM? = _uiState.value.expenses.find { it.id == id }

    /** Records a lender/borrower transaction from the Add Expense screen's toggled flow */
    fun addLendingRecord(
        personName: String,
        phone: String,
        amount: Double,
        purpose: String,
        isGiven: Boolean,
        dueDateMillis: Long?,
        onSaved: () -> Unit = {}
    ) = viewModelScope.launch {
        try {
            val contactId = contactRepo.getOrCreateContactId(personName, phone)
            lendingRepo.addLending(
                LendingTransactionEntity(
                    userId = DEFAULT_LOCAL_USER_ID,
                    contactId = contactId,
                    amount = amount,
                    transactionType = if (isGiven) "lent" else "borrowed",
                    dueDate = dueDateMillis,
                    notes = purpose.ifBlank { null }
                )
            )
            onSaved()
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }
}
