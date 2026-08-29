package com.abhishek.spendly.ui.screens.legal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.ui.components.FinanceTopBar
import com.abhishek.spendly.ui.theme.AppSpacing

@Composable
fun TermsAndConditionsScreen(navManager: NavManager? = null) {
    Scaffold(
        topBar = {
            FinanceTopBar(
                title = "Terms & Conditions",
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
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(AppSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
                ) {
                    TermsSection(
                        "1. Accepting these terms",
                        "By installing and using Spendly, you agree to these Terms & Conditions. " +
                            "If you disagree with any part, please don't use the app."
                    )
                    TermsSection(
                        "2. Your data, your responsibility",
                        "Expense, budget and lending records you enter are for your own personal or " +
                            "business record-keeping. Spendly is a tracking tool, not an accounting, tax, " +
                            "or legal advisory service — verify any figures you rely on for filings or disputes."
                    )
                    TermsSection(
                        "3. Informal lending ledger",
                        "The Lender/Borrower feature helps you track informal money given or taken between " +
                            "individuals. It does not constitute a loan agreement, does not calculate legally " +
                            "binding interest, and Spendly is not a party to any such arrangement."
                    )
                    TermsSection(
                        "4. Subscriptions",
                        "Premium features are offered on monthly/yearly subscription plans as described " +
                            "in-app. Pricing and included features may change with notice inside the app."
                    )
                    TermsSection(
                        "5. No warranty",
                        "Spendly is provided \"as is\" without warranty of any kind. We work to keep the app " +
                            "reliable, but we're not liable for financial decisions made based on its data."
                    )
                    TermsSection(
                        "6. Changes",
                        "These terms may be updated as the app evolves; continued use after an update means " +
                            "you accept the revised terms."
                    )
                }
            }
        }
    }
}

@Composable
private fun TermsSection(title: String, body: String) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(
            body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun PreviewTermsAndConditionsScreen() {
    TermsAndConditionsScreen()
}
