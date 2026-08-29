package com.abhishek.spendly.ui.screens.staff

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.ui.theme.AppShapes
import com.abhishek.spendly.ui.theme.AppSpacing

@Composable
fun ProcessedExpensesScreen() {
    var selectedStaff by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var filteredExpenses by remember { mutableStateOf(samplePendingExpenses.filter { it.status != Status.Pending }) }

    Column(modifier = Modifier.fillMaxSize().padding(AppSpacing.md)) {
        Text(
            text = "Processed Expenses",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(AppSpacing.md))

        // --- Filter Row ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
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

        Spacer(modifier = Modifier.height(AppSpacing.md))

        if (filteredExpenses.isEmpty()) {
            EmptyProcessedState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                items(filteredExpenses) { expense ->
                    ProcessedExpenseCard(expense)
                }
            }
        }
    }
}

@Composable
private fun EmptyProcessedState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.FilterAltOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(
                text = "No processed expenses match this filter",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DropdownFilter(label: String, options: List<String>, selected: String?, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, shape = AppShapes.small) {
            Text(text = "$label: ${selected ?: "All"}", style = MaterialTheme.typography.labelLarge)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProcessedExpenseCard(expense: StaffExpensePending) {
    Card(
        shape = AppShapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(AppSpacing.md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(expense.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Text("₹${expense.amount} • ${expense.category}", style = MaterialTheme.typography.bodyMedium)
                    Text(expense.staffName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(expense.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                StatusBadge(status = expense.status)
            }

            // Show rejection note if rejected
            if (expense.status == Status.Rejected && !expense.rejectionNotes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(
                    text = "Rejection note: ${expense.rejectionNotes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor(Status.Rejected)
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
