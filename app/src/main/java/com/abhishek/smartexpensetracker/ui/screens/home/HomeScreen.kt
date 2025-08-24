package com.abhishek.smartexpensetracker.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.smartexpensetracker.core.datastore.Currency
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.data.model.ExpenseStatus
import com.abhishek.smartexpensetracker.ui.components.BaseScaffold
import com.abhishek.smartexpensetracker.ui.screens.staff.Role
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navManager: NavManager? = null,
    viewModel: HomeViewModel
) {
//    val uiState by viewModel.homeUiStateA.collectAsState()
    val state by viewModel.uiState.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val isPremium by viewModel.isPremium.collectAsState()
    val isBusiness by viewModel.isBusiness.collectAsState()

    val ctx = LocalContext.current

    BaseScaffold(
        navManager = navManager,
        currentRoute = ScreenRoutes.Home.route,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = state.userNameOrNull ?: "Guest",
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (isBusiness) "Business Dashboard" else "Track smart. Save smart.",
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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navManager?.navigate(ScreenRoutes.AddExpense.route) },
                icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                text = { Text("Add Expense") }
            )
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                when(state){
                    is HomeUiState.Loading -> {
                        item { CircularProgressIndicator() }
                    }
                    is HomeUiState.Error -> {
//                        item { ErrorCard((state as HomeUiState.Error).message) }
                        Toast.makeText(ctx, (state as HomeUiState.Error).message, Toast.LENGTH_SHORT).show()
                    }
                    is HomeUiState.BusinessDashboard -> {
                        val s = state as HomeUiState.BusinessDashboard
                        item { TodaySummaryCard(currency!!, s.totalExpense) }
                        item { IncomeVsExpenseCard(currency!!, s.monthlyExpense, s.monthlyIncome) }
                        if (s.weeklyTrend.isNotEmpty()) item { WeeklyTrendCard(s.weeklyTrend, currency) }
                        if (s.approvalRecordList.isNotEmpty()) item { PendingApprovalsCard(s.approvalRecordList, currency) }
                        if (s.staffSpendingList.isNotEmpty()) item { StaffLeaderboardCard(s.staffSpendingList, currency) }

                    }
                    is HomeUiState.PersonalDashboard -> {
                        val s = state as HomeUiState.PersonalDashboard
                        item { TodaySummaryCard(currency!!, s.totalExpense) }
                        item { IncomeVsExpenseCard(currency!!, s.monthlyExpense, s.monthlyIncome) }
                        if (s.categoryBreakdown.isNotEmpty()) item { CategoryBreakdownCard(s.categoryBreakdown, currency) }
                        if (s.budgetProgressList.isNotEmpty()) item { BudgetsCard(s.budgetProgressList, currency) }
                    }

                }

                // AI feedback (common for both modes)
                if (state.AITips?.isNotEmpty() == true || state.ImprovementIdeas?.isNotEmpty() == true) {
                    item { AiFeedbackCard(state.AITips, state.ImprovementIdeas, navManager) }
                }

                // Recent activity (common)
                if (state.recent.isNotEmpty()) {
                    item { RecentActivityCard(state.recent, currency, navManager) }
                }

//                item { QuickActionsRow(navManager) }
                item { Spacer(Modifier.height(64.dp)) }

            }
        }
    )
}


// ---------- SECTION CARDS ----------

@Composable
private fun StaffLeaderboardCard(
    leaderboard: List<StaffSpending>,
    currency: Currency?
) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Staff Leaderboard", style = MaterialTheme.typography.titleMedium)
            leaderboard.take(5).forEachIndexed { index, staff ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${index + 1}. ${staff.staffName}", fontWeight = FontWeight.Medium)
                    Text("₹ ${staff.amount.roundToInt()}", fontWeight = FontWeight.SemiBold)
                }
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}


@Composable
private fun TodaySummaryCard(currency: Currency, totalExpense: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Today’s spend", style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${currency.symbol} ${totalExpense.roundToInt()}",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(8.dp))
            // TODO : We can add If need in future.
            /*Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AssistChip(onClick = { }, label = { Text("Scan receipt") }, leadingIcon = {
                    Icon(Icons.Outlined.CameraAlt, contentDescription = null)
                })
                AssistChip(onClick = { }, label = { Text("Open reports") }, leadingIcon = {
                    Icon(Icons.Outlined.Assessment, contentDescription = null)
                })
            }*/
        }
    }
}

@Composable
private fun IncomeVsExpenseCard(currency: Currency, monthExpense : Double, monthIncome : Double) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("This month", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                StatPill(title = "Income", value = monthIncome, currency = currency)
                StatPill(title = "Expense", value = monthExpense, currency = currency)
                val net = monthIncome - monthExpense
                StatPill(title = "Net", value = net, currency = currency)
            }
        }
    }
}

@Composable
private fun StatPill(title: String, value: Double, currency: Currency) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("${currency.symbol} ${value.roundToInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun WeeklyTrendCard(points: List<DailyPoint>, currency: Currency?) {
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
private fun CategoryBreakdownCard(slices: List<CategorySlice>, currency: Currency?) {
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
private fun BudgetsCard(items: List<BudgetProgress>, currency: Currency?) {
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
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AiFeedbackCard(
    tips: List<AiTip>?,
    improvements: List<ImprovementIdea>?,
    onAction: NavManager?
) {
    val ctx = LocalContext.current

    Card(Modifier.fillMaxWidth()) {
        Column(
            Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title
            Text(
                "AI Insights & Improvements",
                style = MaterialTheme.typography.titleMedium
            )

            // AI Tips Section
            tips?.forEach { tip ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                    )
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(tip.title, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            tip.detail,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Suggestions Chips Section
            if (improvements?.isNotEmpty() == true) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    improvements.forEach { imp ->
                        SuggestionChip(
                            onClick = {
                                Toast.makeText(ctx, "Clicked for ${imp.actionLabel}", Toast.LENGTH_SHORT).show()
                            },
                            label = { Text(imp.title) },

                            colors = SuggestionChipDefaults.suggestionChipColors(
                                // TODO: Change Color they show some highlight.
                                containerColor = MaterialTheme.colorScheme.background
                            )
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
    items: List<ApprovalRecord>,
    currency: Currency?,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Pending Approvals",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(onClick = {  }) {
                    Text("View all")
                }
            }

            // Approval Items
            items.take(3).forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Side (Info)
                    Column(Modifier.weight(1f)) {
                        Text(item.title, fontWeight = FontWeight.Medium)
                        Text(
                            "$currency ${item.amount.roundToInt()} • ${item.staffName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Right Side (Actions)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {  },
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = "Reject",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        Spacer(Modifier.width(2.dp))
                        IconButton(
                            onClick = {  },
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                Icons.Outlined.Check,
                                contentDescription = "Approve",
                                tint = MaterialTheme.colorScheme.primary
                            )
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
    currency: Currency?,
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
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
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
                    size = Size(barWidthPx, h),
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
    val placeable = remember { mutableStateListOf<@Composable () -> Unit>() }
    placeable.clear()
    placeable.add(content)
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

    // Dummy data list
    val dummyStaffSpending = listOf(
        StaffSpending(
            id = 1,
            staffId = 101,
            staffName = "Ravi Sharma",
            role = Role.Admin,
            amount = 1200.0,
            category = "Travel",
            description = "Taxi to client office",
            date = System.currentTimeMillis(),
            status = ExpenseStatus.PENDING
        ),
        StaffSpending(
            id = 2,
            staffId = 102,
            staffName = "Neha Gupta",
            role = Role.EntryOnly,
            amount = 500.0,
            category = "Food",
            description = "Lunch with client",
            date = System.currentTimeMillis() - 86400000, // 1 day ago
            status = ExpenseStatus.APPROVED,
            approverId = 201,
            approverName = "Amit Verma",
            notes = "Valid expense"
        ),
        StaffSpending(
            id = 3,
            staffId = 103,
            staffName = "Amit Verma",
            role = Role.Approver,
            amount = 3000.0,
            category = "Office Supplies",
            description = "Stationery purchase",
            date = System.currentTimeMillis() - 2 * 86400000, // 2 days ago
            status = ExpenseStatus.REJECTED,
            approverId = 201,
            approverName = "Ravi Sharma",
            notes = "Need prior approval"
        ),
        StaffSpending(
            id = 4,
            staffId = 104,
            staffName = "Priya Singh",
            role = Role.Viewer,
            amount = 1500.0,
            category = "Travel",
            description = "Flight ticket booking",
            date = System.currentTimeMillis() - 3 * 86400000, // 3 days ago
            status = ExpenseStatus.APPROVED,
            approverId = 201,
            approverName = "Amit Verma",
            notes = "Approved for client visit"
        )
    )

    val sample = HomeUiStateA(
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
            ApprovalRecord("1", "Rohit", "Cab from client visit", 540.0, true),
            ApprovalRecord("2", "Sneha", "Lunch with vendor", 920.0, true)
        ),
        recent = listOf(
            ExpenseItem("1", "Printer ink", "Utility", 780.0, "10:24 AM"),
            ExpenseItem("2", "Team lunch", "Food", 2450.0, "Yesterday"),
            ExpenseItem("3", "Airport taxi", "Travel", 820.0, "Yesterday")
        ),
        staffLeaderboard = dummyStaffSpending
    )
    SmartExpenseTrackerTheme(
        true,
        false
    ){
        HomeScreen(null, hiltViewModel())
//        PendingApprovalsCard(sample.pendingApprovals, sample.currency, )
//        AiFeedbackCard(sample.aiTips, sample.improvements, null)

    }
}