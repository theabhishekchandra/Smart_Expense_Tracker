package com.abhishek.spendly.ui.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GroupOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.tooling.preview.Preview
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.ui.theme.AppShapes
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.RoleAdmin
import com.abhishek.spendly.ui.theme.RoleApprover
import com.abhishek.spendly.ui.theme.RoleEntry
import com.abhishek.spendly.ui.theme.RoleViewer

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

/** Semantic color for a staff [Role], shared by the staff screens so badges stay consistent. */
fun roleColor(role: Role): Color = when (role) {
    Role.Admin -> RoleAdmin
    Role.Approver -> RoleApprover
    Role.EntryOnly -> RoleEntry
    Role.Viewer -> RoleViewer
}

/** Small rounded role chip shared across the staff screens. */
@Composable
fun RoleBadge(role: Role, modifier: Modifier = Modifier) {
    val color = roleColor(role)
    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.15f), AppShapes.small)
            .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
    ) {
        Text(
            text = role.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
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
fun StaffManagementScreen(navManager: NavManager? = null) {
    var showAddDialog by remember { mutableStateOf(false) }
    var staffBeingEdited by remember { mutableStateOf<Staff?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Staff Management") },
                navigationIcon = {
                    if (navManager != null) {
                        IconButton(onClick = { navManager.navigateBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Staff")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (sampleStaffList.isEmpty()) {
                EmptyStaffState()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(AppSpacing.sm),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    items(sampleStaffList, key = { it.id }) { staff ->
                        ModernStaffCard(staff, onEditClick = { staffBeingEdited = staff })
                    }
                }
            }

            if (showAddDialog) {
                StaffDialog(
                    onDismiss = { showAddDialog = false },
                    staffToEdit = null,
                    onSave = { newStaff -> sampleStaffList.add(newStaff) }
                )
            }

            staffBeingEdited?.let { staff ->
                StaffDialog(
                    onDismiss = { staffBeingEdited = null },
                    staffToEdit = staff,
                    onSave = { updated ->
                        val index = sampleStaffList.indexOfFirst { it.id == updated.id }
                        if (index != -1) sampleStaffList[index] = updated
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyStaffState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.GroupOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(
                text = "No staff added yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// --- Staff Card ---
@Composable
fun ModernStaffCard(staff: Staff, onEditClick: () -> Unit = {}) {
    var expandedDropdown by remember { mutableStateOf(false) }

    Card(
        shape = AppShapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.md),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Profile Avatar ---
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        AppShapes.extraLarge
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = staff.name.split(" ").map { it.first() }.joinToString(""),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(AppSpacing.sm))

            // --- Staff Info ---
            Column(modifier = Modifier.weight(1f)) {
                Text(text = staff.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = staff.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(AppSpacing.xs))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Staff ID Badge
                    Text(
                        text = staff.staffId,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, AppShapes.extraSmall)
                            .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.sm))

                    // Total Expenses
                    Text(
                        text = "₹${staff.totalExpense}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(AppSpacing.sm))

            // --- Role Dropdown ---
            Box {
                RoleBadge(
                    role = staff.role,
                    modifier = Modifier.clickable { expandedDropdown = true }
                )

                DropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    Role.entries.forEach { role ->
                        DropdownMenuItem(
                            text = { Text(text = role.name) },
                            onClick = {
                                staff.role = role
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(AppSpacing.xs))

            // --- Edit / Delete Icons ---
            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit ${staff.name}")
                }
                IconButton(onClick = { sampleStaffList.remove(staff) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete ${staff.name}",
                        tint = MaterialTheme.colorScheme.error
                    )
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
            shape = AppShapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.md)
        ) {
            Column(modifier = Modifier.padding(AppSpacing.lg)) {
                Text(
                    text = if (staffToEdit == null) "Add New Staff" else "Update Staff",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(AppSpacing.md))

                // Name TextField
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))

                // Email TextField
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(AppSpacing.sm))

                // Staff ID display
                OutlinedTextField(
                    value = staffId,
                    onValueChange = { staffId = it },
                    label = { Text("Staff ID") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(AppSpacing.sm))

                // Role Dropdown
                var expanded by remember { mutableStateOf(false) }
                Column {
                    Text(
                        text = "Role",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Box {
                        RoleBadge(
                            role = selectedRole,
                            modifier = Modifier.clickable { expanded = true }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            Role.entries.forEach { role ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = role.name,
                                            color = roleColor(role)
                                        )
                                    },
                                    onClick = {
                                        selectedRole = role
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.lg))

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm, Alignment.End),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
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
