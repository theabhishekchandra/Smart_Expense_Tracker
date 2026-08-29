package com.abhishek.spendly.ui.screens.lender

import androidx.compose.foundation.background
import com.abhishek.spendly.ui.theme.isAppDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.ui.components.AnimatedAmountText
import com.abhishek.spendly.ui.components.GradientCard
import com.abhishek.spendly.ui.theme.AppShapes
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.DangerColor
import com.abhishek.spendly.ui.theme.DangerColorDark
import com.abhishek.spendly.ui.theme.SuccessColor
import com.abhishek.spendly.ui.theme.SuccessColorDark
import com.abhishek.spendly.ui.theme.WarningColor
import com.abhishek.spendly.ui.theme.WarningColorDark

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

/** Semantic color for a free-form lending status string ("Paid"/"Pending"/"Overdue"). */
@Composable
private fun lendingStatusColor(status: String): Color {
    val dark = isAppDarkTheme()
    return when (status.lowercase()) {
        "paid" -> if (dark) SuccessColorDark else SuccessColor
        "overdue" -> if (dark) DangerColorDark else DangerColor
        else -> if (dark) WarningColorDark else WarningColor
    }
}

@Composable
private fun LendingStatusBadge(status: String, modifier: Modifier = Modifier) {
    val color = lendingStatusColor(status)
    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.15f), AppShapes.small)
            .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LenderBorrowerDetailsScreen(
    person: PersonDetail,
    onMarkAsPaid: () -> Unit,
    onSendReminder: () -> Unit,
    onEdit: () -> Unit,
    onBack: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = person.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppSpacing.sm),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                if (person.status == "Pending") {
                    Button(onClick = onMarkAsPaid, shape = AppShapes.small, modifier = Modifier.weight(1f)) {
                        Text("Mark as Paid")
                    }
                }
                Button(onClick = onSendReminder, shape = AppShapes.small, modifier = Modifier.weight(1f)) {
                    Text("Send Reminder")
                }
                OutlinedButton(onClick = onEdit, shape = AppShapes.small, modifier = Modifier.weight(1f)) {
                    Text("Edit")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(AppSpacing.md)
                .fillMaxSize()
        ) {
            // Summary hero: total amount owed, in the same GradientCard +
            // AnimatedAmountText style as Home's summary cards.
            GradientCard(modifier = Modifier.fillMaxWidth().padding(bottom = AppSpacing.md)) {
                Text(
                    "Total Amount",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(AppSpacing.xs))
                AnimatedAmountText(
                    amount = person.totalAmount,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    decimals = 0
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Box(
                    modifier = Modifier
                        .background(Color.White, AppShapes.small)
                        .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
                ) {
                    Text(
                        text = person.status,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = lendingStatusColor(person.status)
                    )
                }
                Spacer(modifier = Modifier.height(AppSpacing.xs))
                Text(
                    "Last Updated: ${person.lastUpdated}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            // Transaction History Title
            Text("Transaction History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(AppSpacing.sm))

            // Transaction List
            if (person.transactions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(AppSpacing.sm))
                        Text(
                            "No transactions yet",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                ) {
                    items(person.transactions) { transaction ->
                        Card(
                            shape = AppShapes.medium,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppSpacing.sm),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(transaction.date, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                    Text(
                                        transaction.notes,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    AnimatedAmountText(
                                        amount = transaction.amount,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        decimals = 0
                                    )
                                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                                    LendingStatusBadge(status = transaction.status)
                                }
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
