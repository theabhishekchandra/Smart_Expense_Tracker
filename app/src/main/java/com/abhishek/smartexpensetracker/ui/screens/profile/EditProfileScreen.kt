package com.abhishek.smartexpensetracker.ui.screens.profile

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
            // Profile Image with overlay edit icon
            Box(contentAlignment = Alignment.BottomEnd) {
                AsyncImage(
                    model = if (profileUrl.isNotBlank()) profileUrl else "https://via.placeholder.com/150",
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .shadow(6.dp, CircleShape)
                )
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Change Picture",
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
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = dob,
                        onValueChange = { dob = it },
                        label = { Text("Date of Birth (DD/MM/YYYY)") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Gender Dropdown
                    ExposedDropdownMenuBox(
                        expanded = false,
                        onExpandedChange = { /* TODO: Add expansion state */ }
                    ) {
                        OutlinedTextField(
                            value = gender,
                            onValueChange = {},
                            label = { Text("Gender") },
                            readOnly = true,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = false,
                            onDismissRequest = { }
                        ) {
                            genderOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = { gender = option }
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

//                    OutlinedTextField(
//                        value = profileUrl,
//                        onValueChange = { profileUrl = it },
//                        label = { Text("Profile Image URL") },
//                        singleLine = true,
//                        shape = RoundedCornerShape(16.dp),
//                        modifier = Modifier.fillMaxWidth()
//                    )
                }
            }

            Spacer(Modifier.height(100.dp)) // leave space for FAB
        }
    }
}

@Preview
@Composable
private fun PreviewEditProfileScreen() {
    EditProfileScreen(
        currentName = "Abhishek Chandra",
        currentEmail = "ac927920@gmail.com",
        currentProfileImage = "https://via.placeholder.com/150",
        onSave = { _, _, _, _, _, _, _ -> },
        onCancel = {}
    )
}
