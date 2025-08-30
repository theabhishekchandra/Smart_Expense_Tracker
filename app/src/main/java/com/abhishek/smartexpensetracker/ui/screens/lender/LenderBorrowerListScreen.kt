package com.abhishek.smartexpensetracker.ui.screens.lender

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val context = LocalContext.current

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
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // 🔹 Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                label = { Text("Search by name or status") }
            )

            // 🔹 Filter Chips
            Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                LoanStatus.values().forEach { status ->
                    FilterChip(
                        selected = filterStatus == status,
                        onClick = { filterStatus = if (filterStatus == status) null else status },
                        label = { Text(status.name) },
                        modifier = Modifier.padding(end = 6.dp)
                    )
                }
            }

            // 🔹 List
            if (filteredList.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No records found")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
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

// 🔹 Swipeable List Item
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
            modifier = Modifier.matchParentSize().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red,
                modifier = Modifier.size(28.dp).clickable {
                    onDelete(item)
                    Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
                }
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.Blue,
                modifier = Modifier.size(28.dp).clickable {
                    onEdit(item)
                    Toast.makeText(context, "Edit ${item.name}", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Foreground Card
        Card(
            modifier = Modifier
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .fillMaxWidth()
                .padding(6.dp)
                .clickable { onClick(item) },
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Status: ${item.status.name}",
                        color = when (item.status) {
                            LoanStatus.PENDING -> Color(0xFFFF9800)
                            LoanStatus.PAID -> Color(0xFF4CAF50)
                            LoanStatus.OVERDUE -> Color.Red
                        }
                    )
                }
                Text(
                    "₹${item.amount}",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp)
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
