package com.abhishek.smartexpensetracker.ui.screens.report

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.data.model.UserRole
import com.abhishek.smartexpensetracker.ui.components.BaseScaffold


/* ---------------- Screen ---------------- */

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReportsScreen(
    navManager: NavManager?,
    isBusinessMode: Boolean,
    userRole: UserRole,
    reportsViewModel: ReportsViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val uiState by reportsViewModel.reportsState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var chartMode by remember { mutableStateOf(true) } // toggle between table & charts

    BaseScaffold(
        navManager = navManager,
        currentRoute = ScreenRoutes.Reports.route,
        topBar = {
            TopAppBar(
                title = { Text("Reports & Analytics") },
                navigationIcon = {
                    IconButton(onClick = { navManager?.navigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { reportsViewModel.exportReport() }) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export Report")
                    }
                    IconButton(onClick = { /* Share Logic */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share Report")
                    }
                }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                /* 🔹 Search & Quick Filter */
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                        Spacer(Modifier.width(8.dp))
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = TextStyle(fontSize = 16.sp),
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    Color.LightGray.copy(alpha = 0.3f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(10.dp),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text("Search expenses...", color = Color.Gray, fontSize = 14.sp)
                                }
                                innerTextField()
                            }
                        )
                    }
                }

                /* 🔹 Filters */
                item {
                    FiltersSection(
                        isBusinessMode = isBusinessMode,
                        userRole = userRole,
                        selectedPeriod = uiState.selectedPeriod,
                        selectedStaff = uiState.selectedStaff,
                        onPeriodSelected = { reportsViewModel.selectPeriod(it) },
                        onStaffSelected = { reportsViewModel.selectStaff(it) }
                    )
                }

                /* 🔹 Toggle View: Chart / List */
                item {
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("View Mode", style = MaterialTheme.typography.titleMedium)
                        IconButton(onClick = { chartMode = !chartMode }) {
                            if (chartMode) Icon(Icons.Default.List, "Switch to List")
                            else Icon(Icons.Default.PieChart, "Switch to Charts")
                        }
                    }
                }

                /* 🔹 Charts / Table */
                if (chartMode) {
                    item {
                        ReportsChartsSection(uiState)
                    }
                } else {
                    item {
                        ExpenseTableSection(uiState.expenses, searchQuery)
                    }
                }

                /* 🔹 AI Insights */
                if (uiState.aiInsights.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(16.dp))
                        AIInsightsSection(uiState.aiInsights)
                    }
                }

                item { Spacer(Modifier.height(32.dp)) }
            }
        }
    )
}

@Composable
fun ExpenseTableSection(expenses: List<Expense>, searchQuery: String) {
    val filtered = expenses.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            filtered.forEach { expense ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(expense.title, style = MaterialTheme.typography.titleMedium)
                        Text("₹${expense.amount}", style = MaterialTheme.typography.bodyLarge)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            expense.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = "${expense.timestamp}", // ✅ fixed: don’t multiply timestamp
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    Text(
                        "By: ${expense.title}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.DarkGray)
                    )

                    Divider(modifier = Modifier.padding(vertical = 6.dp))
                }
            }
        }
    }
}

/* ---------------- AI Insights with Icons ---------------- */

@Composable
fun AIInsightsSection(aiInsights: List<String>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Text("AI Insights", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        aiInsights.forEach { insight ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PieChart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text(insight, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

/* ---------------- Filters ---------------- */

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
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Filters", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DropdownMenuBox(
                    items = ReportPeriod.entries.map { it.displayName },
                    selectedItem = selectedPeriod.displayName,
                    label = "Time Period",
                    onItemSelected = { period -> onPeriodSelected(ReportPeriod.fromString(period)) },
                    modifier = Modifier.weight(1f)
                )
                if (isBusinessMode && userRole == UserRole.ADMIN) {
                    DropdownMenuBox(
                        items = listOf("All Staff", "Staff A", "Staff B"),
                        selectedItem = selectedStaff ?: "All Staff",
                        label = "Staff",
                        onItemSelected = onStaffSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(
    items: List<String>,
    selectedItem: String,
    label: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(selectedItem) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        selected = item
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

/* ---------------- Charts Section ---------------- */

@Composable
fun ReportsChartsSection(uiState: ReportsUiState) {
    Column(modifier = Modifier.fillMaxWidth()) {

        // 📊 Spending Trends (Bar Chart with Scroll)
        Text(
            "Spending Trends",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            val barData = mapOf(
                "Jan" to 2000f, "Feb" to 3500f, "Mar" to 1800f,
                "Apr" to 4000f, "May" to 2500f, "Jun" to 3200f
            )

            // Scrollable if too many months
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                BarChartWithAxis(
                    data = barData,
                    modifier = Modifier
                        .height(220.dp)
                        .width((barData.size * 90).dp) // dynamic width
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 🥧 Category Breakdown (Pie Chart with List)
        Text(
            "Category Breakdown",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            val categoryData = mapOf(
                "Food" to 40f,
                "Travel" to 25f,
                "Bills" to 35f,
                "Shopping" to 15f,
                "Other" to 10f
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PieChartWithHover(
                    data = categoryData,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )

                Spacer(Modifier.height(12.dp))

                // Sorted List High → Low
                categoryData.entries
                    .sortedByDescending { it.value }
                    .forEach { (category, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(category, style = MaterialTheme.typography.bodyMedium)
                            Text("${value}%", style = MaterialTheme.typography.bodySmall)
                        }
                    }
            }
        }

        Spacer(Modifier.height(16.dp))

        // 📈 Spending Over Time (Line Chart with Axis)
        Text(
            "Spending Over Time",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            val lineData = listOf(500f, 1200f, 800f, 2000f, 1800f, 2200f)

            LineChartWithAxis(
                data = lineData,
                xLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun BarChartWithAxis(data: Map<String, Float>, modifier: Modifier = Modifier) {
    val maxY = (data.values.maxOrNull() ?: 0f) * 1.2f
    Canvas(modifier = modifier) {
        val barWidth = size.width / (data.size * 2)
        val space = barWidth
        val xStep = (barWidth + space)

        // Y axis
        drawLine(Color.Gray, start = Offset(80f, 0f), end = Offset(80f, size.height))

        data.entries.forEachIndexed { index, entry ->
            val barHeight = (entry.value / maxY) * size.height
            val x = 100f + index * xStep
            drawRect(
                color = Color(0xFF2196F3),
                topLeft = Offset(x, size.height - barHeight),
                size = Size(barWidth, barHeight)
            )
            drawContext.canvas.nativeCanvas.drawText(
                entry.key,
                x,
                size.height - 10,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 28f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
            drawContext.canvas.nativeCanvas.drawText(
                entry.value.toInt().toString(),
                x,
                size.height - barHeight - 10,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.DKGRAY
                    textSize = 26f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}
@Composable
fun PieChartWithHover(
    data: Map<String, Float>,
    modifier: Modifier = Modifier
) {
    val total = data.values.sum()
    var selectedSlice by remember { mutableStateOf<String?>(null) }

    // Auto generate distinct colors based on number of slices
    val colors = remember(data.size) {
        List(data.size) { index ->
            Color.hsv(
                hue = (index * (360f / data.size)) % 360f,
                saturation = 0.7f,
                value = 0.9f
            )
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier
            .matchParentSize()
            .clickable {
                // Toggle selection on click (for demo purpose)
                selectedSlice = if (selectedSlice == null) data.keys.first() else null
            }) {
            var startAngle = -90f
            data.entries.forEachIndexed { index, entry ->
                val sweep = 360 * (entry.value / total)
                val sliceColor =
                    if (selectedSlice == entry.key) colors[index].copy(alpha = 0.6f)
                    else colors[index]

                // ✅ Draw only the slice (no inner circle)
                drawArc(
                    color = sliceColor,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    size = size
                )
                startAngle += sweep
            }
        }

        // ✅ Show hover/selection details in center
        selectedSlice?.let {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(it, style = MaterialTheme.typography.bodyMedium)
                Text(
                    "${data[it]?.toInt()} (${((data[it] ?: 0f) / total * 100).toInt()}%)",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}





@Composable
fun LineChartWithAxis(data: List<Float>, xLabels: List<String>, modifier: Modifier = Modifier) {
    val maxY = (data.maxOrNull() ?: 0f) * 1.2f
    Canvas(modifier = modifier) {
        val xStep = size.width / (data.size - 1)
        val points = data.mapIndexed { i, value ->
            Offset(i * xStep, size.height - (value / maxY) * size.height)
        }

        // Y Axis
        drawLine(Color.Gray, start = Offset(80f, 0f), end = Offset(80f, size.height))

        // Line
        for (i in 0 until points.size - 1) {
            drawLine(Color(0xFFFF5722), points[i], points[i + 1], strokeWidth = 6f)
        }

        // Points + labels
        points.forEachIndexed { i, point ->
            drawCircle(Color(0xFFFF5722), radius = 10f, center = point)
            drawContext.canvas.nativeCanvas.drawText(
                data[i].toInt().toString(),
                point.x,
                point.y - 15,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 28f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
            drawContext.canvas.nativeCanvas.drawText(
                xLabels[i],
                point.x,
                size.height - 10,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.DKGRAY
                    textSize = 26f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}



/* ---------------- AI Insights ---------------- */

/*@Composable
fun AIInsightsSection(aiInsights: List<String>) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Text("AI Insights", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        aiInsights.forEach { insight ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(insight, modifier = Modifier.padding(12.dp))
            }
        }
    }
}*/

/* ---------------- ViewModel ---------------- */

enum class ReportPeriod(val displayName: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly");

    companion object {
        fun fromString(name: String) = entries.first { it.displayName == name }
    }
}

data class ReportsUiState(
    val selectedPeriod: ReportPeriod = ReportPeriod.WEEKLY,
    val selectedStaff: String? = null,
    val expenses: List<Expense> = emptyList(),
    val aiInsights: List<String> = listOf("You spent 20% more on Travel this month.", "Food category is your highest expense.")
)
