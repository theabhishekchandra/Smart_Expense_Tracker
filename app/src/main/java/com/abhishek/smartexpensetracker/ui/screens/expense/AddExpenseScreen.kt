@file:OptIn(ExperimentalMaterial3Api::class)

package com.abhishek.smartexpensetracker.ui.screens.expense

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhishek.smartexpensetracker.data.model.UserRole
import com.abhishek.smartexpensetracker.ui.components.FinanceTopBar
import com.abhishek.smartexpensetracker.ui.components.ReceiptUploader

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    userRole: UserRole,
    isBusinessMode: Boolean
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var receiptUri by remember { mutableStateOf<String?>(null) }
    var staffAssigned by remember { mutableStateOf("") }
    var projectAssigned by remember { mutableStateOf("") }
    var assignToSelf by remember { mutableStateOf(true) }

    // New lender/borrower states
    var isLenderBorrower by remember { mutableStateOf(true) }
    var personName by remember { mutableStateOf("") }
    var lbAmount by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var reminderEnabled by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("Pending") }
    val statusOptions = listOf("Pending", "Paid", "Overdue")
    var isGiven by remember { mutableStateOf(true) }

    val categoryList = listOf("Staff", "Travel", "Food", "Utility", "Other")
    var expanded by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        receiptUri = uri?.toString()
    }

    Scaffold(
        topBar = {
            FinanceTopBar(
                "Add Expense",
                showBackButton = true,
                showSearch = false,
                showFilter = false,
                showNotifications = false,
                showMenu = false,
                onBackClick = {}
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier.padding(12.dp),
                icon = { Icon(imageVector = if (!isLenderBorrower) Icons.Default.Add else Icons.Default.Save, contentDescription = "Save Image") },
                text = { Text(text = if (!isLenderBorrower) "Add Expense" else "Save Record",) },
                onClick = {  }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                // Toggle normal expense or lender/borrower
                Row(
                    modifier = Modifier
                        .fillMaxWidth().padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = !isLenderBorrower,
                        onClick = { isLenderBorrower = false },
                        label = { Text("Normal Expense") }
                    )
                    FilterChip(
                        selected = isLenderBorrower,
                        onClick = { isLenderBorrower = true },
                        label = { Text("Lender / Borrower") }
                    )
                }
                Column(modifier = Modifier.padding(16.dp)) {

                    if (!isLenderBorrower) {
                        // ---------- Normal Expense Flow ----------
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Title") },
                            placeholder = { Text("Enter expense title") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount (₹)") },
                            placeholder = { Text("0.00") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = category,
                                onValueChange = {},
                                label = { Text("Category") },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                categoryList.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat) },
                                        onClick = {
                                            category = cat
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        if (category == "Other") {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = notes,
                                onValueChange = { if (it.length <= 100) notes = it },
                                label = { Text("Your Category") },
                                placeholder = { Text("Enter custom category") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { if (it.length <= 100) notes = it },
                            label = { Text("Notes (optional)") },
                            placeholder = { Text("Add notes (max 100 chars)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ReceiptUploader(
                            uri = receiptUri,
                            onUpload = { launcher.launch("image/*") },
                            onRemove = { receiptUri = null }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Business fields
                        if (isBusinessMode) {
                            OutlinedTextField(
                                value = projectAssigned,
                                onValueChange = { projectAssigned = it },
                                label = { Text("Project / Department (optional)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            if (userRole == UserRole.ADMIN) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Assign To:", style = MaterialTheme.typography.bodyLarge)
                                    Row {
                                        FilterChip(
                                            selected = assignToSelf,
                                            onClick = { assignToSelf = true },
                                            label = { Text("Self") }
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        FilterChip(
                                            selected = !assignToSelf,
                                            onClick = { assignToSelf = false },
                                            label = { Text("Staff") }
                                        )
                                    }
                                }

                                if (!assignToSelf) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedTextField(
                                        value = staffAssigned,
                                        onValueChange = { staffAssigned = it },
                                        label = { Text("Staff Name/ID") },
                                        placeholder = { Text("Enter staff to assign") },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    } else {
                        // ---------- Lender / Borrower Flow ----------



                        OutlinedTextField(
                            value = personName,
                            onValueChange = { personName = it },
                            label = { Text("Mobile Number") },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Contacts,
                                    contentDescription = "Phone Number")
                            },
                            placeholder = { Text("Select from contacts or enter manually") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))


                        OutlinedTextField(
                            value = personName,
                            onValueChange = { personName = it },
                            label = { Text("Person’s Name") },
                            placeholder = { Text("Select from contacts or enter manually") },
                            modifier = Modifier.fillMaxWidth()
                        )



                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = lbAmount,
                            onValueChange = { lbAmount = it },
                            label = { Text("Amount (₹)") },
                            placeholder = { Text("0.00") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = purpose,
                            onValueChange = { purpose = it },
                            label = { Text("Purpose / Notes") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = dueDate,
                            onValueChange = { dueDate = it },
                            label = { Text("Due Date") },
                            placeholder = { Text("DD/MM/YYYY") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Transaction Type Toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Transaction Type", fontSize = 16.sp)
                            AssistChip(
                                onClick = { isGiven = true },
                                label = { Text("Given") },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = if (isGiven) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                                )
                            )
                            AssistChip(
                                onClick = { isGiven = false },
                                label = { Text("Taken") },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = if (!isGiven) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        ExposedDropdownMenuBox(
                            expanded = expandedStatus,
                            onExpandedChange = { expandedStatus = !expandedStatus }
                        ) {
                            OutlinedTextField(
                                value = status,
                                onValueChange = {},
                                label = { Text("Status") },
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedStatus) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expandedStatus,
                                onDismissRequest = { expandedStatus = false }
                            ) {
                                statusOptions.forEach { st ->
                                    DropdownMenuItem(
                                        text = { Text(st) },
                                        onClick = {
                                            status = st
                                            expandedStatus = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = reminderEnabled,
                                onCheckedChange = { reminderEnabled = it }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Enable Reminder (WhatsApp / SMS / Notification)")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (!isLenderBorrower) {
                                if (title.isNotBlank() && amount.toDoubleOrNull() != null && amount.toDouble() > 0) {
                                    // TODO save normal expense
                                }
                            } else {
                                if (personName.isNotBlank() && lbAmount.toDoubleOrNull() != null) {
                                    // TODO save lender/borrower record
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = if (!isLenderBorrower) "Add Expense" else "Save Record",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddExpenseScreen() {
    AddExpenseScreen(userRole = UserRole.ADMIN, isBusinessMode = true)
}
