package com.abhishek.smartexpensetracker.ui.screens.listscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.AccessTime
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
import com.abhishek.smartexpensetracker.core.voice.VoiceManager
import com.abhishek.smartexpensetracker.data.model.DateFilter
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.data.model.GroupMode
import com.abhishek.smartexpensetracker.ui.components.BaseScaffold
import com.abhishek.smartexpensetracker.ui.components.BottomNavigationBar
import com.abhishek.smartexpensetracker.ui.screens.login.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    navManager: NavManager? = null,
    viewModel: ExpenseViewModel? = hiltViewModel()
) {
    val uiState by viewModel?.uiState!!.collectAsState()
    var filterExpanded by remember { mutableStateOf(false) }
    var total by remember{ mutableDoubleStateOf(0.0) }

    LaunchedEffect(uiState.expenses) {
        uiState.expenses.forEach {
            total = total + it.amount
        }
    }

    BaseScaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Expenses (${total}) • ₹${"%.2f".format(total)}")
                },
                actions = {
                    // 🔹 Search box
//                    TextField(
//                        value = uiState.searchQuery,
//                        onValueChange = { viewModel.updateSearch(it) },
//                        placeholder = { Text("Search…") },
//                        singleLine = true,
//                        modifier = Modifier.width(150.dp).padding(end = 8.dp)
//                    )

//                    // 🔹 Filter menu
//                    Box {
//                        IconButton(onClick = { filterExpanded = true }) {
//                            Icon(Icons.Default.CalendarMonth, contentDescription = "Filter")
//                        }
//                        DropdownMenu(
//                            expanded = filterExpanded,
//                            onDismissRequest = { filterExpanded = false }
//                        ) {
//                            DropdownMenuItem(
//                                text = { Text("Today") },
//                                onClick = { viewModel.loadExpenses(DateFilter.TODAY); filterExpanded = false }
//                            )
//                            DropdownMenuItem(
//                                text = { Text("Yesterday") },
//                                onClick = { viewModel.loadExpenses(DateFilter.YESTERDAY); filterExpanded = false }
//                            )
//                            DropdownMenuItem(
//                                text = { Text("Last 7 Days") },
//                                onClick = { viewModel.loadExpenses(DateFilter.LAST_7_DAYS); filterExpanded = false }
//                            )
//                            DropdownMenuItem(
//                                text = { Text("All") },
//                                onClick = { viewModel.loadExpenses(DateFilter.ALL); filterExpanded = false }
//                            )
//                        }
//                    }

//                    // 🔹 Toggle group
//                    IconButton(onClick = { viewModel.toggleGroupMode() }) {
//                        Icon(
//                            imageVector = if (uiState.groupMode == GroupMode.TIME) Icons.Default.Category else Icons.Default.AccessTime,
//                            contentDescription = "Toggle group"
//                        )
//                    }
//
//                    // 🔹 Reports
//                    IconButton(onClick = { /* navManager?.navigate(ScreenRoutes.Report.route) */ }) {
//                        Icon(Icons.Default.Assessment, contentDescription = "Report")
//                    }
                }
            )
        },
        navManager = navManager,
        currentRoute = ScreenRoutes.ExpenseList.route,
        /*bottomBar = {
            BottomNavigationBar(
                selectedRoute = ScreenRoutes.ExpenseList.route,
                onItemSelected = { route ->
                    if (route == ScreenRoutes.Voice.route) {
                        VoiceManager.toggleListening()
                    } else {
                        VoiceManager.stopListening()
                        navManager?.navigate(route)
                    }
                }
            )
        },*/
        floatingActionButton = {
            FloatingAddButton(onClick = { navManager?.navigate(ScreenRoutes.AddExpense.route) })
        },
        content = { padding ->
            ExpenseList(
            expenses = uiState.expenses,
            groupMode = uiState.groupMode,
            onDeleteExpense = { viewModel.deleteExpense(it) },
            onEditExpense = { viewModel.editExpense(it) },
            padding = padding,
            isLoading = false
        ) }
    )
}


@Composable
fun FloatingAddButton(onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Default.Add, contentDescription = "Add")
    }
}

@Composable
fun ExpenseList(
    expenses: List<Expense>,
    groupMode: GroupMode,
    onDeleteExpense: (Expense) -> Unit,
    onEditExpense: (Expense) -> Unit,
    padding: PaddingValues,
    isLoading: Boolean
) {
    if (expenses.isEmpty() && !isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("No expenses for selected filter.", style = MaterialTheme.typography.bodyMedium)
        }
    } else if (expenses.isEmpty() || isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        when (groupMode) {
            GroupMode.TIME -> {
                LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
                    items(expenses) {
                        SwipeableExpenseItem(
                            expense = it,
                            onDelete = onDeleteExpense,
                            onEdit = onEditExpense
                        )
                    }
                }
            }
            GroupMode.CATEGORY -> {
                val grouped = expenses.groupBy { it.category }
                LazyColumn(contentPadding = padding, modifier = Modifier.fillMaxSize()) {
                    grouped.forEach { (cat, list) ->
                        item { Text(cat, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(12.dp)) }
                        items(list) {
                            SwipeableExpenseItem(
                                expense = it,
                                onDelete = onDeleteExpense,
                                onEdit = onEditExpense
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableExpenseItem(
    expense: Expense,
    onDelete: (Expense) -> Unit,
    onEdit: (Expense) -> Unit
) {
    val context = LocalContext.current
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val sizePx = 150f
    val anchors = mapOf(0f to 0, -sizePx to 1, sizePx to 2) // 0 = normal, -1 = left swipe, 2 = right swipe

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
        // Background buttons
        Row(
            modifier = Modifier
                .matchParentSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side Delete
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onDelete(expense)
                        Toast.makeText(context, "Deleted ${expense.title}", Toast.LENGTH_SHORT)
                            .show()
                    }
            )
            // Right side Edit
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.Blue,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onEdit(expense)
                        Toast.makeText(context, "Edit ${expense.title}", Toast.LENGTH_SHORT).show()
                    }
            )
        }
        // Foreground Card (moves with swipe)
        Card(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .fillMaxWidth()
                .padding(6.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("₹${"%.2f".format(expense.amount)}", style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp))
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
