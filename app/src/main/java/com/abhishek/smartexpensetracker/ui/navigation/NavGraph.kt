package com.abhishek.smartexpensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abhishek.smartexpensetracker.data.repository.ExpenseRepository
import com.abhishek.smartexpensetracker.ui.navigation.NavComp.ENTRY
import com.abhishek.smartexpensetracker.ui.navigation.NavComp.LIST
import com.abhishek.smartexpensetracker.ui.navigation.NavComp.REPORT
import com.abhishek.smartexpensetracker.ui.screens.EntryScreen
import com.abhishek.smartexpensetracker.ui.screens.ListScreen
import com.abhishek.smartexpensetracker.ui.screens.ReportScreen
import com.abhishek.smartexpensetracker.ui.viewmodel.ExpenseViewModel
import com.abhishek.smartexpensetracker.ui.viewmodel.ExpenseViewModelFactory

@Composable
fun NavGraph(repository: ExpenseRepository) {
    val navController = rememberNavController()
    val factory = ExpenseViewModelFactory(repository)
    val viewModel: ExpenseViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = LIST) {
        composable(LIST) {
            ListScreen(
                expenses = uiState.expenses,
                totalCount = uiState.expenses.size,
                totalAmount = uiState.totalForDay,
                isLoading = uiState.isLoading,
                dateFilter = uiState.dateFilter,
                onFilterChange = { filter -> viewModel.setDateFilter(filter) },
                onAdd = { navController.navigate(ENTRY) },
                onReport = { navController.navigate(REPORT) },
                onDeleteExpense = { viewModel.deleteExpense(it) }
            )
        }
        composable(ENTRY) {
            EntryScreen(
                topTotal = uiState.totalForDay,
                existingExpenses = uiState.expenses,
                onAdd = { title, amount, cat, notes, path ->
                    viewModel.addExpense(title, amount, cat, notes, path)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(REPORT) {
            ReportScreen(
                expenses = uiState.expenses,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

