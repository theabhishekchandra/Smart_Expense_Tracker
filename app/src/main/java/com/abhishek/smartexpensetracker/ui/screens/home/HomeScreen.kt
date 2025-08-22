package com.abhishek.smartexpensetracker.ui.screens.home

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.core.voice.VoiceManager
import com.abhishek.smartexpensetracker.ui.components.BaseScaffold
import com.abhishek.smartexpensetracker.ui.components.BottomNavigationBar
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navManager: NavManager? = null,
    state: HomeUiState,
    modifier: Modifier = Modifier
) {
    BaseScaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hello, ${state.userName}",
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Track smart. Save smart.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* notifications */ }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { /* menu */ }) {
                        Icon(Icons.Outlined.MoreVert, contentDescription = "More")
                    }
                }
            )
        },
        navManager = navManager,
        currentRoute = ScreenRoutes.Home.route,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navManager?.navigate(ScreenRoutes.AddExpense.route) },
                icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                text = { Text("Add Expense") }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { TodaySummaryCard(state) }
                item { IncomeVsExpenseCard(state) }
                if (state.weeklyTrend.isNotEmpty()) {
                    item { WeeklyTrendCard(state.weeklyTrend, state.currency) }
                }
                if (state.categoryBreakdown.isNotEmpty()) {
                    item { CategoryBreakdownCard(state.categoryBreakdown, state.currency) }
                }
                if (state.budgets.isNotEmpty()) {
                    item { BudgetsCard(state.budgets, state.currency) }
                }
                if (state.aiTips.isNotEmpty() || state.improvements.isNotEmpty()) {
                    item { AiFeedbackCard(state.aiTips, state.improvements, navManager) }
                }
                if (state.pendingApprovals.isNotEmpty()) {
                    item { PendingApprovalsCard(state.pendingApprovals, state.currency, navManager) }
                }
                if (state.recent.isNotEmpty()) {
                    item { RecentActivityCard(state.recent, state.currency, navManager) }
                }
                item { QuickActionsRow(navManager) }
                item { Spacer(Modifier.height(64.dp)) }
            }
        }
    )

//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Column {
//                        Text(
//                            text = "Hello, ${state.userName}",
//                            style = MaterialTheme.typography.titleLarge,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis
//                        )
//                        Text(
//                            text = "Track smart. Save smart.",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { /* notifications */ }) {
//                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
//                    }
//                    IconButton(onClick = { /* menu */ }) {
//                        Icon(Icons.Outlined.MoreVert, contentDescription = "More")
//                    }
//                }
//            )
//        },
//        bottomBar = {
//            BottomNavigationBar(
//                selectedRoute = ScreenRoutes.Home.route,
//                onItemSelected = { route ->
//                    if (route == ScreenRoutes.Voice.route) {
//                        VoiceManager.toggleListening()
//                    } else {
//                        VoiceManager.stopListening()
//                        navManager?.navigate(route)
//                    }
//                }
//            )
//        },
//        floatingActionButton = {
//            ExtendedFloatingActionButton(
//                onClick = { navManager?.navigate(ScreenRoutes.AddExpense.route) },
//                icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
//                text = { Text("Add Expense") }
//            )
//        }
//    ) { padding ->
//        LazyColumn(
//            modifier = modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            item { TodaySummaryCard(state) }
//            item { IncomeVsExpenseCard(state) }
//            if (state.weeklyTrend.isNotEmpty()) {
//                item { WeeklyTrendCard(state.weeklyTrend, state.currency) }
//            }
//            if (state.categoryBreakdown.isNotEmpty()) {
//                item { CategoryBreakdownCard(state.categoryBreakdown, state.currency) }
//            }
//            if (state.budgets.isNotEmpty()) {
//                item { BudgetsCard(state.budgets, state.currency) }
//            }
//            if (state.aiTips.isNotEmpty() || state.improvements.isNotEmpty()) {
//                item { AiFeedbackCard(state.aiTips, state.improvements, navManager) }
//            }
//            if (state.pendingApprovals.isNotEmpty()) {
//                item { PendingApprovalsCard(state.pendingApprovals, state.currency, navManager) }
//            }
//            if (state.recent.isNotEmpty()) {
//                item { RecentActivityCard(state.recent, state.currency, navManager) }
//            }
//            item { QuickActionsRow(navManager) }
//            item { Spacer(Modifier.height(64.dp)) }
//        }
//    }
}

// ---------- SECTION CARDS ----------

@Composable
private fun TodaySummaryCard(state: HomeUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Today’s spend", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${state.currency} ${state.todayTotal.roundToInt()}",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AssistChip(onClick = { }, label = { Text("Scan receipt") }, leadingIcon = {
                    Icon(Icons.Outlined.CameraAlt, contentDescription = null)
                })
                AssistChip(onClick = { }, label = { Text("Open reports") }, leadingIcon = {
                    Icon(Icons.Outlined.Assessment, contentDescription = null)
                })
            }
        }
    }
}

@Composable
private fun IncomeVsExpenseCard(state: HomeUiState) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("This month", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatPill(title = "Income", value = state.monthIncome, currency = state.currency)
                StatPill(title = "Expense", value = state.monthExpense, currency = state.currency)
                val net = state.monthIncome - state.monthExpense
                StatPill(title = "Net", value = net, currency = state.currency)
            }
        }
    }
}

@Composable
private fun StatPill(title: String, value: Double, currency: String) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("₹ ${value.roundToInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun WeeklyTrendCard(points: List<DailyPoint>, currency: String) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Weekly trend", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            SimpleBarChart(
                data = points.map { it.amount },
                labels = points.map { it.day },
                barWidth = 22.dp
            )
        }
    }
}

@Composable
private fun CategoryBreakdownCard(slices: List<CategorySlice>, currency: String) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Category breakdown", style = MaterialTheme.typography.titleMedium)
            slices.forEach { slice ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(slice.name, style = MaterialTheme.typography.bodyMedium)
                    Text("₹ ${slice.amount.roundToInt()}", fontWeight = FontWeight.Medium)
                }
                LinearProgressIndicator(
                    progress = { progressFrom(slice.amount, slices.sumOf { it.amount }) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
private fun BudgetsCard(items: List<BudgetProgress>, currency: String) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Budgets", style = MaterialTheme.typography.titleMedium)
            items.forEach { b ->
                Column(Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(b.category, fontWeight = FontWeight.Medium)
                        Text("₹ ${b.spent.roundToInt()} / ₹ ${b.limit.roundToInt()}")
                    }
                    val pct = (b.spent / (b.limit.coerceAtLeast(1.0))).toFloat().coerceIn(0f, 1f)
                    LinearProgressIndicator(
                        progress = { pct },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    )
                    if (pct >= 1f) {
                        Text(
                            text = "Limit exceeded",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AiFeedbackCard(
    tips: List<AiTip>,
    improvements: List<ImprovementIdea>,
    onAction: NavManager?
) {
    val ctx = LocalContext.current

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("AI Insights & Improvements", style = MaterialTheme.typography.titleMedium)
            tips.forEach { tip ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(tip.title, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text(tip.detail, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
            if (improvements.isNotEmpty()) {
                FlowRowWrap(spacing = 8.dp) {
                    improvements.forEach { imp ->
                        SuggestionChip(
                            onClick = {
//                                onAction?.navigate(ScreenRoutes.AddExpense.route)
//                                onAction(HomeAction.ApplyImprovement(imp.title))
                                Toast.makeText(ctx,"Clicked On Clip", Toast.LENGTH_SHORT).show()
                            },
                            label = { Text(imp.title) }
                        )
                    }
                }
                Text(
                    "Tap a suggestion to apply quick improvements",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun PendingApprovalsCard(
    items: List<ApprovalItem>,
    currency: String,
    onAction: NavManager?
) {
    val ctx = LocalContext.current

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Pending approvals", style = MaterialTheme.typography.titleMedium)
                AssistChip(onClick = { /* open all */ }, label = { Text("View all") })
            }
            items.take(3).forEach { item ->
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(item.title, fontWeight = FontWeight.Medium)
                            Text(
                                "₹ ${item.amount.roundToInt()} • ₹ ${item.staffName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(onClick = {
//                                onAction(HomeAction.Reject(item.id))
                                Toast.makeText(ctx, "OT ->BTN", Toast.LENGTH_SHORT).show()
                            }
                            ) { Text("Reject") }
                            Button(onClick = {
//                                onAction(HomeAction.Approve(item.id))
                            } ) {
                                Icon(Icons.Outlined.CheckCircle, contentDescription = null)
                                Spacer(Modifier.width(6.dp))
                                Text("Approve")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentActivityCard(
    recent: List<ExpenseItem>,
    currency: String,
    onAction: NavManager?
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Recent activity", style = MaterialTheme.typography.titleMedium)
            recent.take(5).forEach { item ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(item.title, fontWeight = FontWeight.Medium)
                        Text(
                            "₹ ${item.category} • ₹ ${item.time}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text("₹ ${item.amount.roundToInt()}", fontWeight = FontWeight.SemiBold)
                }
                Divider()
            }
        }
    }
}

@Composable
private fun QuickActionsRow(onAction: NavManager?) {
    val ctx = LocalContext.current

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        QuickActionButton("Add Expense", Icons.Outlined.Add) {
//            onAction(HomeAction.AddExpense)
            Toast.makeText(ctx,"Add", Toast.LENGTH_SHORT).show()
        }
        QuickActionButton("Scan", Icons.Outlined.CameraAlt) {
//            onAction(HomeAction.ScanReceipt)
            Toast.makeText(ctx,"Scan", Toast.LENGTH_SHORT).show()

        }
        QuickActionButton("Reports", Icons.Outlined.Assessment) {
//            onAction(HomeAction.OpenReports)
            Toast.makeText(ctx,"Reports", Toast.LENGTH_SHORT).show()

        }
        QuickActionButton("Send", Icons.AutoMirrored.Outlined.Send) { /* share/export */ }
    }
}

@Composable
private fun QuickActionButton(label: String, icon: ImageVector, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier/*.weight(1f)*/
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}

// ---------- REUSABLES ----------

@Composable
private fun SimpleBarChart(
    data: List<Double>,
    labels: List<String>,
    barWidth: Dp,
    chartHeight: Dp = 140.dp,
    barSpacing: Dp = 12.dp,
    cornerRadius: Dp = 6.dp,
) {
    val max = (data.maxOrNull() ?: 1.0).coerceAtLeast(1.0)
    val density = LocalDensity.current
    val heightPx = with(density) { chartHeight.toPx() }
    val barWidthPx = with(density) { barWidth.toPx() }
    val spacingPx = with(density) { barSpacing.toPx() }
    val corner = with(density) { cornerRadius.toPx() }

    Column(Modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp)
        ) {
            var x = 16f
            data.forEach { value ->
                val h = (value / max * (heightPx - 24f)).toFloat()
                drawRoundRect(
//                    color = MaterialTheme.colorScheme.primary,
                    color = Color.Blue,
                    topLeft = Offset(x, size.height - h - 8f),
                    size = androidx.compose.ui.geometry.Size(barWidthPx, h),
                    cornerRadius = CornerRadius(corner, corner)
                )
                x += barWidthPx + spacingPx
            }
        }
        Spacer(Modifier.height(6.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            labels.forEach { label ->
                Text(label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FlowRowWrap(
    spacing: Dp = 8.dp,
    content: @Composable () -> Unit
) {
    // Simple wrap using Column + Row blocks (no experimental APIs)
    val placeables = remember { mutableStateListOf<@Composable () -> Unit>() }
    placeables.clear()
    placeables.add(content)
    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
        // Render single row content (chips will naturally wrap if you use FlowRow from foundation in future)
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) { content() }
    }
}

private fun progressFrom(part: Double, total: Double): Float {
    if (total <= 0) return 0f
    return (part / total).toFloat().coerceIn(0f, 1f)
}

// ---------- PREVIEW ----------

@Preview(showBackground = true, showSystemUi = true, )
@Composable
private fun HomeScreenPreview() {
    val sample = HomeUiState(
        userName = "Abhishek",
        todayTotal = 2350.0,
        monthExpense = 45210.0,
        monthIncome = 75000.0,
        categoryBreakdown = listOf(
            CategorySlice("Travel", 12000.0),
            CategorySlice("Food", 9800.0),
            CategorySlice("Staff", 18800.0),
            CategorySlice("Utility", 4600.0)
        ),
        weeklyTrend = listOf(
            DailyPoint("Mon", 4200.0),
            DailyPoint("Tue", 1800.0),
            DailyPoint("Wed", 2600.0),
            DailyPoint("Thu", 3900.0),
            DailyPoint("Fri", 1600.0),
            DailyPoint("Sat", 5100.0),
            DailyPoint("Sun", 2200.0)
        ),
        budgets = listOf(
            BudgetProgress("Travel", 12000.0, 10000.0),
            BudgetProgress("Food", 9800.0, 12000.0),
            BudgetProgress("Staff", 18800.0, 20000.0)
        ),
        aiTips = listOf(
            AiTip("Travel is trending high", "Consider switching to monthly passes. You’re 20% above last month."),
            AiTip("Food spike on weekends", "Batch-order supplies midweek to avoid surge pricing.")
        ),
        improvements = listOf(
            ImprovementIdea("Set Travel budget", "Set"),
            ImprovementIdea("Enable daily reminders", "Enable"),
            ImprovementIdea("Scan receipts", "Scan")
        ),
        pendingApprovals = listOf(
            ApprovalItem("1", "Rohit", "Cab from client visit", 540.0, true),
            ApprovalItem("2", "Sneha", "Lunch with vendor", 920.0, true)
        ),
        recent = listOf(
            ExpenseItem("1", "Printer ink", "Utility", 780.0, "10:24 AM"),
            ExpenseItem("2", "Team lunch", "Food", 2450.0, "Yesterday"),
            ExpenseItem("3", "Airport taxi", "Travel", 820.0, "Yesterday")
        )
    )
    SmartExpenseTrackerTheme {
        HomeScreen(null,sample)
    }
}