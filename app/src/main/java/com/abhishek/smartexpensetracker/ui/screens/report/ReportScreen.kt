package com.abhishek.smartexpensetracker.ui.screens.report

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.core.utils.DateUtils
import com.abhishek.smartexpensetracker.core.utils.ExportUtils
import com.abhishek.smartexpensetracker.data.model.UserRole
import com.abhishek.smartexpensetracker.ui.components.BaseScaffold
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    navManager: NavManager?,
    isBusinessMode: Boolean,
    userRole: UserRole,
    reportsViewModel: ReportsViewModel = hiltViewModel()
) {
    val uiState by reportsViewModel.reportsState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    BaseScaffold(
        navManager = navManager,
        currentRoute = ScreenRoutes.Reports.route,
        topBar = {
            TopAppBar(
                title = { Text("Reports & Analytics") },
                actions = {
                    IconButton(onClick = {/* reportsViewModel.exportReport() */}) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export Report")
                    }
                }
            )
        },
        /*floatingActionButton = {
            if (!isBusinessMode || userRole == UserRole.ADMIN || userRole == UserRole.ENTRY_ONLY) {
                FloatingActionButton(
                    onClick = { *//*reportsViewModel.onAddExpenseClick()*//* },
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }
            }
        },*/
        content =  { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Filters
                FiltersSection(
                    isBusinessMode = isBusinessMode,
                    userRole = userRole,
                    selectedPeriod = uiState.selectedPeriod,
                    selectedStaff = uiState.selectedStaff,
                    onPeriodSelected = { reportsViewModel.selectPeriod(it) },
                    onStaffSelected = { reportsViewModel.selectStaff(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Charts
                ReportsChartsSection(
                    uiState = uiState
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3. AI Insights
                AIInsightsSection(aiInsights = uiState.aiInsights)

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Export button (alternative place)
                Button(
                    onClick = { /*reportsViewModel.exportReport()*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Report")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}

@Composable
fun FiltersSection(
    isBusinessMode: Boolean,
    userRole: UserRole,
    selectedPeriod: ReportPeriod,
    selectedStaff: String?,
    onPeriodSelected: (ReportPeriod) -> Unit,
    onStaffSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Filters", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DropdownMenuBox(
                    items = ReportPeriod.entries.map { it.displayName },
                    selectedItem = selectedPeriod.displayName,
                    label = "Time Period",
                    onItemSelected = { period ->
                        onPeriodSelected(ReportPeriod.fromString(period))
                    },
                    modifier = Modifier.weight(0.5f)
                )
                if (isBusinessMode && userRole == UserRole.ADMIN) {
                    DropdownMenuBox(
                        items = listOf("All Staff", "Staff A", "Staff B"),
                        selectedItem = selectedStaff ?: "All Staff",
                        label = "Staff",
                        onItemSelected = { onStaffSelected(it) },
                        modifier = Modifier.weight(0.5f)
                    )

                }
            }
        }
    }
}

@Composable
fun ReportsChartsSection(uiState: ReportsUiState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Spending Trends", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            // TODO: Replace with Compose Chart library (Line / Bar chart)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Monthly Expenses", style = MaterialTheme.typography.titleMedium)
                    BarChart(
                        data = mapOf("Jan" to 2000f, "Feb" to 3500f, "Mar" to 1800f, "Apr" to 4000f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }

            /*Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                BarChart(
                    data = mapOf("Jan" to 2000f, "Feb" to 3500f, "Mar" to 1800f, "Apr" to 4000f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }*/
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Category Breakdown", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            // TODO: Replace with Pie chart

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Category Breakdown", style = MaterialTheme.typography.titleMedium)
                    PieChart(
                        data = mapOf("Food" to 40f, "Travel" to 25f, "Bills" to 35f),
                        colors = listOf(Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF9800)),
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
//            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                PieChart(
//                    data = mapOf("Food" to 40f, "Travel" to 25f, "Cab" to 25f, "Bills" to 35f),
//                    colors = listOf(Color(0xFF4CAF50), Color(0xFFE91E63), Color(0xFF2196F3), Color(0xFFFF9800)),
//                    modifier = Modifier
//                        .size(200.dp)
////                        .align(Alignment.CenterHorizontally)
//                )
//            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Line Chart", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 16.dp, bottom = 8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Spending Trend", style = MaterialTheme.typography.titleMedium)
                LineChart(
                    data = listOf(500f, 1200f, 800f, 2000f, 1800f, 2200f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}

@Composable
fun AIInsightsSection(aiInsights: List<String>) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text("AI Insights", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        aiInsights.forEach { insight ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = insight,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun DropdownMenuBox(
    items: List<String>,
    selectedItem: String,
    label: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(selectedItem) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(text = { Text(item) }, onClick = {
                    text = item
                    expanded = false
                    onItemSelected(item)
                })
            }
        }
    }
}

enum class ReportPeriod(val displayName: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly");

    companion object {
        fun fromString(name: String) = ReportPeriod.entries.first { it.displayName == name }
    }
}

data class ReportsUiState(
    val selectedPeriod: ReportPeriod = ReportPeriod.WEEKLY,
    val selectedStaff: String? = null,
    val expenses: List<Expense> = emptyList(),
    val aiInsights: List<String> = listOf("You spent 20% more on Travel this month.", "Food category is your highest expense.")
)

@Composable
fun FilterSection(
    isBusinessMode: Boolean,
    userRole: UserRole,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Filters", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DropdownMenuBox(
                    items = listOf("Weekly", "Monthly", "Quarterly"),
                    label = "Time Period",
                    onItemSelected = { period ->
//                        onFilterSelected(ReportFilter.Period(period))
                    }
                )
                if (isBusinessMode && userRole == UserRole.ADMIN) {
                    DropdownMenuBox(
                        items = listOf("All Staff", "Staff 1", "Staff 2"),
                        label = "Staff",
                        onItemSelected = { staff ->
//                            onFilterSelected(ReportFilter.Staff(staff))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChartsSection(uiState: ReportsUiState) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Spending Trends",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            // Replace with a Compose chart library
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Bar / Line Chart Placeholder")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Category Breakdown",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Pie Chart Placeholder")
            }
        }
    }
}


@Composable
fun DropdownMenuBox(items: List<String>, label: String, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(items.first()) }

    Column {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth(0.48f)
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(text = { Text(item) }, onClick = {
                    selectedText = item
                    expanded = false
                    onItemSelected(item)
                })
            }
        }
    }
}


/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navManager: NavManager? = null,
    last7DaysExpenses: List<Expense>,
    isBusinessMode: Boolean = false,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = if (isBusinessMode) listOf("Overview", "Categories", "Staff") else listOf("Overview", "Categories")

    // Totals grouped by day
    val last7DaysTotals = remember(last7DaysExpenses) {
        val now = System.currentTimeMillis()
        (0..6).mapNotNull { i ->
            val dayStart = DateUtils.startOfDayMillis(now - i * 86400000L)
            val dayEnd = DateUtils.endOfDayMillis(now - i * 86400000L)
            val dayExpenses = last7DaysExpenses.filter { it.timestamp in dayStart..dayEnd }
            if (dayExpenses.isNotEmpty()) dayStart to dayExpenses.sumOf { it.amount } else null
        }.reversed()
    }

    // Category totals
    val categoryTotals = remember(last7DaysExpenses) {
        last7DaysExpenses.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    // Staff totals
    val staffTotals = remember(last7DaysExpenses) {
        last7DaysExpenses.groupBy { it.category ?: "Unknown" }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    BaseScaffold(
        navManager = navManager,
        currentRoute = ScreenRoutes.Reports.route,
        topBar = {
            ReportTopBar(
                onBack = onBack,
                onExport = {
                    val csv = ExportUtils.buildCsvFromExpenses(last7DaysExpenses)
                    val uri = ExportUtils.writeCsvToCache(context, "expenses.csv", csv)
                    if (uri != null) Toast.makeText(context, "Exported to cache", Toast.LENGTH_SHORT).show()
                },
                onShare = {
                    val csv = ExportUtils.buildCsvFromExpenses(last7DaysExpenses)
                    val uri = ExportUtils.writeCsvToCache(context, "expenses.csv", csv)
                    if (uri != null) ExportUtils.shareCsv(context, uri)
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Modern Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            height = 4.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title, style = MaterialTheme.typography.labelLarge) }
                        )
                    }
                }

                when (selectedTab) {
                    0 -> OverviewTab(last7DaysExpenses, last7DaysTotals)
                    1 -> CategoriesTab(categoryTotals)
                    2 -> if (isBusinessMode) StaffTab(staffTotals)
                }
            }
        }
    )
}*/

/* ---------------- Tab Screens ---------------- */

@Composable
fun OverviewTab(expenses: List<Expense>, last7DaysTotals: List<Pair<Long, Double>>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionTitle("AI Insights") }
        item { InsightsCard(expenses = expenses) }

        item { SectionTitle("Totals — Last 7 Days") }
        item {
            last7DaysTotals.forEach { (dayMs, total) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(dayMs)))
                        Text("₹${"%.2f".format(total)}")
                    }
                }
            }
        }

        item { SectionTitle("Daily Overview (Bar Chart)") }
        item { DailyBarChart(last7DaysTotals.associate { it.first to it.second }) }
    }
}

@Composable
fun CategoriesTab(categoryTotals: Map<String, Double>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionTitle("Category Totals") }
        item { TotalsList(data = categoryTotals) }
        item { CategoryPieChart(categoryTotals) }
    }
}

@Composable
fun StaffTab(staffTotals: Map<String, Double>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { SectionTitle("Staff Totals") }
        item { TotalsList(data = staffTotals) }
    }
}

/* ---------------- Modern Components ---------------- */

@Composable
fun InsightsCard(expenses: List<Expense>) {
    val total = expenses.sumOf { it.amount }
    val food = expenses.filter { it.category == "Food" }.sumOf { it.amount }
    val foodPercent = if (total > 0) (food / total) * 100 else 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Smart Insights", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text("You spent ₹${"%.0f".format(food)} on Food (${String.format("%.1f", foodPercent)}% of total).")
            Text("Highest category: ${expenses.groupBy { it.category }.maxByOrNull { it.value.sumOf { e -> e.amount } }?.key ?: "N/A"}")
        }
    }
}

@Composable
fun CategoryPieChart(data: Map<String, Double>) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        val sum = data.values.sumOf { it } ?: 1.0
        data.forEach { (category, total) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(category, modifier = Modifier.weight(1f))
                LinearProgressIndicator(
                progress = { (total / sum).toFloat() },
                modifier = Modifier
                    .weight(2f)
                    .height(12.dp)
                    .padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = ProgressIndicatorDefaults.linearTrackColor,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )
                Text("₹${"%.0f".format(total)}", modifier = Modifier.weight(1f))
            }
        }
    }
}
/* ---------------- Reusable Components ---------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportTopBar(onBack: () -> Unit, onExport: () -> Unit, onShare: () -> Unit) {
    TopAppBar(
        title = { Text("Report — Last 7 days") },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {
            IconButton(onClick = onExport) {
                Icon(Icons.Default.UploadFile, contentDescription = "Export")
            }
            IconButton(onClick = onShare) {
                Icon(Icons.Default.Share, contentDescription = "Share")
            }
        }
    )
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
    HorizontalDivider(
        Modifier.padding(bottom = 8.dp),
        DividerDefaults.Thickness,
        DividerDefaults.color
    )
}

@Composable
fun TotalsList(data: Map<String, Double>) {
    Column {
        data.forEach { (label, total) ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label)
                Text("₹${"%.2f".format(total)}")
            }
        }
    }
    HorizontalDivider(
        Modifier.padding(vertical = 12.dp),
        DividerDefaults.Thickness,
        DividerDefaults.color
    )
}

@Composable
fun DailyBarChart(dailyTotals: Map<Long, Double>) {
    val maxTotal = dailyTotals.values.maxOrNull() ?: 1.0
    Column {
        dailyTotals.forEach { (dayMs, total) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                val dayLabel = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(dayMs))
                Text(
                    text = dayLabel,
                    modifier = Modifier.width(60.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .width((200 * (total / maxTotal)).dp)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(Modifier.width(8.dp))
                Text("₹${"%.0f".format(total)}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
    HorizontalDivider(
        Modifier.padding(vertical = 12.dp),
        DividerDefaults.Thickness,
        DividerDefaults.color
    )
}

@Composable
fun ExpenseRow(expense: Expense) {
    ListItem(
        headlineContent = { Text(expense.title) },
        supportingContent = {
            Text(
                SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                    .format(Date(expense.timestamp)) + " — " + expense.category
            )
        },
        trailingContent = { Text("₹${"%.2f".format(expense.amount)}") }
    )
    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
}

/* ---------------- Preview ---------------- */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewReportsScreen_Personal() {
    // Mock ViewModel state for Personal User
    val mockUiState = ReportsUiState(
        selectedPeriod = ReportPeriod.WEEKLY,
        selectedStaff = null,
        expenses = listOf(
            Expense(1, "Lunch", 250.0, "Food", "2025-08-23"),
            Expense(2, "Taxi", 500.0, "Travel", "2025-08-23")
        ),
        aiInsights = listOf(
            "You spent 20% more on Travel this month.",
            "Food category is your highest expense."
        )
    )

    // Using a fake ViewModel for preview
    ReportsScreenPreviewContent(
        isBusinessMode = false,
        userRole = UserRole.PERSONAL,
        uiState = mockUiState
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewReportsScreen_BusinessAdmin() {
    val mockUiState = ReportsUiState(
        selectedPeriod = ReportPeriod.MONTHLY,
        selectedStaff = "All Staff",
        expenses = listOf(
            Expense(1, "Office Supplies", 1200.0, "Utility", "2025-08-23", "Staff A"),
            Expense(2, "Client Lunch", 2000.0, "Food", "2025-08-23", "Staff B")
        ),
        aiInsights = listOf(
            "Staff A exceeded budget by 15% in Food category.",
            "Travel expenses increased by 10% compared to last month."
        )
    )

    ReportsScreenPreviewContent(
        isBusinessMode = true,
        userRole = UserRole.ADMIN,
        uiState = mockUiState
    )
}

// Helper composable to inject mock state for preview
@Composable
fun ReportsScreenPreviewContent(
    isBusinessMode: Boolean,
    userRole: UserRole,
    uiState: ReportsUiState
) {
    // Fake ViewModel implementation
    val fakeViewModel = object : ReportsViewModelFake(uiState) {}
    ReportsScreen(
        navManager = null,
        isBusinessMode = isBusinessMode,
        userRole = userRole,
        reportsViewModel = fakeViewModel
    )
}

// Fake ViewModel class for preview
open class ReportsViewModelFake(private val mockState: ReportsUiState) : ReportsViewModel() {
    override val reportsState: StateFlow<ReportsUiState> = MutableStateFlow(mockState)
}

