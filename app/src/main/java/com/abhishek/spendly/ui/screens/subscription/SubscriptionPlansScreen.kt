package com.abhishek.spendly.ui.screens.subscription

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.spendly.core.datastore.PremiumType
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.ui.components.FinanceTopBar
import com.abhishek.spendly.ui.components.GradientCard
import com.abhishek.spendly.ui.screens.setting.SettingsViewModel
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.GreyColor
import com.abhishek.spendly.ui.theme.SpendlyTheme

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
    val premiumType by settingsViewModel.premiumType.collectAsState(initial = PremiumType.BASIC)

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
                .padding(AppSpacing.md)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Choose the right plan for you",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = AppSpacing.xs)
            )

            // Free Plan
            PlanCard(
                title = "Free Plan",
                price = "₹0",
                pricePeriod = "/ month",
                features = listOf(
                    "✔ Track expenses manually",
                    "✔ Basic categories",
                    "✔ Monthly summary report",
                    "✖ AI-powered insights",
                    "✖ Multi-device sync",
                    "✖ Export to Excel/CSV"
                ),
                isPremium = false,
                isCurrentPlan = premiumType == PremiumType.BASIC,
                onClick = { settingsViewModel.setPremium(PremiumType.BASIC,false) }
            )

            // Premium Monthly Plan
            PlanCard(
                title = "Premium Monthly",
                price = "₹199",
                pricePeriod = "/ month",
                features = listOf(
                    "✔ Everything in Free",
                    "✔ AI-powered expense insights",
                    "✔ Unlimited categories",
                    "✔ Multi-device sync",
                    "✔ Export to Excel/CSV/PDF",
                    "✔ Staff/Team expense tracking"
                ),
                isPremium = true,
                isCurrentPlan = premiumType == PremiumType.MONTHLY,
                onClick = { settingsViewModel.setPremium(PremiumType.MONTHLY,true)}
            )

            // Premium Yearly Plan
            PlanCard(
                title = "Premium Yearly",
                price = "₹1999",
                pricePeriod = "/ year",
                badge = "Save 20%",
                features = listOf(
                    "✔ Everything in Monthly Plan",
                    "✔ Priority customer support",
                    "✔ Early access to new features"
                ),
                isPremium = true,
                highlight = true,
                isCurrentPlan = premiumType == PremiumType.YEARLY,
                onClick = { settingsViewModel.setPremium(PremiumType.YEARLY,true) }
            )
        }
        // Loader Overlay
        if (loader) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.3f)),
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
    pricePeriod: String,
    features: List<String>,
    isPremium: Boolean,
    highlight: Boolean = false,
    isCurrentPlan: Boolean = false,
    badge: String? = null,
    onClick: () -> Unit
) {
    val buttonLabel = if (isCurrentPlan) "Current Plan" else if (isPremium) "Subscribe Now" else "Continue Free"

    if (highlight) {
        // The recommended/featured plan gets the brand hero gradient + white text instead
        // of a flat tinted container, so it reads as the visually "premium" choice.
        GradientCard(modifier = Modifier.fillMaxWidth()) {
            PlanCardBody(
                title = title,
                price = price,
                pricePeriod = pricePeriod,
                features = features,
                isCurrentPlan = isCurrentPlan,
                badgeText = badge ?: "Recommended",
                titleColor = Color.White,
                priceColor = Color.White,
                featureColor = Color.White.copy(alpha = 0.92f),
                dividerColor = Color.White.copy(alpha = 0.3f),
                tagContainerColor = Color.White.copy(alpha = 0.22f),
                tagContentColor = Color.White,
                buttonColors = ButtonDefaults.buttonColors(
                    // The button itself stays a solid white pill regardless of theme
                    // (it's meant to pop against the vivid hero gradient), so its label
                    // needs a color that's always dark - `colorScheme.primary` flips to
                    // a pale, on-dark-surface tone in dark mode and becomes unreadable
                    // on this white container.
                    containerColor = Color.White,
                    contentColor = GreyColor
                ),
                buttonLabel = buttonLabel,
                onClick = onClick
            )
        }
    } else {
        val titleColor = MaterialTheme.colorScheme.onSurface
        val priceColor = if (isPremium) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        Card(
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            PlanCardBody(
                title = title,
                price = price,
                pricePeriod = pricePeriod,
                features = features,
                isCurrentPlan = isCurrentPlan,
                badgeText = badge,
                titleColor = titleColor,
                priceColor = priceColor,
                featureColor = titleColor,
                dividerColor = MaterialTheme.colorScheme.outlineVariant,
                tagContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                tagContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                buttonColors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                buttonLabel = buttonLabel,
                onClick = onClick
            )
        }
    }
}

@Composable
private fun PlanCardBody(
    title: String,
    price: String,
    pricePeriod: String,
    features: List<String>,
    isCurrentPlan: Boolean,
    badgeText: String?,
    titleColor: Color,
    priceColor: Color,
    featureColor: Color,
    dividerColor: Color,
    tagContainerColor: Color,
    tagContentColor: Color,
    buttonColors: ButtonColors,
    buttonLabel: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(AppSpacing.md),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            if (isCurrentPlan) {
                PlanTag(text = "Current", containerColor = tagContainerColor, contentColor = tagContentColor)
            } else if (badgeText != null) {
                PlanTag(text = badgeText, containerColor = tagContainerColor, contentColor = tagContentColor)
            }
        }

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = price,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = priceColor
            )
            Spacer(modifier = Modifier.width(AppSpacing.xs))
            Text(
                text = pricePeriod,
                style = MaterialTheme.typography.bodyMedium,
                color = featureColor
            )
        }

        HorizontalDivider(color = dividerColor)

        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            features.forEach { feature ->
                Text(
                    text = feature,
                    style = MaterialTheme.typography.bodyMedium,
                    color = featureColor
                )
            }
        }

        Spacer(modifier = Modifier.height(AppSpacing.xs))

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = buttonColors
        ) {
            Text(text = buttonLabel, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun PlanTag(text: String, containerColor: Color, contentColor: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = containerColor,
        contentColor = contentColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSubscriptionPlansScreenLight() {
    SpendlyTheme(darkTheme = false, ) {
        SubscriptionPlansScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSubscriptionPlansScreenDark() {
    SpendlyTheme(darkTheme = true, ) {
        SubscriptionPlansScreen()
    }
}
