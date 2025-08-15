package com.abhishek.smartexpensetracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.ui.viewmodel.DateFilter
import java.text.SimpleDateFormat
import java.util.*

enum class GroupMode { TIME, CATEGORY }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    expenses: List<Expense>,
    totalCount: Int,
    totalAmount: Double,
    isLoading: Boolean,
    dateFilter: DateFilter,
    onFilterChange: (DateFilter) -> Unit,
    onAdd: () -> Unit,
    onReport: () -> Unit,
    onDeleteExpense: (Expense) -> Unit
){
    var groupMode by remember { mutableStateOf(GroupMode.TIME) }
    var filterExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Expenses ($totalCount) • ₹${"%.2f".format(totalAmount)}")
                },
                actions = {
                    Box {
                        IconButton(onClick = { filterExpanded = true }) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = "Filter")
                        }
                        DropdownMenu(
                            expanded = filterExpanded,
                            onDismissRequest = { filterExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Today") },
                                onClick = { onFilterChange(DateFilter.TODAY); filterExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Yesterday") },
                                onClick = { onFilterChange(DateFilter.YESTERDAY); filterExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Last 7 Days") },
                                onClick = { onFilterChange(DateFilter.LAST_7_DAYS); filterExpanded = false }
                            )
                        }
                    }
                    IconButton(onClick = { groupMode = if (groupMode == GroupMode.TIME) GroupMode.CATEGORY else GroupMode.TIME }) {
                        Icon(
                            imageVector = if (groupMode == GroupMode.TIME) Icons.Default.Category else Icons.Default.AccessTime,
                            contentDescription = "Toggle group"
                        )
                    }
                    IconButton(onClick = onReport) {
                        Icon(Icons.Default.Assessment, contentDescription = "Report")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingAddButton(onClick = onAdd)
        }
    ) { padding ->
        ExpenseList(
            expenses = expenses,
            groupMode = groupMode,
            onDeleteExpense = onDeleteExpense,
            padding = padding,
            isLoading = isLoading
        )
    }
}


@Composable
fun FloatingAddButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Default.Add, contentDescription = "Add")
    }
}

@Composable
fun ExpenseList(
    expenses: List<Expense>,
    groupMode: GroupMode,
    onDeleteExpense: (Expense) -> Unit,
    padding: PaddingValues,
    isLoading: Boolean
) {
    if (expenses.isEmpty() && !isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("No expenses for selected filter.", style = MaterialTheme.typography.bodyMedium)
        }
    } else if (expenses.isEmpty() || isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        when (groupMode) {
            GroupMode.TIME -> {
                LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
                    items(expenses) { ExpenseCard(expense = it, onDelete = onDeleteExpense) }
                }
            }
            GroupMode.CATEGORY -> {
                val grouped = expenses.groupBy { it.category }
                LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
                    grouped.forEach { (cat, list) ->
                        item { Text(cat, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(12.dp)) }
                        items(list) { ExpenseCard(expense = it, onDelete = onDeleteExpense) }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseCard(expense: Expense, onDelete: (Expense) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(expense.title, style = MaterialTheme.typography.titleSmall)
                Text(
                    "${expense.category} • ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(expense.timestamp))}",
                    style = MaterialTheme.typography.bodySmall
                )
                expense.notes?.takeIf { it.isNotEmpty() }?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("₹${"%.2f".format(expense.amount)}", style = MaterialTheme.typography.titleSmall)
                Text(
                    "Delete",
                    modifier = Modifier.padding(top = 8.dp).clickable { onDelete(expense) },
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListScreenReusable() {
    val demo = listOf(
        Expense(title = "Tea", amount = 10.0, category = "Food", notes = "", receiptUri = null),
        Expense(title = "Bus", amount = 20.0, category = "Transport", notes = "", receiptUri = null)
    )
    ListScreen(
        expenses = demo,
        totalCount = demo.size,
        totalAmount = demo.sumOf { it.amount },
        isLoading = false,
        onFilterChange = {
        },
        onAdd = {},
        onReport = {},
        onDeleteExpense = { },
        dateFilter = DateFilter.TODAY
    )
}
