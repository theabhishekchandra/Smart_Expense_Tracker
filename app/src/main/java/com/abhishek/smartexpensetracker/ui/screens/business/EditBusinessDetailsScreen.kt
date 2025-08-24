package com.abhishek.smartexpensetracker.ui.screens.business

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.ui.components.FinanceTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBusinessDetailsScreen(
    navManager: NavManager? = null,
    currentBusinessName: String,
    currentOwnerName: String,
    currentBusinessLogo: String?,
    currentEmail: String,
    currentPhone: String,
    onSave: (
        String, String, String?, String, String, String, String
    ) -> Unit,
    onCancel: () -> Unit
) {
    var businessName by remember { mutableStateOf(currentBusinessName) }
    var ownerName by remember { mutableStateOf(currentOwnerName) }
    var logoUrl by remember { mutableStateOf(currentBusinessLogo ?: "") }
    var email by remember { mutableStateOf(currentEmail) }
    var phone by remember { mutableStateOf(currentPhone) }
    var businessType by remember { mutableStateOf("Retail") }
    var currency by remember { mutableStateOf("INR") }

    val businessTypes = listOf("Retail", "Restaurant", "Service", "Freelancer", "Other")
    val currencyOptions = listOf("INR", "USD", "EUR", "GBP")

    Scaffold(
        topBar = {
            FinanceTopBar(
                title = "Edit Business Details",
                showBackButton = true,
                onBackClick = onCancel,
                showSearch = false,
                showFilter = false,
                showNotifications = false,
                showMenu = false
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    onSave(
                        businessName, ownerName, logoUrl.takeIf { it.isNotBlank() },
                        email, phone, businessType, currency
                    )
                },
                shape = RoundedCornerShape(50),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                text = { Text("Save", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Save, contentDescription = "Save") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Business Logo with overlay edit icon
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = if (logoUrl.isNotBlank()) logoUrl else "https://via.placeholder.com/150",
                    contentDescription = "Business Logo",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .shadow(6.dp, CircleShape)
                )
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change Logo",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(6.dp)
                        .clickable { /* TODO: Open image picker */ },
                    tint = Color.White
                )
            }

            Spacer(Modifier.height(24.dp))

            // Card container for form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text("Business Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = ownerName,
                        onValueChange = { ownerName = it },
                        label = { Text("Owner Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Business Email") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Business Phone") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Business Type Dropdown
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = { /* TODO: Add expansion state */ }
                    ) {
                        OutlinedTextField(
                            value = businessType,
                            onValueChange = {},
                            label = { Text("Business Type") },
                            readOnly = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = false,
                            onDismissRequest = { }
                        ) {
                            businessTypes.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = { businessType = option }
                                )
                            }
                        }
                    }

                    // Currency Dropdown
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = { /* TODO: Add expansion state */ }
                    ) {
                        OutlinedTextField(
                            value = currency,
                            onValueChange = {},
                            label = { Text("Preferred Currency") },
                            readOnly = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = false,
                            onDismissRequest = { }
                        ) {
                            currencyOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = { currency = option }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(100.dp)) // leave space for FAB
        }
    }
}

@Preview
@Composable
private fun PreviewEditBusinessDetailsScreen() {
    EditBusinessDetailsScreen(
        currentBusinessName = "Smart Traders",
        currentOwnerName = "Abhishek Chandra",
        currentBusinessLogo = "https://via.placeholder.com/150",
        currentEmail = "business@example.com",
        currentPhone = "+91 9876543210",
        onSave = { _, _, _, _, _, _, _ -> },
        onCancel = {}
    )
}
