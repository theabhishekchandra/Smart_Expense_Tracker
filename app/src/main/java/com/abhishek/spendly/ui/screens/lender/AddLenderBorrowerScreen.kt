package com.abhishek.spendly.ui.screens.lender

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.ui.theme.AppShapes
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.heroGradientVertical
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLenderBorrowerScreen(
    currentName: String = "",
    currentMobile: String = "",
    currentAmount: String = "",
    currentIsGiven: Boolean = true,
    currentDueDate: String = "",
    currentNotes: String = "",
    onSave: (String, String, String, Boolean, String, String) -> Unit,
    onCancel: () -> Unit
) {
    val isEditMode = currentName.isNotBlank()
    var name by remember { mutableStateOf(currentName) }
    var mobile by remember { mutableStateOf(currentMobile) }
    var amount by remember { mutableStateOf(currentAmount) }
    var isGiven by remember { mutableStateOf(currentIsGiven) }
    var dueDate by remember { mutableStateOf(currentDueDate) }
    var notes by remember { mutableStateOf(currentNotes) }

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onCancel() },
                    shape = AppShapes.small,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        onSave(name, mobile, amount, isGiven, dueDate, notes)
                    },
                    shape = AppShapes.small,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isEditMode) "Update" else "Save")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Gradient hero header - replaces the generic TopAppBar, matching the
            // vibrant-gradient-fintech hero pattern used on Login.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        heroGradientVertical(),
                        RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(AppSpacing.lg)
            ) {
                Column {
                    IconButton(onClick = onCancel, modifier = Modifier.size(48.dp)) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancel",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(AppSpacing.sm))
                    Text(
                        text = if (isEditMode) "Edit Lender / Borrower" else "Add Lender / Borrower",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Track money you give or take",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(AppSpacing.md),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
            ) {

                // Person's Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Person's Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Mobile Number
                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text("Mobile Number (optional)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                // Amount
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Transaction Type Toggle
                Column {
                    Text("Transaction Type", style = MaterialTheme.typography.titleSmall)
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                    ) {
                        FilterChip(
                            selected = isGiven,
                            onClick = { isGiven = true },
                            label = { Text("Given") }
                        )
                        FilterChip(
                            selected = !isGiven,
                            onClick = { isGiven = false },
                            label = { Text("Taken") }
                        )
                    }
                }

                // Due Date (date picker simplified as text input for now)
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date (dd MMM yyyy)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    maxLines = 4
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewAddLenderBorrowerScreen() {
    AddLenderBorrowerScreen(
        onSave = { name, mobile, amount, isGiven, dueDate, notes ->
            // Handle save logic here
        },
        onCancel = {}
    )
}
