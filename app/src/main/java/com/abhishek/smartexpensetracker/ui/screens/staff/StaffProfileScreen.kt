package com.abhishek.smartexpensetracker.ui.screens.staff

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.ui.components.AnimatedAmountText
import com.abhishek.smartexpensetracker.ui.components.GlassStatTile
import com.abhishek.smartexpensetracker.ui.components.GradientCard
import com.abhishek.smartexpensetracker.ui.theme.AppShapes
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing

@Composable
fun StaffProfileScreen(staff: Staff, pendingApprovals: Int = 0) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(AppSpacing.md)
    ) {
        // --- Hero: avatar / name / role + total expenses, in the same GradientCard
        // + AnimatedAmountText + GlassStatTile style as Home's summary cards. ---
        GradientCard(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.White.copy(alpha = 0.22f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = staff.name.split(" ").map { it.first() }.joinToString(""),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(AppSpacing.md))

                Column {
                    Text(
                        text = staff.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Box(
                        modifier = Modifier
                            .background(Color.White, AppShapes.small)
                            .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
                    ) {
                        Text(
                            text = staff.role.name,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = roleColor(staff.role)
                        )
                    }
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Text(
                        text = staff.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.lg))

            Text(
                "Total Expenses Logged",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(alpha = 0.85f)
            )
            Spacer(modifier = Modifier.height(AppSpacing.xs))
            AnimatedAmountText(
                amount = staff.totalExpense,
                style = MaterialTheme.typography.displaySmall,
                color = Color.White,
                decimals = 0
            )

            Spacer(modifier = Modifier.height(AppSpacing.md))
            GlassStatTile(
                label = "Pending Approvals",
                value = "$pendingApprovals",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(AppSpacing.md))

        // --- Quick Actions ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Card(
                shape = AppShapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.weight(1f).height(80.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Text(
                        "Add Expense",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Card(
                shape = AppShapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier.weight(1f).height(80.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Assessment,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    Text(
                        "View Reports",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewStaffProfileScreen() {
    StaffProfileScreen(
        staff = Staff(
            id = 1,
            staffId = "Staff002",
            name = "John Doe",
            email = "john@example.com",
            totalExpense = 1200.0,
            role = Role.Approver
        ),
        pendingApprovals = 3
    )
}
