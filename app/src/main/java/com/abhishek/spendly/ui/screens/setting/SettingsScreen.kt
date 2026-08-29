package com.abhishek.spendly.ui.screens.setting

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.spendly.core.datastore.Currency
import com.abhishek.spendly.core.datastore.ExportFormat
import com.abhishek.spendly.core.datastore.Language
import com.abhishek.spendly.core.datastore.SyncFrequency
import com.abhishek.spendly.core.datastore.SyncWith
import com.abhishek.spendly.core.datastore.ThemeType
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.core.navigation.ScreenRoutes
import com.abhishek.spendly.ui.components.FinanceTopBar
import com.abhishek.spendly.ui.theme.AppSpacing

@Composable
fun SettingsScreen(
    navManager: NavManager? = null,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val prefs by viewModel.userPreferences.collectAsState()
    val isLoading by viewModel.loader.collectAsState()

    val languages = Language.entries.map { it.value }
    val currencies = Currency.entries.map { it.value }
    val exportFormats = ExportFormat.entries.map { it.value }
    val cloudProviders = SyncWith.entries.map { it.value }
    val syncFrequencies = SyncFrequency.entries.map { it.value }


    val context = LocalContext.current
    // Collect state flows
    val loader by viewModel.loader.collectAsState()
    val toastMessage = viewModel.toastMessage

    // One-time toast effect
    LaunchedEffect(toastMessage) {
        toastMessage.collect { message ->
            if (message.isNotBlank()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            FinanceTopBar(
                title = "Settings",
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
                .padding(AppSpacing.md)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.lg)
        ) {

            // Account Section
            SettingsSection("Account") {
                SettingsItem(
                    Icons.Default.Person, "Edit Profile",
                    "Change your name, email & picture", { navManager?.navigate(ScreenRoutes.EditProfile.route)}
                )
                SettingsRowDivider()
                SettingsItem(Icons.Default.Business, "Business Details",
                    "Edit company name, GST, PAN, etc.", {navManager?.navigate(ScreenRoutes.EditBusinessDetails.route)}
                )
            }

            // App Preferences
            SettingsSection("App Preferences") {
                SettingsDropdownItem(Icons.Default.Translate, "Language", prefs.language.value, languages) {
                    viewModel.setLanguage(Language.fromValue(it))
                }
                SettingsRowDivider()
                SettingsDropdownItem(Icons.Default.CurrencyRupee, "Currency", prefs.currency.value, currencies) {
                    viewModel.setCurrency(Currency.fromValue(it))
                }
                SettingsRowDivider()
                // Theme & Business Mode
                SettingsToggleItem(Icons.Default.DarkMode, "Dark Mode", prefs.themeMode == ThemeType.DARK) {
                    viewModel.setTheme(if (it) ThemeType.DARK else ThemeType.LIGHT)
                }
                SettingsRowDivider()
                SettingsToggleItem(Icons.Default.Business, "Business Mode", prefs.isBusinessMode) {
                    viewModel.setBusinessMode(it)
                }
            }

            // Reports
            SettingsSection("Reports") {
                SettingsDropdownItem(Icons.Default.Description, "Export Format", prefs.exportFormat.value, exportFormats) {
                    viewModel.setExportFormat(ExportFormat.fromValue(it))
                }
            }

            // Cloud Sync
            SettingsSection("Cloud Sync") {
                SettingsDropdownItem(Icons.Default.Cloud, "Sync With", prefs.syncWith.value, cloudProviders) {
                    viewModel.setSyncWith(SyncWith.fromValue(it))
                }
                SettingsRowDivider()
                SettingsDropdownItem(Icons.Default.Schedule, "Sync Frequency", prefs.syncFrequency.value, syncFrequencies) {
                    viewModel.setSyncFrequency(SyncFrequency.fromValue(it))
                }
            }

            // Security
            SettingsSection("Security") {
                SettingsItem(Icons.Default.Lock, "Change Password",
                    "Update your login password",{
                        // TODO: Implement password change
                        showToast("Coming Soon...")
                    }
                )
                SettingsRowDivider()
                SettingsItem(Icons.Default.Fingerprint, "Biometric Login",
                    "Use fingerprint or face unlock",{
                        // TODO: Implement biometric login
                        showToast("Coming Soon...")}
                )
            }

            // Notifications
            SettingsSection("Notifications") {
                SettingsToggleItem(Icons.Default.Notifications, "Push Notifications", prefs.pushNotifications) {
                    viewModel.setPushNotifications(it)
                }
                SettingsRowDivider()
                SettingsToggleItem(Icons.Default.Email, "Email Alerts", prefs.emailAlerts) {
                    viewModel.setEmailAlerts(it)
                }
            }

            // Premium
            SettingsSection("Premium") {
                SettingsItem(Icons.Default.Star, "Go Premium",
                    "Unlock advanced features",{ navManager?.navigate(ScreenRoutes.Subscription.route)}
                )
            }

            // Support
            SettingsSection("Support") {
                SettingsItem(Icons.AutoMirrored.Filled.Chat, "Chat Support",
                    "Talk to our support team instantly",{
                        // TODO: Implement chat support
                        showToast("Coming Soon...")})
                SettingsRowDivider()
                SettingsItem(Icons.Default.Email, "Email Support",
                    "support@smartexpense.com",{
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:support@smartexpense.com")
                            putExtra(Intent.EXTRA_SUBJECT, "Spendly Support Request")
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        } else {
                            showToast("No email app found")
                        }
                    })
                SettingsRowDivider()
                SettingsItem(Icons.AutoMirrored.Filled.HelpOutline, "FAQ",
                    "Frequently asked questions",{ navManager?.navigate(ScreenRoutes.FAQ.route) })
            }

            // About
            SettingsSection("About") {
                SettingsItem(Icons.Default.PrivacyTip, "Privacy & Terms", "Read our policies",{
                    navManager?.navigate(ScreenRoutes.PrivacyPolicy.route)
                })
                SettingsRowDivider()
                SettingsItem(Icons.Default.Info, "About App", "Version 1.0.0",{
                    navManager?.navigate(ScreenRoutes.AboutUs.route)
                })
                SettingsRowDivider()
                SettingsItem(Icons.Default.Share, "Share App", "Invite your friends",{
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Check out Spendly — a smart personal & business expense tracking app!")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Spendly via"))
                })
            }

            Spacer(modifier = Modifier.height(AppSpacing.sm))

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }

        if(loader) {
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

/**
 * A settings section: a title above a white [Card] grouping its rows, instead of a bare
 * heading + rows directly on the scaffold background - gives each group a clean, elevated
 * surface consistent with the rest of the "vibrant gradient fintech" redesign.
 */
@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = AppSpacing.xs, bottom = AppSpacing.sm)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = AppSpacing.xs),
                content = content
            )
        }
    }
}

@Composable
private fun SettingsRowDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = AppSpacing.md),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, description: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = AppSpacing.sm + AppSpacing.xs, vertical = AppSpacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(AppSpacing.sm + AppSpacing.xs))
        Column {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SettingsToggleItem(icon: ImageVector, title: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppSpacing.sm + AppSpacing.xs, vertical = AppSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(AppSpacing.sm + AppSpacing.xs))
            Text(title, style = MaterialTheme.typography.bodyLarge)
        }
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}
@Composable
fun SettingsDropdownItem(
    icon: ImageVector,
    title: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(horizontal = AppSpacing.sm + AppSpacing.xs, vertical = AppSpacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(AppSpacing.sm + AppSpacing.xs))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(selectedOption, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Default.ArrowDropDown, contentDescription = "Select $title")
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingScreen() {
    SettingsScreen(
        null,
        hiltViewModel()
    )
}
