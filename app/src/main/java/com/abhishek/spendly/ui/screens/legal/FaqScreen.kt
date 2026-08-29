package com.abhishek.spendly.ui.screens.legal

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.ui.components.FinanceTopBar
import com.abhishek.spendly.ui.theme.AppSpacing

private data class FaqEntry(val question: String, val answer: String)

private val faqEntries = listOf(
    FaqEntry(
        "What's the difference between Personal and Business Mode?",
        "Personal Mode tracks your own day-to-day spending and budgets. Business Mode adds " +
            "staff expense submission, an approval workflow for admins/approvers, and per-staff " +
            "spending reports — switch between them any time from Settings."
    ),
    FaqEntry(
        "How does the Lender/Borrower ledger work?",
        "It's a simple ledger for informal money you give or take with people you know — record " +
            "the amount, a due date and notes, mark it paid once settled, and send a reminder " +
            "over SMS when it's due."
    ),
    FaqEntry(
        "How do staff expense approvals work?",
        "A staff member submits an expense with a category, amount and optional receipt. It shows " +
            "up under Pending Approvals for an Admin/Approver, who can approve or reject it directly " +
            "from the dashboard."
    ),
    FaqEntry(
        "What do I get with Premium?",
        "Premium raises limits like the number of staff you can add, and unlocks deeper reports. " +
            "You can compare plans and upgrade from Settings > Go Premium."
    ),
    FaqEntry(
        "Is my data backed up if I switch phones?",
        "By default your data lives only on this device. Turn on Cloud Sync from Settings to back " +
            "it up to your chosen provider so it can be restored on a new device."
    )
)

@Composable
fun FaqScreen(navManager: NavManager? = null) {
    Scaffold(
        topBar = {
            FinanceTopBar(
                title = "FAQ",
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
                .padding(padding)
                .padding(AppSpacing.md)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            faqEntries.forEach { entry -> FaqItem(entry) }
        }
    }
}

@Composable
private fun FaqItem(entry: FaqEntry) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(AppSpacing.md)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    entry.question,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(end = AppSpacing.sm)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
            if (expanded) {
                Text(
                    entry.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = AppSpacing.sm)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFaqScreen() {
    FaqScreen()
}
