package com.abhishek.smartexpensetracker.ui.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProcessedExpensesScreen() {
    var selectedStaff by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var filteredExpenses by remember { mutableStateOf(samplePendingExpenses.filter { it.status != Status.Pending }) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Processed Expenses",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- Filter Row ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DropdownFilter("Staff", listOf("All") + samplePendingExpenses.map { it.staffName }.distinct(), selectedStaff) {
                selectedStaff = it
                filteredExpenses = samplePendingExpenses.filter { exp ->
                    exp.status != Status.Pending && (selectedStaff == "All" || exp.staffName == selectedStaff)
                            && (selectedCategory == null || selectedCategory == "All" || exp.category == selectedCategory)
                }
            }

            DropdownFilter("Category", listOf("All") + samplePendingExpenses.map { it.category }.distinct(), selectedCategory) {
                selectedCategory = it
                filteredExpenses = samplePendingExpenses.filter { exp ->
                    exp.status != Status.Pending && (selectedStaff == null || selectedStaff == "All" || exp.staffName == selectedStaff)
                            && (selectedCategory == "All" || exp.category == selectedCategory)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(filteredExpenses) { expense ->
                ProcessedExpenseCard(expense)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DropdownFilter(label: String, options: List<String>, selected: String?, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(text = "${label}: ${selected ?: "All"}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onSelected(option)
                    expanded = false
                }) {
                    Text(option)
                }
            }
        }
    }
}

@Composable
fun ProcessedExpenseCard(expense: StaffExpensePending) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(expense.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Amount: ₹${expense.amount}", style = MaterialTheme.typography.bodyMedium)
                    Text("Staff: ${expense.staffName}", style = MaterialTheme.typography.bodyMedium)
                    Text("Category: ${expense.category}", style = MaterialTheme.typography.bodyMedium)
                    Text("Date: ${expense.date}", style = MaterialTheme.typography.bodyMedium)
                }

                // Status badge
                Box(
                    modifier = Modifier
                        .background(
                            color = when (expense.status) {
                                Status.Approved -> Color(0xFF81C784)
                                Status.Rejected -> Color(0xFFE57373)
                                else -> Color.Gray
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = expense.status.name,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            // Show rejection note if rejected
            if (expense.status == Status.Rejected && !expense.rejectionNotes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rejection Note: ${expense.rejectionNotes}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD32F2F)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProcessedExpensesScreen() {
    ProcessedExpensesScreen()
}
