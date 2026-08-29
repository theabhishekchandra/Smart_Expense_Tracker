package com.abhishek.spendly.ui.screens.staff

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.ui.components.AnimatedAmountText
import com.abhishek.spendly.ui.components.GlassStatTile
import com.abhishek.spendly.ui.components.GradientCard
import com.abhishek.spendly.ui.theme.AppShapes
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.DangerColor
import com.abhishek.spendly.ui.theme.DangerColorDark
import com.abhishek.spendly.ui.theme.SuccessColor
import com.abhishek.spendly.ui.theme.SuccessColorDark
import com.abhishek.spendly.ui.theme.WarningColor
import com.abhishek.spendly.ui.theme.WarningColorDark

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

/** Semantic color for a free-form staff expense status string ("Pending"/"Approved"/"Rejected"). */
@Composable
private fun staffExpenseStatusColor(status: String): Color {
    val dark = isSystemInDarkTheme()
    return when (status.lowercase()) {
        "approved" -> if (dark) SuccessColorDark else SuccessColor
        "rejected" -> if (dark) DangerColorDark else DangerColor
        else -> if (dark) WarningColorDark else WarningColor
    }
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
                .padding(AppSpacing.md)
        ) {
            // Hero: total staff spend submitted this period, in the same
            // GradientCard + AnimatedAmountText style as Home's "Today's spend".
            val totalStaffSpend = staffExpenseLists.sumOf { it.amount }
            val totalAllocated = allocatedExpenses.sumOf { it.allocatedAmount }
            val totalRemaining = allocatedExpenses.sumOf { it.remaining }
            GradientCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Total staff spend",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(Modifier.height(AppSpacing.xs))
                AnimatedAmountText(
                    amount = totalStaffSpend,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    decimals = 0
                )
                Spacer(Modifier.height(AppSpacing.md))
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                    GlassStatTile(
                        label = "Allocated",
                        value = "₹${totalAllocated.toInt()}",
                        modifier = Modifier.weight(1f)
                    )
                    GlassStatTile(
                        label = "Remaining",
                        value = "₹${totalRemaining.toInt()}",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(Modifier.height(AppSpacing.lg))

            Text("Add Expense", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(AppSpacing.sm))

            OutlinedTextField(
                value = newTitle,
                onValueChange = { newTitle = it },
                label = { Text("Expense Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(AppSpacing.sm))

            OutlinedTextField(
                value = newAmount,
                onValueChange = { newAmount = it },
                label = { Text("Amount (₹)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(AppSpacing.sm))

            // Category Dropdown
            var expandedCat by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expandedCat, onExpandedChange = { expandedCat = !expandedCat }) {
                OutlinedTextField(
                    value = newCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
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

            Spacer(Modifier.height(AppSpacing.sm))

            // Allocate dropdown (if staff wants to log against Admin allocation)
            var expandedAlloc by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expandedAlloc, onExpandedChange = { expandedAlloc = !expandedAlloc }) {
                OutlinedTextField(
                    value = allocatedExpenses.firstOrNull { it.id == selectedAllocatedId }?.title ?: "None",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Use Allocated Expense (optional)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAlloc) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
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

            Spacer(Modifier.height(AppSpacing.sm))

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
                shape = AppShapes.small,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Submit Expense")
            }

            Spacer(Modifier.height(AppSpacing.lg))
            HorizontalDivider()
            Spacer(Modifier.height(AppSpacing.sm))

            // ================= Allocated Expenses Section =================
            Text("Allocated Budgets", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(AppSpacing.sm))

            if (allocatedExpenses.isEmpty()) {
                Text("No allocated budgets.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                allocatedExpenses.forEach { alloc ->
                    Card(
                        shape = AppShapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AppSpacing.xs),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(AppSpacing.sm)) {
                            Text(alloc.title, style = MaterialTheme.typography.titleMedium)
                            Text("Category: ${alloc.category}", style = MaterialTheme.typography.bodyMedium)
                            Text("Allocated: ₹${alloc.allocatedAmount}", style = MaterialTheme.typography.bodyMedium)
                            Text("Used: ₹${alloc.usedAmount}", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "Remaining: ₹${alloc.remaining}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(AppSpacing.lg))
            HorizontalDivider()
            Spacer(Modifier.height(AppSpacing.sm))

            // ================= Pending Approvals Section =================
            Text("Pending Approvals", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(AppSpacing.sm))

            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                val pendingList = staffExpenseLists.filter { it.status == "Pending" }
                if (pendingList.isEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppSpacing.sm),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Inbox,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(AppSpacing.xs))
                            Text(
                                "No pending approvals.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(pendingList) { expense ->
                        Card(
                            shape = AppShapes.medium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = AppSpacing.xs),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(AppSpacing.sm)) {
                                Text(expense.title, style = MaterialTheme.typography.titleMedium)
                                Text("Category: ${expense.category}", style = MaterialTheme.typography.bodyMedium)
                                Text("Amount: ₹${expense.amount}", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "Status: ${expense.status}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = staffExpenseStatusColor(expense.status)
                                )
                                if (expense.allocatedId != null) {
                                    Text(
                                        "Against: ${allocatedExpenses.firstOrNull { it.id == expense.allocatedId }?.title}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
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
