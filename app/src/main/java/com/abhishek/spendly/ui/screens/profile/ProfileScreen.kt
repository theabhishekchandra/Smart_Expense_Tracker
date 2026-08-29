package com.abhishek.spendly.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.abhishek.spendly.core.datastore.PremiumType
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.core.navigation.ScreenRoutes
import com.abhishek.spendly.ui.components.BaseScaffold
import com.abhishek.spendly.ui.components.FinanceTopBar
import com.abhishek.spendly.ui.components.GradientCard
import com.abhishek.spendly.ui.screens.setting.SettingsViewModel
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.SuccessColor
import com.abhishek.spendly.ui.theme.SuccessColorDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navManager: NavManager? = null,
    onEditProfile: () -> Unit,
    onEditBusinessDetails: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val prefs by settingsViewModel.userPreferences.collectAsState()
    val isPremium by settingsViewModel.isPremium.collectAsState(initial = false)
    val premiumType by settingsViewModel.premiumType.collectAsState(initial = PremiumType.BASIC)
    val profile by settingsViewModel.profileInfo.collectAsState()
    val business by settingsViewModel.businessInfo.collectAsState()
    val name = profile.name
    val email = profile.email
    val profileImage = profile.profileImage

    val loader by settingsViewModel.loader.collectAsState()
    val toastMessage by settingsViewModel.toastMessage.collectAsState(initial = "")

    val isDark = isSystemInDarkTheme()
    val successColor = if (isDark) SuccessColorDark else SuccessColor

    LaunchedEffect(toastMessage) {
        if (toastMessage.isNotEmpty()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }

    BaseScaffold(
        topBar = {
            FinanceTopBar(
                title = "Profile",
                showBackButton = false,
                showSearch = false,
                showFilter = false,
                showNotifications = false,
                showMenu = false
            )
        },
        currentRoute = ScreenRoutes.Profile.route,
        navManager = navManager,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(AppSpacing.md)
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {
                // Profile header - a gradient hero card (avatar + name/email in white)
                // instead of a flat top row, matching the app's brand gradient system.
                GradientCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (!profileImage.isNullOrBlank()) {
                                AsyncImage(
                                    model = profileImage,
                                    contentDescription = "Profile picture",
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.White.copy(alpha = 0.85f), CircleShape)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, Color.White.copy(alpha = 0.85f), CircleShape)
                                        .background(Color.White.copy(alpha = 0.16f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile picture",
                                        modifier = Modifier.size(36.dp),
                                        tint = Color.White
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(AppSpacing.sm + AppSpacing.xs))
                            Column {
                                Text(
                                    name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    email,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                        IconButton(onClick = { navManager?.navigateSingleTop(ScreenRoutes.Settings.route) }) {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.lg))

                // Premium Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = if (premiumType != PremiumType.BASIC) successColor else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(AppSpacing.md),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            if (premiumType != PremiumType.BASIC) "⭐ Premium User" else "Upgrade to Premium",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        if (premiumType == PremiumType.BASIC) {
                            Spacer(modifier = Modifier.height(AppSpacing.sm))
                            Button(
                                onClick = { navManager?.navigateSingleTop(ScreenRoutes.Subscription.route) },
                                shape = MaterialTheme.shapes.large,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Buy Premium", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.lg))

                // Business Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(AppSpacing.md)) {
                        Text("Business Details", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(AppSpacing.sm))
                        if (business.businessName.isBlank()) {
                            Text(
                                "No business details added yet",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text("Business Name: ${business.businessName}", style = MaterialTheme.typography.bodyMedium)
                            if (business.ownerName.isNotBlank()) {
                                Text("Owner: ${business.ownerName}", style = MaterialTheme.typography.bodyMedium)
                            }
                            if (business.email.isNotBlank()) {
                                Text("Business Email: ${business.email}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        Spacer(modifier = Modifier.height(AppSpacing.sm))
                        OutlinedButton(
                            onClick = onEditBusinessDetails,
                            shape = MaterialTheme.shapes.large
                        ) {
                            Text("Edit Business Details", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.lg))

                // Edit Profile Button
                OutlinedButton(
                    onClick = onEditProfile,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Edit Personal Profile", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    )
}


// Small Card for Stats
@Composable
fun StatCard(title: String, value: String) {
    ElevatedCard(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.bodySmall)
            Text(value, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun ProfileOption(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = AppSpacing.sm + AppSpacing.xs, horizontal = AppSpacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(AppSpacing.sm + AppSpacing.xs))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}



@Preview
@Composable
private fun PreviewProfileScreen() {
    ProfileScreen(
        onEditProfile = {},
        onEditBusinessDetails = {}
    )
}
