package com.abhishek.smartexpensetracker.ui.screens.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
                title = { Text("Reminder & Update", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Select Person
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPersonPicker = true },
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Person, contentDescription = "Person", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(selectedPerson, fontSize = 16.sp)
                }
            }

            // Amount Field
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Enter Amount") },
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
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F6F6))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Preview Message:", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(messageText, fontSize = 16.sp)
                }
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* TODO: Send via WhatsApp */ }) {
                    Text("Send WhatsApp")
                }
                Button(onClick = { /* TODO: Send via SMS */ }) {
                    Text("Send SMS")
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
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