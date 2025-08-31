@file:OptIn(ExperimentalMaterial3Api::class)

package com.abhishek.smartexpensetracker.ui.screens.expense

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhishek.smartexpensetracker.ui.components.FinanceTopBar

@Composable
fun BudgetScreen() {
    var budgetType by remember { mutableStateOf("Monthly") } // Monthly / Weekly
    var budgetAmount by remember { mutableIntStateOf(20000) }   // Budget set
    var spentAmount by remember { mutableIntStateOf(15999) }    // Spent amount

    val progress = (spentAmount.toFloat() / budgetAmount.toFloat()).coerceAtMost(1f)

    val budgetStatus = when {
        progress >= 1f -> "Exceeded"
        progress >= 0.8f -> "Nearing Limit"
        else -> "Safe"
    }

    val statusColor = when (budgetStatus) {
        "Exceeded" -> Color.Red
        "Nearing Limit" -> Color(0xFFFFA726) // Orange
        else -> Color(0xFF66BB6A) // Green
    }

    Scaffold(
        topBar = {
            FinanceTopBar(
                title = "Budget Tracker",
                showBackButton = true,
                showSearch = false,
                showFilter = false,
                showNotifications = false,
                showMenu = false,
                onBackClick = {/*TODO: Implement Back Button.*/}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Budget Type Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = budgetType == "Monthly",
                    onClick = { budgetType = "Monthly" },
                    label = { Text("Monthly") },
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = budgetType == "Weekly",
                    onClick = { budgetType = "Weekly" },
                    label = { Text("Weekly") },
                    shape = CircleShape
                )
            }

            // Budget Info Card
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Your $budgetType Budget", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Text("₹$budgetAmount", fontSize = 28.sp, fontWeight = FontWeight.Bold)

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(50)),
                        color = statusColor
                    )

                    Text(
                        "Spent: ₹$spentAmount / ₹$budgetAmount",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Budget Status Alert
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Status: $budgetStatus",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                    if (budgetStatus == "Exceeded") {
                        Text("You have overspent! Consider adjusting expenses.", color = Color.Red)
                    } else if (budgetStatus == "Nearing Limit") {
                        Text("You’re nearing your limit, track expenses carefully.", color = Color(0xFFFFA726))
                    } else {
                        Text("You are within your budget, keep going strong!", color = Color(0xFF388E3C))
                    }
                }
            }

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { budgetAmount += 1000 }) {
                    Text("Increase Budget")
                }
                Button(onClick = { spentAmount += 500 }) {
                    Text("Add Expense")
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBudgetScreen() {
    BudgetScreen()
}