package com.abhishek.smartexpensetracker.ui.screens.staff

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.tooling.preview.Preview

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
fun PendingApprovalsScreen() {
    var selectedExpense by remember { mutableStateOf<StaffExpensePending?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Pending Approvals",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(samplePendingExpenses) { expense ->
                StaffExpenseCard(expense, onApproveReject = { selectedExpense = it; showDialog = true })
                Spacer(modifier = Modifier.height(8.dp))
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
fun StaffExpenseCard(expense: StaffExpensePending, onApproveReject: (StaffExpensePending) -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Receipt thumbnail placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                ) {
                    expense.receiptImage?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = "Receipt",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(expense.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(2.dp))
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
                                Status.Pending -> Color(0xFFFFF59D)
                                Status.Approved -> Color(0xFF81C784)
                                Status.Rejected -> Color(0xFFE57373)
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

            // Rejection note
            if (expense.status == Status.Rejected && !expense.rejectionNotes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rejection Note: ${expense.rejectionNotes}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFD32F2F)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Buttons only for Pending
            if (expense.status == Status.Pending) {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { onApproveReject(expense.copy(status = Status.Approved)) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Approve") }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onApproveReject(expense.copy(status = Status.Rejected)) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Reject") }
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
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Approve or Reject Expense", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Title: ${expense.title}")
                Text("Amount: ₹${expense.amount}")
                Text("Staff: ${expense.staffName}")
                Text("Category: ${expense.category}")
                Text("Date: ${expense.date}")

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Add Note (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onAction(expense.copy(status = Status.Approved, rejectionNotes = note)) }) {
                        Text("Approve")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onAction(expense.copy(status = Status.Rejected, rejectionNotes = note)) }) {
                        Text("Reject")
                    }
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
