package com.abhishek.spendly.ui.screens.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.Color
import com.abhishek.spendly.ui.components.AnimatedAmountText
import com.abhishek.spendly.ui.components.GradientCard
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.CurrencyRupee
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.spendly.core.datastore.Currency
import com.abhishek.spendly.core.navigation.NavManager
import com.abhishek.spendly.core.navigation.ScreenRoutes
import com.abhishek.spendly.data.model.ExpenseStatus
import com.abhishek.spendly.ui.components.BaseScaffold
import com.abhishek.spendly.ui.screens.staff.Role
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.DangerColor
import com.abhishek.spendly.ui.theme.DangerColorDark
import com.abhishek.spendly.ui.theme.SpendlyTheme
import com.abhishek.spendly.ui.theme.SuccessColor
import com.abhishek.spendly.ui.theme.SuccessColorDark
import com.abhishek.spendly.ui.theme.WarningColor
import com.abhishek.spendly.ui.theme.WarningColorDark
import com.abhishek.spendly.ui.theme.isAppDarkTheme
import kotlin.collections.take
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
    val  borrowRecord by viewModel.borrowRecord.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var menuExpanded by remember { mutableStateOf(false) }

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
                    IconButton(onClick = { navManager?.navigate(ScreenRoutes.Settings.route) }) {
                        Icon(Icons.Outlined.Language, contentDescription = "Change language")
                    }
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Outlined.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                            DropdownMenuItem(
                                text = { Text("Refresh") },
                                onClick = { menuExpanded = false; viewModel.refreshHome() }
                            )
                            if (isBusiness) {
                                DropdownMenuItem(
                                    text = { Text("Staff Dashboard") },
                                    onClick = { menuExpanded = false; navManager?.navigate(ScreenRoutes.StaffDashboard.route) }
                                )
                            }
                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = { menuExpanded = false; navManager?.navigate(ScreenRoutes.Settings.route) }
                            )
                        }
                    }
                }
            )
        },
        /*floatingActionButton = {
            *//*ExtendedFloatingActionButton(
                onClick = { navManager?.navigate(ScreenRoutes.AddExpense.route) },
                icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                text = { Text("Add Expense") }
            )*//*

        },*/
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    if (!isBusiness){
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                        ) {
                            FabMenuItem(
                                icon = Icons.Default.Person,
                                text = "Add Expense",
                                onClick = { expanded = false
                                    navManager?.navigate(ScreenRoutes.AddExpense.route) }
                            )
                            FabMenuItem(
                                icon = Icons.Default.Money,
                                text = "Add Income",
                                onClick = { expanded = false
                                    Toast.makeText(ctx, "Coming Soon...", Toast.LENGTH_SHORT).show() }
                            )
                            FabMenuItem(
                                icon = Icons.Default.ShoppingCart,
                                text = "Add Lender",
                                onClick = { expanded = false
                                    navManager?.navigate(ScreenRoutes.AddLender.route) }
                            )
                        }
                    } else {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                        ) {
                            FabMenuItem(
                                icon = Icons.Default.Person,
                                text = "Add Expense",
                                onClick = { expanded = false
                                navManager?.navigate(ScreenRoutes.AddExpense.route) }
                            )
                            FabMenuItem(
                                icon = Icons.Default.Money,
                                text = "Add Sale",
                                onClick = { expanded = false
                                    Toast.makeText(ctx, "Coming Soon...", Toast.LENGTH_SHORT).show() }
                            )
                            FabMenuItem(
                                icon = Icons.Default.ShoppingCart,
                                text = "Add Lender",
                                onClick = { expanded = false
                                    navManager?.navigate(ScreenRoutes.AddLender.route) }
                            )
                        }
                    }
                }

                // Main FAB
                FloatingActionButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(AppSpacing.md)
                    .clickable{ expanded = false},
                verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
            ) {

                when(state){
                    is HomeUiState.Loading -> {
                        item {
                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
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
                        if (s.approvalRecordList.isNotEmpty()) item {
                            PendingApprovalsCard(
                                items = s.approvalRecordList,
                                currency = currency,
                                onViewAll = { navManager?.navigate(ScreenRoutes.Approval.route) },
                                onApprove = { item -> viewModel.approveApproval(item.id) },
                                onReject = { item -> viewModel.rejectApproval(item.id) }
                            )
                        }
                        if (s.staffSpendingList.isNotEmpty()) item {
                            StaffLeaderboardCard(
                                leaderboard = s.staffSpendingList,
                                currency = currency,
                                onManageStaff = { navManager?.navigate(ScreenRoutes.StaffManagement.route) }
                            )
                        }
                        if (s.staffSpendingList.isNotEmpty()) item { StaffPerformanceCard(s.staffSpendingList, currency!!) }
                        item {
                            OutstandingDuesCard(
                                dues = borrowRecord,
                                currency = currency!!,
                                onViewAll = { navManager?.navigate(ScreenRoutes.LenderList.route) }
                            )
                        }
                    }
                    is HomeUiState.PersonalDashboard -> {
                        val s = state as HomeUiState.PersonalDashboard
                        item { TodaySummaryCard(currency!!, s.totalExpense) }
                        item { IncomeVsExpenseCard(currency!!, s.monthlyExpense, s.monthlyIncome) }
                        if (s.categoryBreakdown.isNotEmpty()) item { CategoryBreakdownCard(s.categoryBreakdown, currency) }
                        if (s.budgetProgressList.isNotEmpty()) item { BudgetsCard(s.budgetProgressList, currency) }
                        item { RecentTransactionsCard(s.recentExpenses,currency?: Currency.RUPEE) }
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
    currency: Currency?,
    onManageStaff: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(AppSpacing.md), verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Staff Leaderboard", style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onManageStaff) {
                    Text("Manage", style = MaterialTheme.typography.labelLarge)
                }
            }
            leaderboard.take(5).forEachIndexed { index, staff ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${index + 1}. ${staff.staffName}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "₹ ${staff.amount.roundToInt()}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}


@Composable
private fun TodaySummaryCard(currency: Currency, totalExpense: Double) {
    GradientCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Today's spend",
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(alpha = 0.85f)
        )
        Spacer(Modifier.height(AppSpacing.xs))
        AnimatedAmountText(
            amount = totalExpense,
            style = MaterialTheme.typography.displaySmall,
            color = Color.White,
            prefix = "${currency.symbol} ",
            decimals = 0
        )
    }
}

@Composable
private fun IncomeVsExpenseCard(currency: Currency, monthExpense : Double, monthIncome : Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(AppSpacing.md), verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
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
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.sm),
        horizontalAlignment = Alignment.Start
    ) {
        Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            "${currency.symbol} ${value.roundToInt()}",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
private fun WeeklyTrendCard(points: List<DailyPoint>, currency: Currency?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(AppSpacing.md)) {
            Text("Weekly trend", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(AppSpacing.sm))
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(AppSpacing.md), verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            Text("Category breakdown", style = MaterialTheme.typography.titleMedium)
            slices.forEach { slice ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(slice.name, style = MaterialTheme.typography.bodyMedium)
                    Text("₹ ${slice.amount.roundToInt()}", style = MaterialTheme.typography.titleSmall)
                }
                LinearProgressIndicator(
                    progress = { progressFrom(slice.amount, slices.sumOf { it.amount }) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(MaterialTheme.shapes.extraSmall)
                )
            }
        }
    }
}

@Composable
private fun BudgetsCard(items: List<BudgetProgress>, currency: Currency?) {
    val isDark = isAppDarkTheme()
    val symbol = currency?.symbol ?: "₹"
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(AppSpacing.md), verticalArrangement = Arrangement.spacedBy(AppSpacing.md)) {
            Text("Budgets", style = MaterialTheme.typography.titleMedium)
            items.forEachIndexed { index, b ->
                val pct = (b.spent / (b.limit.coerceAtLeast(1.0))).toFloat().coerceIn(0f, 1f)
                val status = when {
                    pct >= 1f -> "Exceeded"
                    pct >= 0.8f -> "Nearing limit"
                    else -> "On track"
                }
                val statusColor = when (status) {
                    "Exceeded" -> if (isDark) DangerColorDark else DangerColor
                    "Nearing limit" -> if (isDark) WarningColorDark else WarningColor
                    else -> if (isDark) SuccessColorDark else SuccessColor
                }
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            b.category,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Surface(
                            shape = MaterialTheme.shapes.extraSmall,
                            color = statusColor.copy(alpha = 0.14f)
                        ) {
                            Text(
                                status,
                                style = MaterialTheme.typography.labelSmall,
                                color = statusColor,
                                modifier = Modifier.padding(horizontal = AppSpacing.sm, vertical = 2.dp)
                            )
                        }
                    }
                    LinearProgressIndicator(
                        progress = { pct },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(MaterialTheme.shapes.extraSmall),
                        color = statusColor,
                        trackColor = statusColor.copy(alpha = 0.16f)
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "$symbol ${b.spent.roundToInt()} of $symbol ${b.limit.roundToInt()}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "${(pct * 100).roundToInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = statusColor,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                if (index != items.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                }
            }
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiFeedbackCard(
    tips: List<AiTip>?,
    improvements: List<ImprovementIdea>?,
    onAction: NavManager?
) {
    val ctx = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(AppSpacing.lg)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
            horizontalAlignment = Alignment.Start
        ) {
            // Title with AI Glow Effect
            Text(
                text = "🤖 AI Insights & Improvements",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            // AI Tips Section
            tips?.forEach { tip ->
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(AppSpacing.md),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)
                    ) {
                        Text(
                            tip.title,
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            tip.detail,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Suggestion Chips Section
            if (improvements?.isNotEmpty() == true) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
                ) {
                    improvements.forEach { imp ->
                        SuggestionChip(
                            onClick = {
                                Toast.makeText(ctx, "Clicked for ${imp.actionLabel}", Toast.LENGTH_SHORT).show()
                            },
                            label = {
                                Text(
                                    imp.title,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            shape = MaterialTheme.shapes.medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(AppSpacing.xs))

                // Info Text
                Text(
                    "💡 Tap a suggestion to apply quick improvements",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}


@Composable
private fun PendingApprovalsCard(
    items: List<ApprovalRecord>,
    currency: Currency?,
    onViewAll: () -> Unit = {},
    onApprove: (ApprovalRecord) -> Unit = {},
    onReject: (ApprovalRecord) -> Unit = {},
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Pending Approvals",
                    style = MaterialTheme.typography.titleMedium
                )
                TextButton(onClick = onViewAll) {
                    Text("View all", style = MaterialTheme.typography.labelLarge)
                }
            }

            // Approval Items
            items.take(3).forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(AppSpacing.sm),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Side (Info)
                    Column(Modifier.weight(1f)) {
                        Text(item.title, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "$currency ${item.amount.roundToInt()} • ${item.staffName}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Right Side (Actions)
                    Row(horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
                        IconButton(
                            onClick = { onReject(item) },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = "Reject ${item.title}",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                        IconButton(
                            onClick = { onApprove(item) },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                Icons.Outlined.Check,
                                contentDescription = "Approve ${item.title}",
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(AppSpacing.md), verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
            Text("Recent activity", style = MaterialTheme.typography.titleMedium)
            recent.take(5).forEach { item ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(item.title, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "₹ ${item.category} • ₹ ${item.time}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text("₹ ${item.amount.roundToInt()}", style = MaterialTheme.typography.titleSmall)
                }
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}

@Composable
private fun QuickActionsRow(onAction: NavManager?) {
    val ctx = LocalContext.current

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)) {
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
        shape = MaterialTheme.shapes.small,
        modifier = Modifier/*.weight(1f)*/
    ) {
        Icon(icon, contentDescription = null)
        Spacer(Modifier.width(AppSpacing.xs))
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun FabMenuItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        icon = { Icon(icon, contentDescription = text) },
        text = { Text(text, style = MaterialTheme.typography.labelLarge) },
        onClick = onClick
    )
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
    val barColor = MaterialTheme.colorScheme.primary

    Column(Modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(chartHeight)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(AppSpacing.sm)
        ) {
            var x = 16f
            data.forEach { value ->
                val h = (value / max * (heightPx - 24f)).toFloat()
                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(x, size.height - h - 8f),
                    size = Size(barWidthPx, h),
                    cornerRadius = CornerRadius(corner, corner)
                )
                x += barWidthPx + spacingPx
            }
        }
        Spacer(Modifier.height(AppSpacing.xs))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            labels.forEach { label ->
                Text(label, style = MaterialTheme.typography.labelSmall, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FlowRowWrap(
    spacing: Dp = AppSpacing.sm,
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

// ---------- NEW BUSINESS DASHBOARD CARDS ----------

@Composable
private fun TodaySalesExpenseCard(
    sales: Double,
    expense: Double,
    currency: Currency
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            Modifier.padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Text("Today's Sales & Expenses", style = MaterialTheme.typography.titleMedium)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatPill(title = "Sales", value = sales, currency = currency)
                StatPill(title = "Expenses", value = expense, currency = currency)
                StatPill(title = "Net", value = sales - expense, currency = currency)
            }
        }
    }
}

@Composable
private fun OutstandingDuesCard(
    dues: List<BorrowerRecord>,
    currency: Currency,
    onViewAll: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            Modifier.padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Text("Outstanding Dues", style = MaterialTheme.typography.titleMedium)
            if (dues.isEmpty()) {
                Text("No dues pending 🎉", style = MaterialTheme.typography.bodyMedium)
            } else {
                dues.take(5).forEach { record ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(record.borrowerName, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "Due: ${record.dueDate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            "${currency.symbol}${record.borrowedAmount.roundToInt()}",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    HorizontalDivider()
                }
                TextButton(onClick = onViewAll) {
                    Text("View All", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
private fun StaffPerformanceCard(
    staffStats: List<StaffSpending>,
    currency: Currency
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            Modifier.padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Text("Staff Performance", style = MaterialTheme.typography.titleMedium)
            staffStats.take(5).forEach { staff ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(staff.staffName, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${currency.symbol}${staff.amount.roundToInt()}",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
                HorizontalDivider()
            }
        }
    }
}

// ---------- PERSONAL DASHBOARD EXTRA CARD ----------

@Composable
private fun RecentTransactionsCard(
    recent: List<ExpenseItem>,
    currency: Currency
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            Modifier.padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Text("Recent Transactions", style = MaterialTheme.typography.titleMedium)
            if (recent.isEmpty()) {
                Text("No recent transactions yet.", style = MaterialTheme.typography.bodyMedium)
            } else {
                recent.take(5).forEach { item ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(item.title, style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "${item.category} • ${item.time}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            "${currency.symbol}${item.amount.roundToInt()}",
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                    HorizontalDivider()
                }
            }
        }
    }
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
    SpendlyTheme(
        true,

    ){
//        HomeScreen(null, hiltViewModel())
//        PendingApprovalsCard(sample.pendingApprovals, sample.currency, )
        AiFeedbackCard(sample.aiTips, sample.improvements, null)

    }
}