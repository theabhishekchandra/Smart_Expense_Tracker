package com.abhishek.smartexpensetracker.ui.screens.expense

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.data.model.ExpenseDM
import com.abhishek.smartexpensetracker.data.model.ExpenseStatus
import com.abhishek.smartexpensetracker.data.model.GroupMode
import com.abhishek.smartexpensetracker.data.model.UserRole
import com.abhishek.smartexpensetracker.ui.components.BaseScaffold
import com.abhishek.smartexpensetracker.ui.screens.login.viewmodel.ExpenseViewModel
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
                title = { Text("Expenses (${filteredExpenses.size}) • ₹${"%.2f".format(total)}") },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Filters")
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
                    Text("No expenses found", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
                    sortedExpenses.forEach { (category, expenses) ->
                        item {
                            if (groupByCategory) {
                                Text(
                                    text = "$category (${expenses.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                        items(expenses) { expense ->
                            SwipeableExpenseItem(
                                expense = expense,
                                userRole = userRole,
                                onDelete = { if (userRole == UserRole.PERSONAL || userRole == UserRole.ADMIN) viewModel.deleteExpense(it) },
                                onEdit = { if (userRole != UserRole.VIEWER && userRole != UserRole.APPROVER) viewModel.editExpense(it) }
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
            title = { Text("Filter & Sort") },
            text = {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = groupByCategory, onCheckedChange = { groupByCategory = it })
                        Text("Group by Category")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = sortByAmount, onCheckedChange = { sortByAmount = it })
                        Text("Sort by Amount (High → Low)")
                    }

                    // Show status filter ONLY for business roles
                    if (userRole != UserRole.PERSONAL) {
                        Spacer(Modifier.height(8.dp))
                        Text("Filter by Status:", style = MaterialTheme.typography.titleSmall)
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            ExpenseStatus.entries.forEach { status ->
                                AssistChip(
                                    onClick = { filterStatus = if (filterStatus == status) null else status },
                                    label = { Text(status.name) },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = if (filterStatus == status) Color(0xFF4CAF50) else Color.LightGray
                                    ),
                                    modifier = Modifier.padding(4.dp)
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
    onEdit: (ExpenseDM) -> Unit
) {
    val context = LocalContext.current
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
                modifier = Modifier.matchParentSize().padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(32.dp).clickable {
                        onDelete(expense)
                        Toast.makeText(context, "Deleted ${expense.title}", Toast.LENGTH_SHORT).show()
                    }
                )
                if (userRole != UserRole.VIEWER && userRole != UserRole.APPROVER) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Blue,
                        modifier = Modifier.size(32.dp).clickable {
                            onEdit(expense)
                            Toast.makeText(context, "Edit ${expense.title}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        // Expense Card
        Card(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .fillMaxWidth()
                .padding(6.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(expense.title, style = MaterialTheme.typography.titleSmall)
                    Text(
                        "${expense.category} • ${SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(expense.timestamp))}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    expense.notes?.takeIf { it.isNotEmpty() }?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                    }
                    if (expense.receiptUri != null) {
                        Text("📎 Receipt attached", style = MaterialTheme.typography.bodySmall.copy(color = Color.Blue))
                    }
                    // Show extra info only in Business Mode
                    if (userRole != UserRole.PERSONAL) {
                        if (userRole == UserRole.ADMIN && expense.userName != null) {
                            Text("👤 Added by: ${expense.userName}", style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray))
                        }
                        if (expense.status != null) {
                            Text(
                                "Status: ${expense.status.name}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = when (expense.status) {
                                        ExpenseStatus.APPROVED -> Color.Green
                                        ExpenseStatus.REJECTED -> Color.Red
                                        ExpenseStatus.PENDING -> Color(0xFFFF9800)
                                        else -> Color.Gray
                                    }
                                )
                            )
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "₹${"%.2f".format(expense.amount)}",
                        style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp)
                    )
                }
            }
        }
    }
}


@Composable
fun ExpenseList(
    expenses: List<ExpenseDM>,
    groupMode: GroupMode,
    userRole: UserRole,
    onDeleteExpense: (ExpenseDM) -> Unit,
    onEditExpense: (ExpenseDM) -> Unit,
    padding: PaddingValues,
    isLoading: Boolean
) {
    if (expenses.isEmpty() && !isLoading) {
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) { Text("No expenses for selected filter.", style = MaterialTheme.typography.bodyMedium) }
    } else if (expenses.isEmpty() || isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
            items(expenses) { expense ->
                SwipeableExpenseItem(
                    expense = expense,
                    userRole = userRole,
                    onDelete = onDeleteExpense,
                    onEdit = onEditExpense
                )
            }
        }
    }
}

/*@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableExpenseItem(
    expense: ExpenseDM,
    userRole: UserRole,
    onDelete: (ExpenseDM) -> Unit,
    onEdit: (ExpenseDM) -> Unit
) {
    val context = LocalContext.current
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
        // Show delete/edit only if role permits
        if (userRole == UserRole.PERSONAL || userRole == UserRole.ADMIN) {
            Row(
                modifier = Modifier.matchParentSize().padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red,
                    modifier = Modifier.size(32.dp).clickable {
                        onDelete(expense)
                        Toast.makeText(context, "Deleted ${expense.title}", Toast.LENGTH_SHORT).show()
                    }
                )
                if (userRole != UserRole.VIEWER && userRole != UserRole.APPROVER) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Blue,
                        modifier = Modifier.size(32.dp).clickable {
                            onEdit(expense)
                            Toast.makeText(context, "Edit ${expense.title}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

        // Foreground Card
        Card(
            modifier = Modifier.offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }.fillMaxWidth().padding(6.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(expense.title, style = MaterialTheme.typography.titleSmall)
                    Text(
                        "${expense.category} • ${
                            SimpleDateFormat("dd:MM:yyyy : hh:mm a", Locale.getDefault())
                                .format(Date(expense.timestamp))
                        }",
                        style = MaterialTheme.typography.bodySmall
                    )
                    expense.notes?.takeIf { it.isNotEmpty() }?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall)
                    }
                    if (userRole == UserRole.ADMIN && expense.userName != null) {
                        Text("Added by: ${expense.userName}", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("₹${"%.2f".format(expense.amount)}", style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp))
                }
            }
        }
    }
}*/



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
