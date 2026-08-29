package com.abhishek.spendly.ui.screens.expense

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.FractionalThreshold
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberSwipeableState
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.swipeable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.core.navigation.ScreenRoutes
import com.abhishek.spendly.data.model.Expense
import com.abhishek.spendly.data.model.ExpenseDM
import com.abhishek.spendly.data.model.ExpenseStatus
import com.abhishek.spendly.data.model.GroupMode
import com.abhishek.spendly.data.model.UserRole
import com.abhishek.spendly.ui.components.AnimatedAmountText
import com.abhishek.spendly.ui.components.BaseScaffold
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
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    navManager: NavManager? = null,
    viewModel: ExpenseViewModel? = hiltViewModel(),
    userRole: UserRole = UserRole.PERSONAL,
    currentUserId: String? = null
) {
    val uiState by viewModel?.uiState!!.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }
    var groupByCategory by remember { mutableStateOf(true) }
    var sortByAmount by remember { mutableStateOf(false) }
    var filterStatus: ExpenseStatus? by remember { mutableStateOf(null) } // Only in business
    var total by remember { mutableDoubleStateOf(0.0) }

    // Filter expenses based on role
    val filteredExpenses = remember(uiState.expenses, userRole, currentUserId, filterStatus) {
        val baseList = when (userRole) {
            UserRole.PERSONAL -> uiState.expenses.filter { it.userId == currentUserId }
            UserRole.ADMIN -> uiState.expenses
            UserRole.APPROVER -> uiState.expenses.filter { it.status == ExpenseStatus.PENDING }
            UserRole.ENTRY_ONLY -> uiState.expenses.filter { it.userId == currentUserId }
            UserRole.VIEWER -> uiState.expenses
        }
        if (userRole != UserRole.PERSONAL && filterStatus != null) {
            baseList.filter { it.status == filterStatus }
        } else baseList
    }

    // Grouping & Sorting
    val groupedExpenses = if (groupByCategory) {
        filteredExpenses.groupBy { it.category }
    } else {
        mapOf("All Expenses" to filteredExpenses)
    }

    val sortedExpenses = groupedExpenses.mapValues { entry ->
        if (sortByAmount) entry.value.sortedByDescending { it.amount } else entry.value.sortedBy { it.timestamp }
    }

    LaunchedEffect(filteredExpenses) {
        total = filteredExpenses.sumOf { it.amount }
    }

    BaseScaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Expenses (${filteredExpenses.size})", style = MaterialTheme.typography.titleLarge)
                        AnimatedAmountText(
                            amount = total,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            prefix = "Total: ₹"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter and sort expenses")
                    }
                }
            )
        },
        navManager = navManager,
        currentRoute = ScreenRoutes.ExpenseList.route,
        content = { padding ->
            if (sortedExpenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No expenses found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
                    sortedExpenses.forEach { (category, expenses) ->
                        item {
                            if (groupByCategory) {
                                Text(
                                    text = "$category (${expenses.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm)
                                )
                            }
                        }
                        items(expenses) { expense ->
                            SwipeableExpenseItem(
                                expense = expense,
                                userRole = userRole,
                                onDelete = { if (userRole == UserRole.PERSONAL || userRole == UserRole.ADMIN) viewModel.deleteExpense(it) },
                                onEdit = { if (userRole != UserRole.VIEWER && userRole != UserRole.APPROVER) viewModel.editExpense(it) },
                                onClick = { navManager?.navigate(ScreenRoutes.ExpenseDetail.passExpenseId(it.id.toString())) }
                            )
                        }
                    }
                }
            }
        }
    )

    // 🔹 Filter Dialog
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            confirmButton = {
                TextButton(onClick = { showFilterDialog = false }) {
                    Text("Apply")
                }
            },
            title = { Text("Filter & Sort", style = MaterialTheme.typography.titleLarge) },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = groupByCategory, onCheckedChange = { groupByCategory = it })
                        Text("Group by Category", style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = sortByAmount, onCheckedChange = { sortByAmount = it })
                        Text("Sort by Amount (High → Low)", style = MaterialTheme.typography.bodyMedium)
                    }

                    // Show status filter ONLY for business roles
                    if (userRole != UserRole.PERSONAL) {
                        Spacer(Modifier.height(AppSpacing.sm))
                        Text("Filter by Status:", style = MaterialTheme.typography.titleSmall)
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ExpenseStatus.entries.forEach { status ->
                                val selected = filterStatus == status
                                AssistChip(
                                    onClick = { filterStatus = if (filterStatus == status) null else status },
                                    label = { Text(status.name, style = MaterialTheme.typography.labelLarge) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                        labelColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.padding(AppSpacing.xs)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableExpenseItem(
    expense: ExpenseDM,
    userRole: UserRole,
    onDelete: (ExpenseDM) -> Unit,
    onEdit: (ExpenseDM) -> Unit,
    onClick: (ExpenseDM) -> Unit = {}
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    val successColor = if (isDark) SuccessColorDark else SuccessColor
    val dangerColor = if (isDark) DangerColorDark else DangerColor
    val warningColor = if (isDark) WarningColorDark else WarningColor

    // NOTE: swipe mechanics intentionally left untouched (old material.swipeable API) -
    // only the visuals below have been modernized.
    val swipeableState = rememberSwipeableState(0)
    val sizePx = 150f
    val anchors = mapOf(0f to 0, -sizePx to 1, sizePx to 2)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
            .background(Color.Transparent)
    ) {
        // Action buttons for delete/edit
        if (userRole == UserRole.PERSONAL || userRole == UserRole.ADMIN) {
            Row(
                modifier = Modifier.matchParentSize().padding(horizontal = AppSpacing.md),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        onDelete(expense)
                        Toast.makeText(context, "Deleted ${expense.title}", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete ${expense.title}",
                        tint = dangerColor
                    )
                }
                if (userRole != UserRole.VIEWER && userRole != UserRole.APPROVER) {
                    IconButton(
                        onClick = {
                            onEdit(expense)
                            Toast.makeText(context, "Edit ${expense.title}", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit ${expense.title}",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Expense Card
        Card(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.xs)
                .clickable { onClick(expense) },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppSpacing.md),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(expense.title, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(AppSpacing.xs))
                    Text(
                        "${expense.category} • ${SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(expense.timestamp))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    expense.notes?.takeIf { it.isNotEmpty() }?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (expense.receiptUri != null) {
                        Text(
                            "📎 Receipt attached",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    // Show extra info only in Business Mode
                    if (userRole != UserRole.PERSONAL) {
                        if (userRole == UserRole.ADMIN && expense.userName != null) {
                            Text(
                                "👤 Added by: ${expense.userName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(Modifier.height(AppSpacing.xs))
                        val statusColor = when (expense.status) {
                            ExpenseStatus.APPROVED -> successColor
                            ExpenseStatus.REJECTED -> dangerColor
                            ExpenseStatus.PENDING -> warningColor
                        }
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.extraSmall)
                                .background(statusColor.copy(alpha = 0.15f))
                                .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
                        ) {
                            Text(
                                expense.status.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = statusColor
                            )
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "₹${"%.2f".format(expense.amount)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewListScreenReusable() {
    // Fake data
    val demoExpenses = listOf(
        Expense(title = "Tea", amount = 10.0, category = "Food", notes = "Morning tea", receiptUri = null),
        Expense(title = "Bus", amount = 20.0, category = "Transport", notes = "Office travel", receiptUri = null)
    )

    ExpenseListScreen(
        viewModel = null
    )
}
