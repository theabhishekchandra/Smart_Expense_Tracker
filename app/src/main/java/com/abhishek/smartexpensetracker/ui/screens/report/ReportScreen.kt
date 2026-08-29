package com.abhishek.smartexpensetracker.ui.screens.report

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.FileDownload
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
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.data.model.UserRole
import com.abhishek.smartexpensetracker.ui.components.AnimatedAmountText
import com.abhishek.smartexpensetracker.ui.components.BaseScaffold
import com.abhishek.smartexpensetracker.ui.components.GradientCard
import com.abhishek.smartexpensetracker.ui.screens.expense.ExpenseCategory
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing
import kotlin.collections.forEach
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ReportsScreen(
    navManager: NavManager?,
    isBusinessMode: Boolean,
    userRole: UserRole,
    reportsViewModel: ReportsViewModel = hiltViewModel()
) {
    val uiState by reportsViewModel.reportsState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var chartMode by remember { mutableStateOf(true) }

    BaseScaffold(
        navManager = navManager,
        currentRoute = ScreenRoutes.Reports.route,
        topBar = {
            TopAppBar(
                title = { Text("Reports & Analytics", style = MaterialTheme.typography.titleLarge) },
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
                /* Hero summary - total spend for the selected period */
                item {
                    val totalSpend = uiState.expenses.sumOf { it.amount }
                    GradientCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppSpacing.md)
                    ) {
                        Text(
                            "Total ${uiState.selectedPeriod.displayName} Spend",
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
                            "${uiState.expenses.size} transactions",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }

                /* Search & Quick Filter */
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppSpacing.md),
                        placeholder = { Text("Search expenses...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        singleLine = true,
                        shape = MaterialTheme.shapes.small
                    )
                }

                /* Filters */
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

                /* Toggle View: Chart / List */
                item {
                    Spacer(Modifier.height(AppSpacing.md))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppSpacing.md),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("View Mode", style = MaterialTheme.typography.titleLarge)
                        IconButton(onClick = { chartMode = !chartMode }) {
                            if (chartMode) Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Switch to List")
                            else Icon(Icons.Default.PieChart, contentDescription = "Switch to Charts")
                        }
                    }
                }

                /* Charts / Table */
                if (chartMode) {
                    item {
                        ReportsChartsSection(uiState)
                    }
                } else {
                    item {
                        ExpenseTableSection(uiState.expenses, searchQuery)
                    }
                }

                /* AI Insights */
                if (uiState.aiInsights.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(AppSpacing.md))
                        AIInsightsSection(uiState.aiInsights)
                    }
                }

                item { Spacer(Modifier.height(AppSpacing.xl)) }
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
            .padding(AppSpacing.md),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppSpacing.sm + AppSpacing.xs)
        ) {
            filtered.forEach { expense ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppSpacing.sm)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(expense.title, style = MaterialTheme.typography.titleMedium)
                        Text("₹${expense.amount}", style = MaterialTheme.typography.titleMedium)
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            expense.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${expense.timestamp}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        "By: ${expense.title}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = AppSpacing.xs + 2.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
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
        .padding(horizontal = AppSpacing.md)) {
        Text("AI Insights", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(AppSpacing.sm))
        aiInsights.forEach { insight ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppSpacing.xs),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(modifier = Modifier.padding(AppSpacing.sm + AppSpacing.xs), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.PieChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(Modifier.width(AppSpacing.sm))
                    Text(
                        insight,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
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
            .padding(horizontal = AppSpacing.md),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(AppSpacing.md)) {
            Text("Filters", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(AppSpacing.sm + AppSpacing.xs))

            Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm + AppSpacing.xs)) {
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
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
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

        // Spending Trends (Bar Chart with Scroll)
        Text(
            "Spending Trends",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = AppSpacing.md, bottom = AppSpacing.sm)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(horizontal = AppSpacing.md),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            val barData = mapOf(
                "Jan" to 2000f, "Feb" to 3500f, "Mar" to 1800f,
                "Apr" to 4000f, "May" to 2500f, "Jun" to 3200f
            )

            // Scrollable if too many months
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(AppSpacing.sm + AppSpacing.xs)
            ) {
                BarChartWithAxis(
                    data = barData,
                    modifier = Modifier
                        .height(220.dp)
                        .width((barData.size * 90).dp) // dynamic width
                )
            }
        }

        Spacer(Modifier.height(AppSpacing.md))

        // Category Breakdown (Pie Chart with List)
        Text(
            "Category Breakdown",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = AppSpacing.md, bottom = AppSpacing.sm)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.md),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                    .padding(AppSpacing.md)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PieChartWithHover(
                    data = categoryData,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(AppSpacing.sm)
                )

                Spacer(Modifier.height(AppSpacing.sm + AppSpacing.xs))

                // Sorted List High -> Low
                categoryData.entries
                    .sortedByDescending { it.value }
                    .forEach { (category, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = AppSpacing.xs),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(category, style = MaterialTheme.typography.bodyMedium)
                            Text("${value}%", style = MaterialTheme.typography.bodySmall)
                        }
                    }
            }
        }

        Spacer(Modifier.height(AppSpacing.md))

        // Spending Over Time (Line Chart with Axis)
        Text(
            "Spending Over Time",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = AppSpacing.md, bottom = AppSpacing.sm)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(horizontal = AppSpacing.md),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            val lineData = listOf(500f, 1200f, 800f, 2000f, 1800f, 2200f)

            LineChartWithAxis(
                data = lineData,
                xLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun"),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppSpacing.md)
            )
        }
    }
}

@Composable
fun BarChartWithAxis(data: Map<String, Float>, modifier: Modifier = Modifier) {
    val maxY = (data.values.maxOrNull() ?: 0f) * 1.2f
    val barColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.outline
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()
    val valueColor = MaterialTheme.colorScheme.onSurface.toArgb()

    Canvas(modifier = modifier) {
        val barWidth = size.width / (data.size * 2)
        val space = barWidth
        val xStep = (barWidth + space)

        // Y axis
        drawLine(axisColor, start = Offset(80f, 0f), end = Offset(80f, size.height))

        data.entries.forEachIndexed { index, entry ->
            val barHeight = (entry.value / maxY) * size.height
            val x = 100f + index * xStep
            drawRect(
                color = barColor,
                topLeft = Offset(x, size.height - barHeight),
                size = Size(barWidth, barHeight)
            )
            drawContext.canvas.nativeCanvas.drawText(
                entry.key,
                x,
                size.height - 10,
                Paint().apply {
                    color = labelColor
                    textSize = 28f
                    textAlign = Paint.Align.CENTER
                }
            )
            drawContext.canvas.nativeCanvas.drawText(
                entry.value.toInt().toString(),
                x,
                size.height - barHeight - 10,
                Paint().apply {
                    color = valueColor
                    textSize = 26f
                    textAlign = Paint.Align.CENTER
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

    // Distinct colors for each slice, drawn from the shared theme-derived chart palette
    // instead of arbitrary hsv-generated colors.
    val palette = chartColorPalette()
    val colors = remember(data.size, palette) {
        List(data.size) { index -> palette[index % palette.size] }
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

                // Draw only the slice (no inner circle)
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

        // Show hover/selection details in center
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
    val axisColor = MaterialTheme.colorScheme.outline
    val lineColor = MaterialTheme.colorScheme.tertiary
    val valueColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

    Canvas(modifier = modifier) {
        val xStep = size.width / (data.size - 1)
        val points = data.mapIndexed { i, value ->
            Offset(i * xStep, size.height - (value / maxY) * size.height)
        }

        // Y Axis
        drawLine(axisColor, start = Offset(80f, 0f), end = Offset(80f, size.height))

        // Line
        for (i in 0 until points.size - 1) {
            drawLine(lineColor, points[i], points[i + 1], strokeWidth = 6f)
        }

        // Points + labels
        points.forEachIndexed { i, point ->
            drawCircle(lineColor, radius = 10f, center = point)
            drawContext.canvas.nativeCanvas.drawText(
                data[i].toInt().toString(),
                point.x,
                point.y - 15,
                Paint().apply {
                    color = valueColor
                    textSize = 28f
                    textAlign = Paint.Align.CENTER
                }
            )
            drawContext.canvas.nativeCanvas.drawText(
                xLabels[i],
                point.x,
                size.height - 10,
                Paint().apply {
                    color = labelColor
                    textSize = 26f
                    textAlign = Paint.Align.CENTER
                }
            )
        }
    }
}



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


@Composable
fun DonutChart(
    categories: List<ExpenseCategory>,
    totalExpense: Int,
    onSliceClicked: (ExpenseCategory) -> Unit,
    selectedCategory: ExpenseCategory?,
    showPercentage: Boolean
) {
    val holeColor = MaterialTheme.colorScheme.surface
    val labelColor = MaterialTheme.colorScheme.onSurface.toArgb()

    Canvas(
        modifier = Modifier
            .size(260.dp)
            .pointerInput(true) {
                detectTapGestures { offset ->
                    val center = size.width / 2f
                    val dx = offset.x - center
                    val dy = offset.y - center
                    val angle = (atan2(dy, dx) * (180f / Math.PI)).toFloat() + 180f

                    var startAngle = -90f
                    categories.forEach { category ->
                        val sweep = (category.percentage / 100f) * 360f
                        val endAngle = startAngle + sweep
                        if (angle in startAngle..endAngle) {
                            onSliceClicked(category)
                            return@detectTapGestures
                        }
                        startAngle += sweep
                    }
                }
            }
    ) {
        var startAngle = -90f
        val chartSize = size.minDimension

        categories.forEach { category ->
            val sweep = (category.percentage / 100f) * 360f
            val isSelected = category == selectedCategory
            val pushOut = if (isSelected) 20f else 0f
            val angleRad = Math.toRadians((startAngle + sweep / 2).toDouble())

            translate(
                left = (cos(angleRad) * pushOut).toFloat(),
                top = (sin(angleRad) * pushOut).toFloat()
            ) {
                drawArc(
                    color = category.color,
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true,
                    size = Size(chartSize, chartSize)
                )

                if (isSelected) {
                    val radius = chartSize / 2f + 30f
                    val textX = (cos(angleRad) * radius + chartSize / 2f)
                    val textY = (sin(angleRad) * radius + chartSize / 2f)

                    val label = if (showPercentage) {
                        "${category.percentage}%"
                    } else {
                        val amount = (totalExpense * category.percentage / 100).roundToInt()
                        "₹$amount"
                    }

                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        textX.toFloat(),
                        textY.toFloat(),
                        Paint().apply {
                            color = labelColor
                            textSize = 36f
                            textAlign = Paint.Align.CENTER
                            isFakeBoldText = true
                        }
                    )
                }
            }
            startAngle += sweep
        }

        // Donut hole
        drawCircle(
            color = holeColor,
            radius = chartSize / 7f,
            center = Offset(chartSize / 2f, chartSize / 2f)
        )
    }
}
