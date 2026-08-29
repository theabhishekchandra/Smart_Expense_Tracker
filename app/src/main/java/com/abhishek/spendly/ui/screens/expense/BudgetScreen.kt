@file:OptIn(ExperimentalMaterial3Api::class)

package com.abhishek.spendly.ui.screens.expense

import com.abhishek.spendly.ui.theme.isAppDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.ui.components.AnimatedAmountText
import com.abhishek.spendly.ui.components.FinanceTopBar
import com.abhishek.spendly.ui.components.GradientCard
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.DangerColor
import com.abhishek.spendly.ui.theme.DangerColorDark
import com.abhishek.spendly.ui.theme.SuccessColor
import com.abhishek.spendly.ui.theme.SuccessColorDark
import com.abhishek.spendly.ui.theme.WarningColor
import com.abhishek.spendly.ui.theme.WarningColorDark

@Composable
fun BudgetScreen(navManager: NavManager? = null) {
    var budgetType by remember { mutableStateOf("Monthly") } // Monthly / Weekly
    var budgetAmount by remember { mutableIntStateOf(20000) }   // Budget set
    var spentAmount by remember { mutableIntStateOf(15999) }    // Spent amount

    val progress = (spentAmount.toFloat() / budgetAmount.toFloat()).coerceAtMost(1f)

    val budgetStatus = when {
        progress >= 1f -> "Exceeded"
        progress >= 0.8f -> "Nearing Limit"
        else -> "Safe"
    }

    val isDark = isAppDarkTheme()
    val statusColor = when (budgetStatus) {
        "Exceeded" -> if (isDark) DangerColorDark else DangerColor
        "Nearing Limit" -> if (isDark) WarningColorDark else WarningColor
        else -> if (isDark) SuccessColorDark else SuccessColor
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
                onBackClick = { navManager?.navigateBack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
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
                    label = { Text("Monthly", style = MaterialTheme.typography.labelLarge) },
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(AppSpacing.sm))
                FilterChip(
                    selected = budgetType == "Weekly",
                    onClick = { budgetType = "Weekly" },
                    label = { Text("Weekly", style = MaterialTheme.typography.labelLarge) },
                    shape = CircleShape
                )
            }

            // Budget Info Card - the hero moment on this screen
            GradientCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(AppSpacing.lg)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Your $budgetType Budget",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    AnimatedAmountText(
                        amount = budgetAmount.toDouble(),
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White,
                        decimals = 0
                    )

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(MaterialTheme.shapes.extraLarge),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.25f)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AnimatedAmountText(
                            amount = spentAmount.toDouble(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            prefix = "Spent: ₹",
                            decimals = 0
                        )
                        Text(
                            " / ₹$budgetAmount",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Budget Status Alert
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(AppSpacing.md),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Status: $budgetStatus",
                        style = MaterialTheme.typography.titleMedium,
                        color = statusColor
                    )
                    val message = when (budgetStatus) {
                        "Exceeded" -> "You have overspent! Consider adjusting expenses."
                        "Nearing Limit" -> "You’re nearing your limit, track expenses carefully."
                        else -> "You are within your budget, keep going strong!"
                    }
                    Text(message, style = MaterialTheme.typography.bodyMedium, color = statusColor)
                }
            }

            // Quick Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { budgetAmount += 1000 },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("Increase Budget", style = MaterialTheme.typography.labelLarge)
                }
                Button(
                    onClick = { spentAmount += 500 },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text("Add Expense", style = MaterialTheme.typography.labelLarge)
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