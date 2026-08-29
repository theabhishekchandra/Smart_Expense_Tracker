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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.widget.Toast
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.data.model.ExpenseDM
import com.abhishek.smartexpensetracker.data.model.UserRole
import com.abhishek.smartexpensetracker.ui.components.AppButton
import com.abhishek.smartexpensetracker.ui.components.FinanceTopBar
import com.abhishek.smartexpensetracker.ui.components.ReceiptUploader
import com.abhishek.smartexpensetracker.ui.screens.login.viewmodel.ExpenseViewModel
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    userRole: UserRole,
    isBusinessMode: Boolean,
    navManager: NavManager? = null,
    viewModel: ExpenseViewModel = hiltViewModel()
) {
    val context = LocalContext.current
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
    var personPhone by remember { mutableStateOf("") }
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

    fun onSaveClick() {
        if (!isLenderBorrower) {
            if (title.isNotBlank() && amount.toDoubleOrNull() != null && amount.toDouble() > 0) {
                viewModel.addExpense(
                    ExpenseDM(
                        title = title,
                        amount = amount.toDouble(),
                        category = category,
                        notes = notes.ifBlank { null },
                        receiptUri = receiptUri?.let { android.net.Uri.parse(it) }
                    )
                ) {
                    Toast.makeText(context, "Expense saved", Toast.LENGTH_SHORT).show()
                    navManager?.navigateBack()
                }
            } else {
                Toast.makeText(context, "Enter a title and a valid amount", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (personName.isNotBlank() && lbAmount.toDoubleOrNull() != null && lbAmount.toDouble() > 0) {
                val dueDateMillis = runCatching {
                    java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                        .apply { isLenient = false }
                        .parse(dueDate)?.time
                }.getOrNull()
                viewModel.addLendingRecord(
                    personName = personName,
                    phone = personPhone,
                    amount = lbAmount.toDouble(),
                    purpose = purpose,
                    isGiven = isGiven,
                    dueDateMillis = dueDateMillis
                ) {
                    Toast.makeText(context, "Record saved", Toast.LENGTH_SHORT).show()
                    navManager?.navigateBack()
                }
            } else {
                Toast.makeText(context, "Enter a name and a valid amount", Toast.LENGTH_SHORT).show()
            }
        }
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
                onBackClick = { navManager?.navigateBack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(AppSpacing.md)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(AppSpacing.sm))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                // Toggle normal expense or lender/borrower
                Row(
                    modifier = Modifier
                        .fillMaxWidth().padding(top = AppSpacing.sm),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = !isLenderBorrower,
                        onClick = { isLenderBorrower = false },
                        label = { Text("Normal Expense", style = MaterialTheme.typography.labelLarge) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    FilterChip(
                        selected = isLenderBorrower,
                        onClick = { isLenderBorrower = true },
                        label = { Text("Lender / Borrower", style = MaterialTheme.typography.labelLarge) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
                Column(modifier = Modifier.padding(AppSpacing.md)) {

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

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount (₹)") },
                            placeholder = { Text("0.00") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

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
                            Spacer(modifier = Modifier.height(AppSpacing.sm))
                            OutlinedTextField(
                                value = notes,
                                onValueChange = { if (it.length <= 100) notes = it },
                                label = { Text("Your Category") },
                                placeholder = { Text("Enter custom category") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

                        OutlinedTextField(
                            value = notes,
                            onValueChange = { if (it.length <= 100) notes = it },
                            label = { Text("Notes (optional)") },
                            placeholder = { Text("Add notes (max 100 chars)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

                        ReceiptUploader(
                            uri = receiptUri,
                            onUpload = { launcher.launch("image/*") },
                            onRemove = { receiptUri = null }
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

                        // Business fields
                        if (isBusinessMode) {
                            OutlinedTextField(
                                value = projectAssigned,
                                onValueChange = { projectAssigned = it },
                                label = { Text("Project / Department (optional)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(AppSpacing.sm))

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
                                        Spacer(modifier = Modifier.width(AppSpacing.sm))
                                        FilterChip(
                                            selected = !assignToSelf,
                                            onClick = { assignToSelf = false },
                                            label = { Text("Staff") }
                                        )
                                    }
                                }

                                if (!assignToSelf) {
                                    Spacer(modifier = Modifier.height(AppSpacing.sm))
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
                            value = personPhone,
                            onValueChange = { personPhone = it },
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

                        Spacer(modifier = Modifier.height(AppSpacing.sm))


                        OutlinedTextField(
                            value = personName,
                            onValueChange = { personName = it },
                            label = { Text("Person’s Name") },
                            placeholder = { Text("Select from contacts or enter manually") },
                            modifier = Modifier.fillMaxWidth()
                        )



                        Spacer(modifier = Modifier.height(AppSpacing.sm))

                        OutlinedTextField(
                            value = lbAmount,
                            onValueChange = { lbAmount = it },
                            label = { Text("Amount (₹)") },
                            placeholder = { Text("0.00") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

                        OutlinedTextField(
                            value = purpose,
                            onValueChange = { purpose = it },
                            label = { Text("Purpose / Notes") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

                        OutlinedTextField(
                            value = dueDate,
                            onValueChange = { dueDate = it },
                            label = { Text("Due Date") },
                            placeholder = { Text("DD/MM/YYYY") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

                        // Transaction Type Toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                        ) {
                            Text("Transaction Type", style = MaterialTheme.typography.titleSmall)
                            AssistChip(
                                onClick = { isGiven = true },
                                label = { Text("Given") },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = if (isGiven) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    labelColor = if (isGiven) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            AssistChip(
                                onClick = { isGiven = false },
                                label = { Text("Taken") },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = if (!isGiven) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    labelColor = if (!isGiven) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

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

                        Spacer(modifier = Modifier.height(AppSpacing.sm))

                        Row(
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = reminderEnabled,
                                onCheckedChange = { reminderEnabled = it }
                            )
                            Spacer(modifier = Modifier.width(AppSpacing.sm))
                            Text("Enable Reminder (WhatsApp / SMS / Notification)", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.md))

                    AppButton(
                        text = if (!isLenderBorrower) "Add Expense" else "Save Record",
                        onClick = { onSaveClick() },
                        modifier = Modifier.fillMaxWidth()
                    )
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
