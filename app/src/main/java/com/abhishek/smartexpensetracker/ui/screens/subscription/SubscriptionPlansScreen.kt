package com.abhishek.smartexpensetracker.ui.screens.subscription

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.smartexpensetracker.core.datastore.AppPreferencesRepository
import com.abhishek.smartexpensetracker.core.datastore.PremiumType
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.ui.components.FinanceTopBar
import com.abhishek.smartexpensetracker.ui.screens.setting.SettingsViewModel
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionPlansScreen(
    navManager: NavManager? = null,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    // Collect state flows
    val loader by settingsViewModel.loader.collectAsState()
    val toastMessage = settingsViewModel.toastMessage

    // One-time toast effect
    LaunchedEffect(toastMessage) {
        toastMessage.collect { message ->
            if (message.isNotBlank()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            FinanceTopBar(
                title = "Subscription Plans",
                showBackButton = true,
                showSearch = false,
                showFilter = false,
                showNotifications = false,
                showMenu = false,
                onBackClick = { navManager?.navigateBack()},
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

//            Text(
//                text = "Choose the right plan for you",
//                style = MaterialTheme.typography.headlineSmall,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )

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
                onClick = { settingsViewModel.setPremium(PremiumType.BASIC,false) }
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
                onClick = { settingsViewModel.setPremium(PremiumType.MONTHLY,true)}
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
                onClick = { settingsViewModel.setPremium(PremiumType.YEARLY,true) }
            )
        }
        // Loader Overlay
        if (loader) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
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
    val cardColor = if (highlight) Color(0xFFffe082) else MaterialTheme.colorScheme.surface
    val featureTextColor = if (highlight) Color.Black else MaterialTheme.colorScheme.onSurface
    val priceColor = if (isPremium) MaterialTheme.colorScheme.primary else Color.Gray

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = price,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = priceColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            features.forEach { feature ->
                Text(
                    text = feature,
                    fontSize = 14.sp,
                    color = featureTextColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (isPremium) "Subscribe Now" else "Continue Free",
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSubscriptionPlansScreenLight() {
    SmartExpenseTrackerTheme(darkTheme = false, ) {
        SubscriptionPlansScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSubscriptionPlansScreenDark() {
    SmartExpenseTrackerTheme(darkTheme = true, ) {
        SubscriptionPlansScreen()
    }
}
