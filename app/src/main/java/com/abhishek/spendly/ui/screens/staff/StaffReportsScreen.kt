package com.abhishek.spendly.ui.screens.staff

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.ui.components.AnimatedAmountText
import com.abhishek.spendly.ui.components.GradientCard
import com.abhishek.spendly.ui.theme.AppShapes
import com.abhishek.spendly.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffReportsScreen(navManager: NavManager? = null) {
    val staffList = sampleStaffList.sortedByDescending { it.totalExpense }
    val totalSpend = sampleStaffList.sumOf { it.totalExpense }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Staff Reports") },
                navigationIcon = {
                    if (navManager != null) {
                        IconButton(onClick = { navManager.navigateBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(AppSpacing.md)
        ) {
            GradientCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Total staff spend",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(Modifier.height(AppSpacing.xs))
                AnimatedAmountText(
                    amount = totalSpend,
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    decimals = 0
                )
                Spacer(Modifier.height(AppSpacing.xs))
                Text(
                    "${staffList.size} staff members",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }

            Spacer(Modifier.height(AppSpacing.lg))
            Text("By staff member", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(AppSpacing.sm))

            if (staffList.isEmpty()) {
                Text(
                    "No staff added yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    items(staffList, key = { it.id }) { staff ->
                        Card(
                            shape = AppShapes.medium,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(AppSpacing.md),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(staff.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(AppSpacing.xs))
                                    RoleBadge(role = staff.role)
                                }
                                Text(
                                    "₹${staff.totalExpense}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewStaffReportsScreen() {
    StaffReportsScreen()
}
