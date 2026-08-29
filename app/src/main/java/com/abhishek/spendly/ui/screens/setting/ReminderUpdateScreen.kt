package com.abhishek.spendly.ui.screens.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderUpdateScreen(
    onBack: () -> Unit = {}
) {
    var selectedPerson by remember { mutableStateOf("Select Person") }
    var amount by remember { mutableStateOf("") }
    var messageType by remember { mutableStateOf("Reminder") }
    var showPersonPicker by remember { mutableStateOf(false) }

    val messageText = when (messageType) {
        "Reminder" -> "Reminder: You have to pay ₹$amount"
        else -> "Update: I’ve given extra money ₹$amount"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reminder & Update", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(AppSpacing.md)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
        ) {

            // Select Person
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPersonPicker = true },
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier.padding(AppSpacing.md),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(AppSpacing.sm + AppSpacing.xs))
                    Text(selectedPerson, style = MaterialTheme.typography.bodyLarge)
                }
            }

            // Amount Field
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Enter Amount") },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            )

            // Message Type Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    selected = messageType == "Reminder",
                    onClick = { messageType = "Reminder" },
                    label = { Text("Reminder") }
                )
                FilterChip(
                    selected = messageType == "Update",
                    onClick = { messageType = "Update" },
                    label = { Text("Update") }
                )
            }

            // Message Preview
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(AppSpacing.md)) {
                    Text("Preview Message:", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(AppSpacing.sm))
                    Text(messageText, style = MaterialTheme.typography.bodyLarge)
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* TODO: Send via WhatsApp */ }, shape = MaterialTheme.shapes.large) {
                    Text("Send WhatsApp", style = MaterialTheme.typography.labelLarge)
                }
                Button(onClick = { /* TODO: Send via SMS */ }, shape = MaterialTheme.shapes.large) {
                    Text("Send SMS", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }

    // Person Picker (Bottom Sheet style list)
    if (showPersonPicker) {
        AlertDialog(
            onDismissRequest = { showPersonPicker = false },
            title = { Text("Select Person") },
            text = {
                LazyColumn {
                    items(listOf("Amit", "Neha", "Rahul", "Priya")) { person ->
                        Text(
                            text = person,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppSpacing.sm + AppSpacing.xs)
                                .clickable {
                                    selectedPerson = person
                                    showPersonPicker = false
                                }
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewReminderUpdateScreen() {
    ReminderUpdateScreen()

}
