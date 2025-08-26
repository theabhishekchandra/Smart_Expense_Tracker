package com.abhishek.smartexpensetracker.ui.screens.staff

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ====================== Data Classes ======================
data class StaffExpense(
    val id: Int,
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String = "",
    val date: String = "2025-08-25",
    var status: String = "Pending", // Pending / Approved / Rejected
    val allocatedId: Int? = null // link to allocated expense if used
)

data class AllocatedExpense(
    val id: Int,
    val title: String,
    val category: String,
    val allocatedAmount: Double,
    var usedAmount: Double = 0.0
) {
    val remaining: Double
        get() = allocatedAmount - usedAmount
}

// ====================== Composable Screen ======================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffDashboardScreen() {
    // Mock Allocated by Admin
    var allocatedExpenses by remember {
        mutableStateOf(
            listOf(
                AllocatedExpense(1, "Travel Allowance", "Travel", 10000.0, usedAmount = 1500.0),
                AllocatedExpense(2, "Office Supplies", "Utility", 5000.0, usedAmount = 800.0)
            )
        )
    }

    // Staff-submitted Expenses
    var staffExpenseLists by remember {
        mutableStateOf(
            listOf(
                StaffExpense(1, "Taxi fare", 800.0, "Travel", allocatedId = 1),
                StaffExpense(2, "Lunch with client", 700.0, "Food", allocatedId = null)
            )
        )
    }

    // Form state
    var newTitle by remember { mutableStateOf("") }
    var newAmount by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("Food") }
    var selectedAllocatedId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Staff Dashboard") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("➕ Add Expense", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                label = { Text("Expense Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = newAmount,
                onValueChange = { newAmount = it },
                label = { Text("Amount (₹)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            // Category Dropdown
            var expandedCat by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expandedCat, onExpandedChange = { expandedCat = !expandedCat }) {
                OutlinedTextField(
                    value = newCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandedCat, onDismissRequest = { expandedCat = false }) {
                    listOf("Food", "Travel", "Utility", "Staff", "Other").forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                newCategory = category
                                expandedCat = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Allocate dropdown (if staff wants to log against Admin allocation)
            var expandedAlloc by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expandedAlloc, onExpandedChange = { expandedAlloc = !expandedAlloc }) {
                OutlinedTextField(
                    value = allocatedExpenses.firstOrNull { it.id == selectedAllocatedId }?.title ?: "None",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Use Allocated Expense (optional)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAlloc) },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandedAlloc, onDismissRequest = { expandedAlloc = false }) {
                    allocatedExpenses.forEach { alloc ->
                        DropdownMenuItem(
                            text = { Text("${alloc.title} (Remaining ₹${alloc.remaining})") },
                            onClick = {
                                selectedAllocatedId = alloc.id
                                expandedAlloc = false
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text("None (Personal Expense)") },
                        onClick = {
                            selectedAllocatedId = null
                            expandedAlloc = false
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    val amountDouble = newAmount.toDoubleOrNull() ?: 0.0
                    if (newTitle.isNotBlank() && amountDouble > 0) {
                        val newId = (staffExpenseLists.maxOfOrNull { it.id } ?: 0) + 1
                        staffExpenseLists = staffExpenseLists + StaffExpense(
                            id = newId,
                            title = newTitle,
                            amount = amountDouble,
                            category = newCategory,
                            allocatedId = selectedAllocatedId,
                            status = "Pending"
                        )
                        // update used amount in allocated expense
                        allocatedExpenses = allocatedExpenses.map { alloc ->
                            if (alloc.id == selectedAllocatedId) alloc.copy(usedAmount = alloc.usedAmount + amountDouble)
                            else alloc
                        }
                        // Reset
                        newTitle = ""
                        newAmount = ""
                        newCategory = "Food"
                        selectedAllocatedId = null
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Submit Expense")
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            Spacer(Modifier.height(12.dp))

            // ================= Allocated Expenses Section =================
            Text("💰 Allocated Budgets", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (allocatedExpenses.isEmpty()) {
                Text("No allocated budgets.", style = MaterialTheme.typography.bodySmall)
            } else {
                allocatedExpenses.forEach { alloc ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(alloc.title, style = MaterialTheme.typography.titleMedium)
                            Text("Category: ${alloc.category}", style = MaterialTheme.typography.bodyMedium)
                            Text("Allocated: ₹${alloc.allocatedAmount}", style = MaterialTheme.typography.bodyMedium)
                            Text("Used: ₹${alloc.usedAmount}", style = MaterialTheme.typography.bodyMedium)
                            Text("Remaining: ₹${alloc.remaining}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            Spacer(Modifier.height(12.dp))

            // ================= Pending Approvals Section =================
            Text("🕒 Pending Approvals", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                val pendingList = staffExpenseLists.filter { it.status == "Pending" }
                if (pendingList.isEmpty()) {
                    item {
                        Text("No pending approvals.", modifier = Modifier.padding(8.dp))
                    }
                } else {
                    items(pendingList) { expense ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(expense.title, style = MaterialTheme.typography.titleMedium)
                                Text("Category: ${expense.category}", style = MaterialTheme.typography.bodyMedium)
                                Text("Amount: ₹${expense.amount}", style = MaterialTheme.typography.bodyMedium)
                                Text("Status: ${expense.status}", style = MaterialTheme.typography.bodySmall)
                                if (expense.allocatedId != null) {
                                    Text("Against: ${allocatedExpenses.firstOrNull { it.id == expense.allocatedId }?.title}", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewStaffDashboardScreen() {
    StaffDashboardScreen()
}