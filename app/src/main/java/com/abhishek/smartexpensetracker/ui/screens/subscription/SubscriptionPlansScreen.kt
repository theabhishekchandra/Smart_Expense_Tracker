package com.abhishek.smartexpensetracker.ui.screens.subscription

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionPlansScreen(
    onSubscribeClick: (String) -> Unit // PlanId: "free", "monthly", "yearly"
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Subscription Plans") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Choose the right plan for you",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Free Plan
            PlanCard(
                title = "Free Plan",
                price = "₹0 / month",
                features = listOf(
                    "✔ Track expenses manually",
                    "✔ Basic categories",
                    "✔ Monthly summary report",
                    "✖ AI-powered insights",
                    "✖ Multi-device sync",
                    "✖ Export to Excel/CSV"
                ),
                isPremium = false,
                onClick = { onSubscribeClick("free") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Premium Monthly Plan
            PlanCard(
                title = "Premium Monthly",
                price = "₹199 / month",
                features = listOf(
                    "✔ Everything in Free",
                    "✔ AI-powered expense insights",
                    "✔ Unlimited categories",
                    "✔ Multi-device sync",
                    "✔ Export to Excel/CSV/PDF",
                    "✔ Staff/Team expense tracking"
                ),
                isPremium = true,
                onClick = { onSubscribeClick("monthly") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Premium Yearly Plan
            PlanCard(
                title = "Premium Yearly",
                price = "₹1999 / year (Save 20%)",
                features = listOf(
                    "✔ Everything in Monthly Plan",
                    "✔ Priority customer support",
                    "✔ Early access to new features"
                ),
                isPremium = true,
                highlight = true,
                onClick = { onSubscribeClick("yearly") }
            )
        }
    }
}

@Composable
fun PlanCard(
    title: String,
    price: String,
    features: List<String>,
    isPremium: Boolean,
    highlight: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (highlight) Color(0xFFffe082) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = price,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = if (isPremium) MaterialTheme.colorScheme.primary else Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            features.forEach {
                Text(text = it, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = if (isPremium) "Subscribe Now" else "Continue Free")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSubscriptionPlansScreen() {
    SubscriptionPlansScreen(onSubscribeClick = {})
}
