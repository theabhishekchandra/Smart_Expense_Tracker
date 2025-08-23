package com.abhishek.smartexpensetracker.ui.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.tooling.preview.Preview

// --- Staff Model ---
data class Staff(
    val id: Int,
    val staffId: String,
    val name: String,
    var email: String,
    var totalExpense: Double,
    var role: Role
)

enum class Role {
    Admin, Approver, EntryOnly, Viewer
}

// --- Sample Staff List ---
val sampleStaffList = mutableStateListOf(
    Staff(1, "Staff002", "John Doe", "john@example.com", 1200.0, Role.EntryOnly),
    Staff(2, "Staff022", "Jane Smith", "jane@example.com", 2500.0, Role.Approver),
    Staff(3, "Staff042", "Alice Lee", "alice@example.com", 800.0, Role.Viewer)
)

// --- Staff Management Screen ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffManagementScreen() {
    var showAddDialog by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Staff Management") },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Staff")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(sampleStaffList, key = { it.id }) { staff ->
                    ModernStaffCard(staff)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (showAddDialog) {
                StaffDialog(
                    onDismiss = { showAddDialog = false },
                    staffToEdit = Staff(
                        id = 0,
                        staffId = "Staff32",
                        name = "Abhishek Chandra",
                        email = "ac927920@gmail.com",
                        totalExpense = 14342.0,
                        role = Role.EntryOnly
                    ),
                    onSave = { }
                )
            }
        }
    }
}

// --- Staff Card ---
@Composable
fun ModernStaffCard(staff: Staff) {
    var expandedDropdown by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Profile Avatar ---
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        RoundedCornerShape(25.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = staff.name.split(" ").map { it.first() }.joinToString(""),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // --- Staff Info ---
            Column(modifier = Modifier.weight(1f)) {
                Text(text = staff.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = staff.email, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Staff ID Badge
                    Text(
                        text = staff.staffId,
                        modifier = Modifier
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    // Total Expenses
                    Text(
                        text = "₹${staff.totalExpense}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // --- Role Dropdown ---
            Box {
                Text(
                    text = staff.role.name,
                    color = when (staff.role) {
                        Role.Admin -> Color.Red
                        Role.Approver -> Color.Blue
                        Role.EntryOnly -> Color(0xFF4CAF50)
                        Role.Viewer -> Color.Gray
                    },
                    modifier = Modifier
                        .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable { expandedDropdown = true }
                )

                DropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    Role.values().forEach { role ->
                        DropdownMenuItem(onClick = {
                            staff.role = role
                            expandedDropdown = false
                        }) {
                            Text(text = role.name)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // --- Edit / Delete Icons ---
            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = { /* TODO: Edit functionality */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Staff")
                }
                IconButton(onClick = { sampleStaffList.remove(staff) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Staff", tint = Color.Red)
                }
            }
        }
    }
}


// --- Add Staff Dialog ---
@Composable
fun StaffDialog(
    staffToEdit: Staff? = null, // null -> Add new, not null -> Update existing
    onDismiss: () -> Unit,
    onSave: (Staff) -> Unit
) {
    var name by remember { mutableStateOf(staffToEdit?.name ?: "") }
    var email by remember { mutableStateOf(staffToEdit?.email ?: "") }
    var staffId by remember { mutableStateOf(staffToEdit?.staffId ?: "Staff${(sampleStaffList.size + 1).toString().padStart(3, '0')}") }
    var selectedRole by remember { mutableStateOf(staffToEdit?.role ?: Role.EntryOnly) }


    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = if (staffToEdit == null) "Add New Staff" else "Update Staff",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Name TextField
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Email TextField
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Staff ID display
                OutlinedTextField(
                    value = staffId,
                    onValueChange = { staffId = it },
                    label = { Text("Staff ID") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Role Dropdown
                var expanded by remember { mutableStateOf(false) }
                Box {
                    Text(
                        text = "Role: ${selectedRole.name}",
                        color = when (selectedRole) {
                            Role.Admin -> Color.Red
                            Role.Approver -> Color.Blue
                            Role.EntryOnly -> Color(0xFF4CAF50)
                            Role.Viewer -> Color.Gray
                        },
                        modifier = Modifier
                            .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .clickable { expanded = true }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Role.entries.forEach { role ->
                            DropdownMenuItem(onClick = {
                                selectedRole = role
                                expanded = false
                            }) {
                                Text(
                                    text = role.name,
                                    color = when (role) {
                                        Role.Admin -> Color.Red
                                        Role.Approver -> Color.Blue
                                        Role.EntryOnly -> Color(0xFF4CAF50)
                                        Role.Viewer -> Color.Gray
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = {
                        if (name.isNotBlank() && email.isNotBlank()) {
                            val newStaff = Staff(
                                id = staffToEdit?.id ?: ((sampleStaffList.maxOfOrNull { it.id }
                                    ?: 0) + 1),
                                staffId = staffId,
                                name = name,
                                email = email,
                                totalExpense = staffToEdit?.totalExpense ?: 0.0,
                                role = selectedRole
                            )
                            onSave(newStaff)
                            onDismiss()
                        }
                    }) {
                        Text(if (staffToEdit == null) "Add Staff" else "Update Staff")
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewStaffManagementScreen() {
    StaffManagementScreen()
//    StaffDialog(
//        staffToEdit = Staff(
//            id = 1,
//            staffId = "Staff002",
//            name = "John Doe",
//            email = "john@example.com",
//            totalExpense = 1200.0,
//            role = Role.EntryOnly
//        ),
//        onDismiss = { },
//        onSave = { }
//    )
}
