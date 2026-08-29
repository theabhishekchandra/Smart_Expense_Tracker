package com.abhishek.spendly.ui.screens.lender

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import com.abhishek.spendly.ui.theme.isAppDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.ui.components.AnimatedAmountText
import com.abhishek.spendly.ui.theme.AppShapes
import com.abhishek.spendly.ui.theme.AppSpacing
import com.abhishek.spendly.ui.theme.DangerColor
import com.abhishek.spendly.ui.theme.DangerColorDark
import com.abhishek.spendly.ui.theme.SuccessColor
import com.abhishek.spendly.ui.theme.SuccessColorDark
import com.abhishek.spendly.ui.theme.WarningColor
import com.abhishek.spendly.ui.theme.WarningColorDark
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LenderBorrowerListScreen(
    list: List<LenderBorrowerDM> = sampleLenderBorrowers(),
    onItemClick: (LenderBorrowerDM) -> Unit = {},
    onAddClick: () -> Unit = {},
    onEdit: (LenderBorrowerDM) -> Unit = {},
    onDelete: (LenderBorrowerDM) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filterStatus by remember { mutableStateOf<LoanStatus?>(null) }

    val filteredList = list.filter { item ->
        (searchQuery.text.isEmpty() || item.name.contains(searchQuery.text, ignoreCase = true)) &&
                (filterStatus == null || item.status == filterStatus)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lender & Borrower") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add lender or borrower")
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(AppSpacing.sm),
                label = { Text("Search by name or status") },
                singleLine = true
            )

            // Filter Chips
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.sm),
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.xs)
            ) {
                LoanStatus.entries.forEach { status ->
                    FilterChip(
                        selected = filterStatus == status,
                        onClick = { filterStatus = if (filterStatus == status) null else status },
                        label = { Text(status.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppSpacing.xs))

            // List
            if (filteredList.isEmpty()) {
                EmptyLenderBorrowerState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
                ) {
                    items(filteredList) { item ->
                        SwipeableLenderItem(
                            item = item,
                            onEdit = onEdit,
                            onDelete = onDelete,
                            onClick = onItemClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyLenderBorrowerState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Filled.SearchOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(AppSpacing.sm))
            Text(
                text = "No records found",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Swipeable List Item
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableLenderItem(
    item: LenderBorrowerDM,
    onEdit: (LenderBorrowerDM) -> Unit,
    onDelete: (LenderBorrowerDM) -> Unit,
    onClick: (LenderBorrowerDM) -> Unit
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
    ) {
        // Background actions
        Row(
            modifier = Modifier.matchParentSize().padding(horizontal = AppSpacing.md),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                onDelete(item)
                Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete ${item.name}",
                    tint = MaterialTheme.colorScheme.error
                )
            }
            IconButton(onClick = { onEdit(item) }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit ${item.name}",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Foreground Card
        Card(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .fillMaxWidth()
                .padding(vertical = AppSpacing.xs)
                .clickable { onClick(item) },
            shape = AppShapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(AppSpacing.sm),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(AppSpacing.xs))
                    LoanStatusBadge(status = item.status)
                }
                AnimatedAmountText(
                    amount = item.amount,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    decimals = 0
                )
            }
        }
    }
}

// ------------------ Data Models ------------------
enum class LoanStatus { PENDING, PAID, OVERDUE }

data class LenderBorrowerDM(
    val id: String,
    val name: String,
    val amount: Double,
    val status: LoanStatus
)

/** Semantic color for a [LoanStatus], shared by the lender/borrower screens. */
@Composable
fun loanStatusColor(status: LoanStatus): Color {
    val dark = isAppDarkTheme()
    return when (status) {
        LoanStatus.PENDING -> if (dark) WarningColorDark else WarningColor
        LoanStatus.PAID -> if (dark) SuccessColorDark else SuccessColor
        LoanStatus.OVERDUE -> if (dark) DangerColorDark else DangerColor
    }
}

/** Small rounded status chip used on lender/borrower cards. */
@Composable
fun LoanStatusBadge(status: LoanStatus, modifier: Modifier = Modifier) {
    val color = loanStatusColor(status)
    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.15f), AppShapes.small)
            .padding(horizontal = AppSpacing.sm, vertical = AppSpacing.xs)
    ) {
        Text(
            text = status.name,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

fun sampleLenderBorrowers() = listOf(
    LenderBorrowerDM("1", "Ramesh Kumar", 500.0, LoanStatus.PENDING),
    LenderBorrowerDM("1", "Ramesh Kumar", 500.0, LoanStatus.PENDING),
    LenderBorrowerDM("1", "Ramesh Kumar", 500.0, LoanStatus.PENDING),
    LenderBorrowerDM("1", "Ramesh Kumar", 500.0, LoanStatus.PENDING),
    LenderBorrowerDM("1", "Ramesh Kumar", 500.0, LoanStatus.PENDING),
    LenderBorrowerDM("2", "Sita Verma", 1200.0, LoanStatus.PAID),
    LenderBorrowerDM("3", "Amit Sharma", 300.0, LoanStatus.OVERDUE)
)

@Preview(showBackground = true)
@Composable
fun PreviewLenderBorrowerList() {
    LenderBorrowerListScreen()
}
