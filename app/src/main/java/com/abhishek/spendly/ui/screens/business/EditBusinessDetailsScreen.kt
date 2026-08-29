package com.abhishek.spendly.ui.screens.business

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.core.datastore.Currency
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.ui.components.FinanceTopBar
import com.abhishek.spendly.ui.components.LabeledTextField
import com.abhishek.spendly.ui.screens.profile.EditableAvatar
import com.abhishek.spendly.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBusinessDetailsScreen(
    navManager: NavManager? = null,
    currentBusinessName: String,
    currentOwnerName: String,
    currentBusinessLogo: String?,
    currentEmail: String,
    currentPhone: String,
    currentBusinessType: String = "Retail",
    currentCurrency: String = Currency.RUPEE.value,
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
    var businessType by remember { mutableStateOf(currentBusinessType.ifBlank { "Retail" }) }
    var currency by remember { mutableStateOf(currentCurrency.ifBlank { Currency.RUPEE.value }) }

    var businessTypeExpanded by remember { mutableStateOf(false) }
    var currencyExpanded by remember { mutableStateOf(false) }

    val businessTypes = listOf("Retail", "Restaurant", "Service", "Freelancer", "Other")
    val currencyOptions = Currency.entries.map { it.value }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { logoUrl = it.toString() } }

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
                shape = MaterialTheme.shapes.extraLarge,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                text = { Text("Save", style = MaterialTheme.typography.labelLarge) },
                icon = { Icon(Icons.Default.Save, contentDescription = null) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                .padding(padding)
                .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.md)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditableAvatar(
                imageUrl = logoUrl,
                contentDescription = "Business logo",
                fallbackIcon = Icons.Default.Store,
                onEditClick = { imagePickerLauncher.launch("image/*") }
            )

            Spacer(Modifier.height(AppSpacing.lg))

            // Card container for form
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppSpacing.md),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
                ) {
                    LabeledTextField(
                        label = "Business Name",
                        value = businessName,
                        onValueChange = { businessName = it }
                    )

                    LabeledTextField(
                        label = "Owner Name",
                        value = ownerName,
                        onValueChange = { ownerName = it }
                    )

                    LabeledTextField(
                        label = "Business Email",
                        value = email,
                        onValueChange = { email = it },
                        keyboardType = KeyboardType.Email
                    )

                    LabeledTextField(
                        label = "Business Phone",
                        value = phone,
                        onValueChange = { phone = it },
                        keyboardType = KeyboardType.Phone
                    )

                    // Business Type Dropdown
                    ExposedDropdownMenuBox(
                        expanded = businessTypeExpanded,
                        onExpandedChange = { businessTypeExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = businessType,
                            onValueChange = {},
                            label = { Text("Business Type") },
                            readOnly = true,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = businessTypeExpanded,
                            onDismissRequest = { businessTypeExpanded = false }
                        ) {
                            businessTypes.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        businessType = option
                                        businessTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Currency Dropdown
                    ExposedDropdownMenuBox(
                        expanded = currencyExpanded,
                        onExpandedChange = { currencyExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = currency,
                            onValueChange = {},
                            label = { Text("Preferred Currency") },
                            readOnly = true,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = currencyExpanded,
                            onDismissRequest = { currencyExpanded = false }
                        ) {
                            currencyOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        currency = option
                                        currencyExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(AppSpacing.xxl + AppSpacing.lg)) // leave space for FAB
        }
    }
}

@Preview
@Composable
private fun PreviewEditBusinessDetailsScreen() {
    EditBusinessDetailsScreen(
        currentBusinessName = "Smart Traders",
        currentOwnerName = "Abhishek Chandra",
        currentBusinessLogo = null,
        currentEmail = "business@example.com",
        currentPhone = "+91 9876543210",
        onSave = { _, _, _, _, _, _, _ -> },
        onCancel = {}
    )
}
