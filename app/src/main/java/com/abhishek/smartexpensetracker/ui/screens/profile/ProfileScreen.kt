package com.abhishek.smartexpensetracker.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.abhishek.smartexpensetracker.core.datastore.ThemeType
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.ui.components.BaseScaffold
import com.abhishek.smartexpensetracker.ui.components.FinanceTopBar
import com.abhishek.smartexpensetracker.ui.screens.setting.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navManager: NavManager? = null,
    name: String,
    email: String,
    profileImage: String?,
    isPremium: Boolean,
    onEditProfile: () -> Unit,
    onEditBusinessDetails: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val prefs by settingsViewModel.userPreferences.collectAsState()
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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {
                // Top Row: Profile + Settings Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = profileImage ?: "https://via.placeholder.com/150",
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        }
                    }
                    IconButton(onClick = { navManager?.navigateSingleTop(ScreenRoutes.Settings.route) }) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Premium Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isPremium) Color(0xFF4CAF50) else Color(0xFF03A9F4))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            if (isPremium) "🌟 Premium User" else "Upgrade to Premium",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (!isPremium) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { navManager?.navigateSingleTop(ScreenRoutes.Subscription.route) },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Text("Buy Premium", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Toggles: Dark Mode + Business Mode
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
                            Switch(
                                checked = prefs.themeMode == ThemeType.DARK,
                                onCheckedChange = { checked ->
                                    settingsViewModel.setTheme(
                                        if (checked) ThemeType.DARK else ThemeType.LIGHT
                                    )
                                }
                            )
                        }
                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Business Mode", style = MaterialTheme.typography.bodyLarge)
                            Switch(
                                checked = prefs.isBusinessMode,
                                onCheckedChange = { enabled ->
                                    settingsViewModel.setBusinessMode(enabled)
                                }
                            )

                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Business Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Business Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Business Name: My Company Pvt Ltd", style = MaterialTheme.typography.bodyMedium)
                        Text("GST Number: 22AAAAA0000A1Z5", style = MaterialTheme.typography.bodyMedium)
                        Text("Business Email: support@company.com", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = onEditBusinessDetails,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text("Edit Business Details")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Edit Profile Button
                OutlinedButton(
                    onClick = onEditProfile,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Edit Personal Profile")
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
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}



@Preview
@Composable
private fun PreviewProfileScreen() {
    ProfileScreen(
        name = "Abhishek Chandra",
        email = "ac927920@gmail.com",
        profileImage = "https://via.placeholder.com/150",
        isPremium = false,
        onEditProfile = {},
        onEditBusinessDetails = {}
    )
}