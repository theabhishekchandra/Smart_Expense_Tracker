package com.abhishek.smartexpensetracker.ui.screens.setting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.smartexpensetracker.core.datastore.Currency
import com.abhishek.smartexpensetracker.core.datastore.ExportFormat
import com.abhishek.smartexpensetracker.core.datastore.Language
import com.abhishek.smartexpensetracker.core.datastore.SyncFrequency
import com.abhishek.smartexpensetracker.core.datastore.SyncWith
import com.abhishek.smartexpensetracker.core.datastore.ThemeType
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.ui.components.FinanceTopBar

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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 🔹 Account Section
            Text("Account", style = MaterialTheme.typography.titleMedium)
            SettingsItem(
                Icons.Default.Person, "Edit Profile",
                "Change your name, email & picture", { navManager?.navigate(ScreenRoutes.EditProfile.route)}
            )
            SettingsItem(Icons.Default.Business, "Business Details",
                "Edit company name, GST, PAN, etc.", {navManager?.navigate(ScreenRoutes.EditBusinessDetails.route)}
            )
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // 🔹 App Preferences
            Text("App Preferences", style = MaterialTheme.typography.titleMedium)
            SettingsDropdownItem(Icons.Default.Translate, "Language", prefs.language.value, languages) {
                viewModel.setLanguage(Language.fromValue(it))
            }
            SettingsDropdownItem(Icons.Default.CurrencyRupee, "Currency", prefs.currency.value, currencies) {
                viewModel.setCurrency(Currency.fromValue(it))
            }
            // 🔹 Theme & Business Mode
            SettingsToggleItem(Icons.Default.DarkMode, "Dark Mode", prefs.themeMode == ThemeType.DARK) {
                viewModel.setTheme(if (it) ThemeType.DARK else ThemeType.LIGHT)
            }
            SettingsToggleItem(Icons.Default.Business, "Business Mode", prefs.isBusinessMode) {
                viewModel.setBusinessMode(it)
            }
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // 🔹 Reports
            Text("Reports", style = MaterialTheme.typography.titleMedium)
            SettingsDropdownItem(Icons.Default.Description, "Export Format", prefs.exportFormat.value, exportFormats) {
                viewModel.setExportFormat(ExportFormat.fromValue(it))
            }
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // 🔹 Cloud Sync
            Text("Cloud Sync", style = MaterialTheme.typography.titleMedium)
            SettingsDropdownItem(Icons.Default.Cloud, "Sync With", prefs.syncWith.value, cloudProviders) {
                viewModel.setSyncWith(SyncWith.fromValue(it))
            }
            SettingsDropdownItem(Icons.Default.Schedule, "Sync Frequency", prefs.syncFrequency.value, syncFrequencies) {
                viewModel.setSyncFrequency(SyncFrequency.fromValue(it))
            }
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // 🔹 Security
            Text("Security", style = MaterialTheme.typography.titleMedium)
            SettingsItem(Icons.Default.Lock, "Change Password",
                "Update your login password",{
                    // TODO: Implement password change
                    showToast("Coming Soon...")
                }
            )
            SettingsItem(Icons.Default.Fingerprint, "Biometric Login",
                "Use fingerprint or face unlock",{
                    // TODO: Implement biometric login
                    showToast("Coming Soon...")}
            )
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // 🔹 Notifications
            Text("Notifications", style = MaterialTheme.typography.titleMedium)
            SettingsToggleItem(Icons.Default.Notifications, "Push Notifications", prefs.pushNotifications) {
                viewModel.setPushNotifications(it)
            }
            SettingsToggleItem(Icons.Default.Email, "Email Alerts", prefs.emailAlerts) {
                viewModel.setEmailAlerts(it)
            }
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // 🔹 Premium
            Text("Premium", style = MaterialTheme.typography.titleMedium)
            SettingsItem(Icons.Default.Star, "Go Premium",
                "Unlock advanced features",{ navManager?.navigate(ScreenRoutes.Subscription.route)}
            )
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // 🔹 Support
            Text("Support", style = MaterialTheme.typography.titleMedium)
            SettingsItem(Icons.AutoMirrored.Filled.Chat, "Chat Support",
                "Talk to our support team instantly",{
                    // TODO: Implement chat support
                    showToast("Coming Soon...")})
            SettingsItem(Icons.Default.Email, "Email Support",
                "support@smartexpense.com",{
                    // TODO: Implement Email Support.
                    showToast("Coming Soon...")})
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // 🔹 About
            Text("About", style = MaterialTheme.typography.titleMedium)
            SettingsItem(Icons.Default.PrivacyTip, "Privacy & Terms", "Read our policies",{
                //TODO: Implement privacy & terms
                showToast("Coming Soon...")})
            SettingsItem(Icons.Default.Info, "About App", "Version 1.0.0",{
                // TODO: Implement about app
                showToast("Coming Soon...")})
            SettingsItem(Icons.Default.Share, "Share App", "Invite your friends",{
                // TODO: Implement share app
                showToast("Coming Soon...")})
            Spacer(modifier = Modifier.height(20.dp))


            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }

        if(loader) {
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
fun SettingsItem(icon: ImageVector, title: String, description: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 16.sp, style = MaterialTheme.typography.bodyLarge)
            Text(description, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun SettingsToggleItem(icon: ImageVector, title: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontSize = 16.sp, style = MaterialTheme.typography.bodyLarge)
        }
        Switch(checked = isChecked, onCheckedChange = onCheckedChange)
    }
}
@Composable
fun SettingsDropdownItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
            .padding(horizontal = 12.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, style = MaterialTheme.typography.bodyLarge)
            Text(selectedOption, fontSize = 13.sp, color = Color.Gray)
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
