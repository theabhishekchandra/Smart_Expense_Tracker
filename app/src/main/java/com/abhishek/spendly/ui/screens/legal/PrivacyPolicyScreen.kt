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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.core.navigation.ScreenRoutes
import com.abhishek.spendly.ui.components.FinanceTopBar
import com.abhishek.spendly.ui.theme.AppSpacing

@Composable
fun PrivacyPolicyScreen(navManager: NavManager? = null) {
    Scaffold(
        topBar = {
            FinanceTopBar(
                title = "Privacy Policy",
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
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
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
                    PolicySection(
                        title = "What we store",
                        body = "Spendly stores your expenses, budgets, contacts and profile details " +
                            "directly on your device using local, encrypted-at-rest Android storage " +
                            "(Room database and DataStore preferences). We do not currently operate a " +
                            "cloud backend, so this data is not uploaded to or processed on any Spendly server."
                    )
                    PolicySection(
                        title = "What we don't do",
                        body = "We do not sell your data. We do not share your expense, income or " +
                            "contact information with advertisers or third parties."
                    )
                    PolicySection(
                        title = "Permissions",
                        body = "Spendly may request access to your photos (to attach a receipt or profile " +
                            "picture) and contacts (to speed up adding a lender/borrower). These are used only " +
                            "to fill in the record you're creating and are never transmitted off your device."
                    )
                    PolicySection(
                        title = "Cloud sync & backup",
                        body = "If you enable cloud sync from Settings, your data is uploaded to the cloud " +
                            "provider you choose, under that provider's own privacy terms."
                    )
                    PolicySection(
                        title = "Changes to this policy",
                        body = "As Spendly adds new features (such as a hosted backend or online payments), " +
                            "this policy will be updated to reflect exactly what data leaves your device and why."
                    )
                    PolicySection(
                        title = "Contact",
                        body = "Questions about this policy can be sent to support@smartexpense.com."
                    )
                }
            }

            OutlinedButton(
                onClick = { navManager?.navigate(ScreenRoutes.TermsAndConditions.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Terms & Conditions")
            }
        }
    }
}

@Composable
private fun PolicySection(title: String, body: String) {
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
private fun PreviewPrivacyPolicyScreen() {
    PrivacyPolicyScreen()
}
