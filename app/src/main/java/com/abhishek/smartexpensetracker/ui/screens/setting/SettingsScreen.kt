package com.abhishek.smartexpensetracker.ui.screens.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhishek.smartexpensetracker.core.navigation.NavManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    navManager: NavManager? = null
) {
    var isDarkMode by remember { mutableStateOf(false) }
    var isNotificationEnabled by remember { mutableStateOf(true) }

    // Dropdown selections
    var selectedLanguage by remember { mutableStateOf("English") }
    var selectedCurrency by remember { mutableStateOf("INR (₹)") }
    var selectedExportFormat by remember { mutableStateOf("PDF") }
    var selectedCloud by remember { mutableStateOf("Google Drive") }
    var selectedSyncFrequency by remember { mutableStateOf("Weekly") }

    val languages = listOf("English", "Hindi", "Spanish", "French")
    val currencies = listOf("INR (₹)", "USD ($)", "EUR (€)", "GBP (£)")
    val exportFormats = listOf("PDF", "Excel", "CSV")
    val cloudProviders = listOf("Google Drive", "OneDrive", "App Server")
    val syncFrequencies = listOf("Daily", "Weekly", "Monthly")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 5.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // 🔹 Account Section
            Text("Account", style = MaterialTheme.typography.titleMedium)
            SettingsItem(Icons.Default.Person, "Edit Profile", "Change your name, email & picture")
            SettingsItem(Icons.Default.Business, "Business Details", "Edit company name, GST, PAN, etc.")
            Divider()

            // 🔹 App Preferences
            Text("App Preferences", style = MaterialTheme.typography.titleMedium)
            SettingsDropdownItem(Icons.Default.Translate, "Language", selectedLanguage, languages) { selectedLanguage = it }
            SettingsDropdownItem(Icons.Default.CurrencyRupee, "Currency", selectedCurrency, currencies) { selectedCurrency = it }
            Divider()

            // 🔹 Reports
            Text("Reports", style = MaterialTheme.typography.titleMedium)
            SettingsDropdownItem(Icons.Default.Description, "Export Format", selectedExportFormat, exportFormats) { selectedExportFormat = it }
            Divider()

            // 🔹 Cloud Sync
            Text("Cloud Sync", style = MaterialTheme.typography.titleMedium)
            SettingsDropdownItem(Icons.Default.Cloud, "Sync With", selectedCloud, cloudProviders) { selectedCloud = it }
            SettingsDropdownItem(Icons.Default.Schedule, "Sync Frequency", selectedSyncFrequency, syncFrequencies) { selectedSyncFrequency = it }
            Divider()

            // 🔹 Security
            Text("Security", style = MaterialTheme.typography.titleMedium)
            SettingsItem(Icons.Default.Lock, "Change Password", "Update your login password")
            SettingsItem(Icons.Default.Fingerprint, "Biometric Login", "Use fingerprint or face unlock")
            Divider()

            // 🔹 Notifications
            Text("Notifications", style = MaterialTheme.typography.titleMedium)
            SettingsToggleItem(Icons.Default.Notifications, "Push Notifications", isNotificationEnabled) { isNotificationEnabled = it }
            SettingsItem(Icons.Default.Email, "Email Alerts", "Monthly expense reports")
            Divider()

            // 🔹 Premium
            Text("Premium", style = MaterialTheme.typography.titleMedium)
            SettingsItem(Icons.Default.Star, "Go Premium", "Unlock advanced features")
            Divider()

            // 🔹 Support
            Text("Support", style = MaterialTheme.typography.titleMedium)
            SettingsItem(Icons.Default.Chat, "Chat Support", "Talk to our support team instantly")
            SettingsItem(Icons.Default.Email, "Email Support", "support@smartexpense.com")
            Divider()

            // 🔹 About
            Text("About", style = MaterialTheme.typography.titleMedium)
            SettingsItem(Icons.Default.Info, "About App", "Version 1.0.0")
            SettingsItem(Icons.Default.Share, "Share App", "Invite your friends")
            SettingsItem(Icons.Default.PrivacyTip, "Privacy & Terms", "Read our policies")
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SettingsItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
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
fun SettingsToggleItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
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

    /*
    * TODO : Add Language , Currency, Export Format, Sync With, Sync Frequency, Biometric Login, Push Notifications, Email Alerts*/

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
        onBackClick = {},
    )
}
