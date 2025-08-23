package com.abhishek.smartexpensetracker.ui.screens.staff

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Data classes
data class StaffExpense(
    val id: Int,
    val title: String,
    val amount: Double,
    val category: String,
    val notes: String = "",
    val date: String = "LocalDate.now()",
    var status: String = "Pending" // Pending / Approved / Rejected
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffDashboardScreen() {
    var staffExpenseLists by remember {
        mutableStateOf(
            listOf(
                StaffExpense(1, "Travel to client", 1200.0, "Travel"),
                StaffExpense(2, "Lunch meeting", 500.0, "Food")
            )
        )
    }

    var newTitle by remember { mutableStateOf("") }
    var newAmount by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("Food") }

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
            // ➕ Add Expense Section
            Text("Add Expense", style = MaterialTheme.typography.titleMedium)
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
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = newCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Food", "Travel", "Utility", "Staff").forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                newCategory = category
                                expanded = false
                            }
                        )
                    }
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
                            status = "Pending"
                        )
                        newTitle = ""
                        newAmount = ""
                        newCategory = "Food"
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Expense")
            }

            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(16.dp))

            // 🕒 Pending Approvals Section
            Text("Pending Approvals", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
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