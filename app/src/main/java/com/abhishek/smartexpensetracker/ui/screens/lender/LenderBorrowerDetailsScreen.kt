package com.abhishek.smartexpensetracker.ui.screens.lender

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhishek.smartexpensetracker.ui.components.FinanceTopBar

// Data classes
data class Transaction(
    val date: String,
    val amount: Double,
    val status: String,
    val notes: String
)

data class PersonDetail(
    val name: String,
    val totalAmount: Double,
    val status: String,
    val lastUpdated: String,
    val transactions: List<Transaction>
)

@Composable
fun LenderBorrowerDetailsScreen(
    person: PersonDetail,
    onMarkAsPaid: () -> Unit,
    onSendReminder: () -> Unit,
    onEdit: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = person.name, fontSize = 20.sp, fontWeight = FontWeight.Bold) }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (person.status == "Pending") {
                    Button(onClick = onMarkAsPaid, modifier = Modifier.weight(1f)) {
                        Text("Mark as Paid")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Button(onClick = onSendReminder, modifier = Modifier.weight(1f)) {
                    Text("Send Reminder")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(onClick = onEdit, modifier = Modifier.weight(1f)) {
                    Text("Edit")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Amount: ₹${person.totalAmount}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Status: ${person.status}", fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
                    Text("Last Updated: ${person.lastUpdated}", fontSize = 14.sp)
                }
            }

            // Transaction History Title
            Text("Transaction History", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))

            // Transaction List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(person.transactions) { transaction ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Date: ${transaction.date}", fontWeight = FontWeight.Medium)
                                Text("Notes: ${transaction.notes}", fontSize = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("₹${transaction.amount}", fontWeight = FontWeight.Bold)
                                Text(transaction.status, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------- Preview ----------
@Composable
@Preview(showBackground = true)
fun PreviewLenderBorrowerDetailsScreen() {
    val samplePerson = PersonDetail(
        name = "Ravi Kumar",
        totalAmount = 5000.0,
        status = "Pending",
        lastUpdated = "29 Aug 2025",
        transactions = listOf(
            Transaction("25 Aug 2025", 2000.0, "Paid", "First installment"),
            Transaction("28 Aug 2025", 3000.0, "Pending", "Second installment")
        )
    )

    LenderBorrowerDetailsScreen(
        person = samplePerson,
        onMarkAsPaid = {},
        onSendReminder = {},
        onEdit = {}
    )
}
