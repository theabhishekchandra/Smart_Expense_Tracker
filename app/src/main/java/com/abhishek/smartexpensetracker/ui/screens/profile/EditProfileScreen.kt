package com.abhishek.smartexpensetracker.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.ui.components.FinanceTopBar
import com.abhishek.smartexpensetracker.ui.components.LabeledTextField
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navManager: NavManager? = null,
    currentName: String,
    currentEmail: String,
    currentProfileImage: String?,
    onSave: (String, String, String?, String, String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var profileUrl by remember { mutableStateOf(currentProfileImage ?: "") }
    var phone by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var currency by remember { mutableStateOf("USD") }

    var genderExpanded by remember { mutableStateOf(false) }
    var currencyExpanded by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female", "Other")
    val currencyOptions = listOf("USD", "INR", "EUR", "GBP")

    Scaffold(
        topBar = {
            FinanceTopBar(
                title = "Edit Profile",
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
                        name, email, profileUrl.takeIf { it.isNotBlank() },
                        phone, dob, gender, currency
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
                imageUrl = profileUrl,
                contentDescription = "Profile picture",
                fallbackIcon = Icons.Default.Person,
                onEditClick = { /* TODO: Open image picker */ }
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
                        label = "Full Name",
                        value = name,
                        onValueChange = { name = it }
                    )

                    LabeledTextField(
                        label = "Email Address",
                        value = email,
                        onValueChange = { email = it },
                        keyboardType = KeyboardType.Email
                    )

                    LabeledTextField(
                        label = "Phone Number",
                        value = phone,
                        onValueChange = { phone = it },
                        keyboardType = KeyboardType.Phone
                    )

                    LabeledTextField(
                        label = "Date of Birth (DD/MM/YYYY)",
                        value = dob,
                        onValueChange = { dob = it }
                    )

                    // Gender Dropdown
                    ExposedDropdownMenuBox(
                        expanded = genderExpanded,
                        onExpandedChange = { genderExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            label = { Text("Gender") },
                            readOnly = true,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = genderExpanded,
                            onDismissRequest = { genderExpanded = false }
                        ) {
                            genderOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        gender = option
                                        genderExpanded = false
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

/**
 * Circular avatar with an edit affordance overlay. Shared by [EditProfileScreen] and
 * [com.abhishek.smartexpensetracker.ui.screens.business.EditBusinessDetailsScreen] to avoid
 * duplicating the avatar/logo editing layout in both near-identical forms.
 *
 * Falls back to a themed icon inside a tinted circle (instead of fetching a network placeholder
 * image) whenever [imageUrl] is null/blank.
 */
@Composable
fun EditableAvatar(
    imageUrl: String,
    contentDescription: String,
    fallbackIcon: ImageVector,
    onEditClick: () -> Unit
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        if (imageUrl.isNotBlank()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .shadow(6.dp, CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .shadow(6.dp, CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = fallbackIcon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Change picture",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
private fun PreviewEditProfileScreen() {
    EditProfileScreen(
        currentName = "Abhishek Chandra",
        currentEmail = "ac927920@gmail.com",
        currentProfileImage = null,
        onSave = { _, _, _, _, _, _, _ -> },
        onCancel = {}
    )
}
