package com.abhishek.spendly.ui.screens.legal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.BuildConfig
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.ui.components.FinanceTopBar
import com.abhishek.spendly.ui.theme.AppSpacing

@Composable
fun AboutUsScreen(navManager: NavManager? = null) {
    Scaffold(
        topBar = {
            FinanceTopBar(
                title = "About App",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {
            Icon(
                imageVector = Icons.Filled.AccountBalanceWallet,
                contentDescription = "Spendly",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .padding(AppSpacing.sm),
                tint = MaterialTheme.colorScheme.primary
            )

            Text("Spendly", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Version ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(AppSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    Text(
                        "Spendly is a personal and small-business expense tracker: track daily spending, " +
                            "switch into Business Mode for staff expense submission and approval workflows, " +
                            "keep an informal lending/borrowing ledger with contacts, and unlock deeper " +
                            "reports and multi-staff support with Premium.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Built for individuals and small teams who want a clear, private picture of where " +
                            "their money goes — with everything stored on-device.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                "Made with care in India 🇮🇳",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAboutUsScreen() {
    AboutUsScreen()
}
