package com.abhishek.spendly.ui.screens.staff

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.core.navigation.ScreenRoutes
import com.abhishek.spendly.ui.components.FinanceTopBar
import com.abhishek.spendly.ui.theme.AppShapes
import com.abhishek.spendly.ui.theme.AppSpacing

// --- Sample Data Class ---
data class StaffExpensePending(
    val id: Int,
    val title: String,
    val amount: Double,
    val staffName: String,
    val category: String,
    val receiptImage: Int? = null,
    val date: String,
    var status: Status = Status.Pending,
    var rejectionNotes: String? = null
)

// --- Sample Expense Data ---
val samplePendingExpenses = mutableStateListOf(
    StaffExpensePending(1, "Office Lunch", 500.0, "John Doe", "Food", null, "23 Aug 2025"),
    StaffExpensePending(2, "Travel to Client", 1200.0, "Jane Smith", "Travel", null, "22 Aug 2025", status = Status.Approved),
    StaffExpensePending(3, "Stationery", 300.0, "Alice Lee", "Utility", null, "21 Aug 2025", status = Status.Rejected, rejectionNotes = "Not enough ink.")
)

@Composable
fun PendingApprovalsScreen(navManager: NavManager? = null) {
    var selectedExpense by remember { mutableStateOf<StaffExpensePending?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            FinanceTopBar(
                title = "Pending Approvals",
                showBackButton = true,
                onBackClick = { navManager?.navigateBack() },
                showSearch = false,
                showFilter = false,
                showNotifications = false,
                showMenu = false
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppSpacing.md)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = { navManager?.navigate(ScreenRoutes.ProcessedExpense.route) }) {
                    Text("View Processed")
                }
            }
            if (samplePendingExpenses.isEmpty()) {
                EmptyApprovalsState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    items(samplePendingExpenses) { expense ->
                        StaffExpenseCard(expense, onApproveReject = { selectedExpense = it; showDialog = true })
                    }
                }
            }
        }
    }

    // --- Approve/Reject Dialog ---
    selectedExpense?.let { expense ->
        if (showDialog) {
            ApproveRejectDialog(
                expense = expense,
                onDismiss = { showDialog = false },
                onAction = { updatedExpense ->
                    val index = samplePendingExpenses.indexOfFirst { it.id == updatedExpense.id }
                    if (index != -1) samplePendingExpenses[index] = updatedExpense
                    showDialog = false
                }
            )
        }
    }
}

@Composable
private fun EmptyApprovalsState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.Inbox,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(
                text = "No pending approvals",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StaffExpenseCard(expense: StaffExpensePending, onApproveReject: (StaffExpensePending) -> Unit) {
    Card(
        shape = AppShapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(AppSpacing.md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Receipt thumbnail placeholder
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, AppShapes.small)
                ) {
                    expense.receiptImage?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = "Receipt for ${expense.title}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.width(AppSpacing.sm))

                Column(modifier = Modifier.weight(1f)) {
                    Text(expense.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Text("₹${expense.amount} • ${expense.category}", style = MaterialTheme.typography.bodyMedium)
                    Text(expense.staffName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(expense.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Spacer(modifier = Modifier.width(AppSpacing.xs))
                StatusBadge(status = expense.status)
            }

            // Rejection note
            if (expense.status == Status.Rejected && !expense.rejectionNotes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(
                    text = "Rejection note: ${expense.rejectionNotes}",
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor(Status.Rejected)
                )
            }

            // Buttons only for Pending
            if (expense.status == Status.Pending) {
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm, Alignment.End),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { onApproveReject(expense.copy(status = Status.Rejected)) },
                        shape = AppShapes.small,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = statusColor(Status.Rejected))
                    ) { Text("Reject") }

                    Button(
                        onClick = { onApproveReject(expense.copy(status = Status.Approved)) },
                        shape = AppShapes.small,
                        colors = ButtonDefaults.buttonColors(containerColor = statusColor(Status.Approved))
                    ) { Text("Approve") }
                }
            }
        }
    }
}

// --- Approve/Reject Dialog with Optional Note ---
@Composable
fun ApproveRejectDialog(expense: StaffExpensePending, onDismiss: () -> Unit, onAction: (StaffExpensePending) -> Unit) {
    var note by remember { mutableStateOf(expense.rejectionNotes ?: "") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = AppShapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier.fillMaxWidth().padding(AppSpacing.md)
        ) {
            Column(modifier = Modifier.padding(AppSpacing.md)) {
                Text(
                    "Approve or Reject Expense",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(expense.title, style = MaterialTheme.typography.bodyMedium)
                Text("₹${expense.amount}", style = MaterialTheme.typography.bodyMedium)
                Text(expense.staffName, style = MaterialTheme.typography.bodyMedium)
                Text(expense.category, style = MaterialTheme.typography.bodyMedium)
                Text(expense.date, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(AppSpacing.sm))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Add Note (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppSpacing.md))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm, Alignment.End),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    OutlinedButton(
                        onClick = { onAction(expense.copy(status = Status.Rejected, rejectionNotes = note)) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = statusColor(Status.Rejected))
                    ) { Text("Reject") }
                    Button(
                        onClick = { onAction(expense.copy(status = Status.Approved, rejectionNotes = note)) },
                        colors = ButtonDefaults.buttonColors(containerColor = statusColor(Status.Approved))
                    ) { Text("Approve") }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPendingApprovalsScreen() {
    PendingApprovalsScreen()
}
