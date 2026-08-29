package com.abhishek.spendly.ui.screens.expense

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.abhishek.spendly.data.model.UserRole
import com.abhishek.spendly.data.model.ExpenseStatus
import com.abhishek.spendly.ui.components.BaseScaffold
import com.abhishek.spendly.ui.components.AnimatedAmountText
import com.abhishek.spendly.ui.components.GradientCard
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.core.navigation.ScreenRoutes
import com.abhishek.spendly.data.model.ExpenseDM
import com.abhishek.spendly.ui.screens.login.viewmodel.ExpenseViewModel
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.DangerColor
import com.abhishek.spendly.ui.theme.DangerColorDark
import com.abhishek.spendly.ui.theme.SuccessColor
import com.abhishek.spendly.ui.theme.SuccessColorDark
import com.abhishek.spendly.ui.theme.WarningColor
import com.abhishek.spendly.ui.theme.WarningColorDark
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailScreen(
    expense: ExpenseDM,
    userRole: UserRole,
    navManager: NavManager? = null,
    viewModel: ExpenseViewModel? = hiltViewModel()
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    val successColor = if (isDark) SuccessColorDark else SuccessColor
    val dangerColor = if (isDark) DangerColorDark else DangerColor
    val warningColor = if (isDark) WarningColorDark else WarningColor
    val statusColor = when (expense.status) {
        ExpenseStatus.APPROVED -> successColor
        ExpenseStatus.REJECTED -> dangerColor
        ExpenseStatus.PENDING -> warningColor
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    BaseScaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Detail", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navManager?.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        navManager = navManager,
        currentRoute = ScreenRoutes.ExpenseDetail.route,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(AppSpacing.md),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
            ) {
                // Hero card: the amount is the star of the screen
                GradientCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        expense.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(Modifier.height(AppSpacing.xs))
                    AnimatedAmountText(
                        amount = expense.amount,
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )
                }

                // Category / status as small colored chips below the hero amount
                Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
                    StatusBadge(
                        text = expense.category,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    StatusBadge(
                        text = expense.status.name,
                        containerColor = statusColor.copy(alpha = 0.15f),
                        contentColor = statusColor
                    )
                }

                // Details card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                    ) {
                        DetailRow(label = "Notes", value = expense.notes?.takeIf { it.isNotBlank() } ?: "-")
                        DetailRow(
                            label = "Date",
                            value = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(expense.timestamp))
                        )
                        if (!expense.userName.isNullOrEmpty()) {
                            DetailRow(label = "Added by", value = expense.userName)
                        }
                    }
                }

                // Receipt Image
                expense.receiptUri?.let {
                    Text("Receipt", style = MaterialTheme.typography.titleSmall)
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Receipt image attached to this expense",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                }

                Spacer(modifier = Modifier.height(AppSpacing.sm))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.md, Alignment.CenterHorizontally)
                ) {
                    // Approve/Reject only for Admin or Approver
                    if (userRole == UserRole.ADMIN || userRole == UserRole.APPROVER) {
                        Button(
                            onClick = {
                                viewModel?.approveExpense(expense)
                                Toast.makeText(context, "Approved ${expense.title}", Toast.LENGTH_SHORT).show()
                                navManager?.navigateBack()
                            },
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(containerColor = successColor, contentColor = MaterialTheme.colorScheme.onPrimary)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(Modifier.width(AppSpacing.xs))
                            Text("Approve", style = MaterialTheme.typography.labelLarge)
                        }

                        Button(
                            onClick = {
                                viewModel?.rejectExpense(expense)
                                Toast.makeText(context, "Rejected ${expense.title}", Toast.LENGTH_SHORT).show()
                                navManager?.navigateBack()
                            },
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(containerColor = dangerColor, contentColor = MaterialTheme.colorScheme.onError)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Spacer(Modifier.width(AppSpacing.xs))
                            Text("Reject", style = MaterialTheme.typography.labelLarge)
                        }
                    }

                    // Edit/Delete only for Personal, Admin, Entry Only
                    if (userRole == UserRole.PERSONAL || userRole == UserRole.ADMIN || userRole == UserRole.ENTRY_ONLY) {
                        Button(
                            onClick = { viewModel?.editExpense(expense) },
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(Modifier.width(AppSpacing.xs))
                            Text("Edit", style = MaterialTheme.typography.labelLarge)
                        }

                        Button(
                            onClick = {
                                viewModel?.deleteExpense(expense)
                                Toast.makeText(context, "Deleted ${expense.title}", Toast.LENGTH_SHORT).show()
                                navManager?.navigateBack()
                            },
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.buttonColors(containerColor = dangerColor, contentColor = MaterialTheme.colorScheme.onError)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(Modifier.width(AppSpacing.xs))
                            Text("Delete", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun StatusBadge(text: String, containerColor: androidx.compose.ui.graphics.Color, contentColor: androidx.compose.ui.graphics.Color) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.extraSmall)
            .background(containerColor)
            .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
    ) {
        Text(text, style = MaterialTheme.typography.labelMedium, color = contentColor)
    }
}
