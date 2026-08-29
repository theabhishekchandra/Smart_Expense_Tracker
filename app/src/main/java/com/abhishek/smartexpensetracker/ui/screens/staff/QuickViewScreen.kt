package com.abhishek.smartexpensetracker.ui.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.ui.theme.AppShapes
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing
import com.abhishek.smartexpensetracker.ui.theme.DangerColor
import com.abhishek.smartexpensetracker.ui.theme.DangerColorDark
import com.abhishek.smartexpensetracker.ui.theme.SuccessColor
import com.abhishek.smartexpensetracker.ui.theme.SuccessColorDark
import com.abhishek.smartexpensetracker.ui.theme.WarningColor
import com.abhishek.smartexpensetracker.ui.theme.WarningColorDark

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

/**
 * Semantic color representing a [Status], shared by the staff approval screens so pending
 * approvals always render the same way regardless of which screen shows them.
 */
@Composable
fun statusColor(status: Status): Color {
    val dark = isSystemInDarkTheme()
    return when (status) {
        Status.Pending -> if (dark) WarningColorDark else WarningColor
        Status.Approved -> if (dark) SuccessColorDark else SuccessColor
        Status.Rejected -> if (dark) DangerColorDark else DangerColor
    }
}

/** Small rounded status chip shared across the staff approval screens. */
@Composable
fun StatusBadge(status: Status, modifier: Modifier = Modifier) {
    val color = statusColor(status)
    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.15f), AppShapes.small)
            .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
    ) {
        Text(
            text = status.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

@Composable
fun QuickViewScreen(
    contributions: List<ExpenseContribution>,
    isAdmin: Boolean = true
) {
    Column(modifier = Modifier.fillMaxSize().padding(AppSpacing.md)) {
        // --- Header ---
        Text(
            text = if (isAdmin) "Pending Approvals" else "Your Contributions",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(AppSpacing.sm))

        // --- Summary Card ---
        Card(
            shape = AppShapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(AppSpacing.md),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: ${contributions.size}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(AppSpacing.md))
                val pendingCount = contributions.count { it.status == Status.Pending }
                Text(
                    text = "Pending: $pendingCount",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = statusColor(Status.Pending)
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.md))

        // --- Contributions List ---
        if (contributions.isEmpty()) {
            EmptyContributionsState(isAdmin = isAdmin)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
            ) {
                items(contributions) { contribution ->
                    ContributionCard(contribution)
                }
            }
        }
    }
}

@Composable
private fun EmptyContributionsState(isAdmin: Boolean) {
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
                text = if (isAdmin) "No pending approvals" else "No contributions yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ContributionCard(contribution: ExpenseContribution) {
    Card(
        shape = AppShapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Navigate to details */ }
    ) {
        Column(modifier = Modifier.padding(AppSpacing.md)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(contribution.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Text(
                        "₹${contribution.amount} • ${contribution.date}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                StatusBadge(status = contribution.status)
            }

            // --- Notes ---
            contribution.notes?.let {
                Spacer(modifier = Modifier.height(AppSpacing.sm))
                Text(
                    text = "Note: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
