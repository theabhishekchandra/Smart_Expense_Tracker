package com.abhishek.smartexpensetracker.ui.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

data class ExpenseContribution(
    val id: Int,
    val title: String,
    val amount: Double,
    val date: String,
    val status: Status,
    val notes: String? = null // Optional notes field
)
enum class Status {
    Pending, Approved, Rejected
}

// --- Sample Data ---
val sampleContributions = listOf(
    ExpenseContribution(1, "Office Lunch", 500.0, "23 Aug 2025", Status.Pending, "Team lunch for project kickoff"),
    ExpenseContribution(2, "Travel Expense", 1200.0, "22 Aug 2025", Status.Approved, "Client meeting travel"),
    ExpenseContribution(3, "Stationery", 300.0, "21 Aug 2025", Status.Pending, "Purchased pens and notebooks")
)

@Composable
fun QuickViewScreen(
    contributions: List<ExpenseContribution>,
    isAdmin: Boolean = true
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // --- Header ---
        Text(
            text = if (isAdmin) "Pending Approvals" else "Your Contributions",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        // --- Summary Card ---
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: ${contributions.size}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                val pendingCount = contributions.count { it.status == Status.Pending }
                Text(
                    text = "Pending: $pendingCount",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Contributions List ---
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(contributions) { contribution ->
                ContributionCard(contribution)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
@Composable
fun ContributionCard(contribution: ExpenseContribution) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to details */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(contribution.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "₹${contribution.amount} • ${contribution.date}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
                // Status Badge
                Box(
                    modifier = Modifier
                        .background(
                            when (contribution.status) {
                                Status.Pending -> Color(0xFFFFF59D)
                                Status.Approved -> Color(0xFF81C784)
                                Status.Rejected -> Color(0xFFE57373)
                            },
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = contribution.status.name,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // --- Notes ---
            contribution.notes?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Note: $it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewQuickViewScreen() {
    QuickViewScreen(contributions = sampleContributions, isAdmin = true)
}
