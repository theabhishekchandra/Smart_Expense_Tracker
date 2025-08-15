package com.abhishek.smartexpensetracker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.utils.ExportUtils
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    expenses: List<Expense>,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val dailyTotals = remember(expenses) {
        expenses.groupBy { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.timestamp)) }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    val categoryTotals = remember(expenses) {
        expenses.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }

    Scaffold(
        topBar = {
            ReportTopBar(
                onBack = onBack,
                onExport = {
                    val csv = ExportUtils.buildCsvFromExpenses(expenses)
                    val uri = ExportUtils.writeCsvToCache(context, "expenses.csv", csv)
                    if (uri != null) {
                        Toast.makeText(context, "Exported to cache", Toast.LENGTH_SHORT).show()
                    }
                },
                onShare = {
                    val csv = ExportUtils.buildCsvFromExpenses(expenses)
                    val uri = ExportUtils.writeCsvToCache(context, "expenses.csv", csv)
                    if (uri != null) {
                        ExportUtils.shareCsv(context, uri)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            item {
                SectionTitle("Daily Totals")
                TotalsList(data = dailyTotals)
            }

            item {
                SectionTitle("Category Totals")
                TotalsList(data = categoryTotals)
            }

            item {
                SectionTitle("Daily Overview (Bar Chart)")
                DailyBarChart(dailyTotals)
            }

            item {
                SectionTitle("All Expenses")
            }
            items(expenses) { e ->
                ExpenseRow(expense = e)
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
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
fun DailyBarChart(dailyTotals: Map<String, Double>) {
    val maxTotal = dailyTotals.values.maxOrNull() ?: 1.0
    Column {
        dailyTotals.forEach { (day, total) ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(
                    text = day.substring(5), // just show MM-dd
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
    Divider()
}

/* ---------------- Preview ---------------- */

@Preview(showBackground = true)
@Composable
fun PreviewReportScreen() {
    val now = System.currentTimeMillis()
    val demo = listOf(
        Expense(title = "Tea", amount = 10.0, category = "Food", notes = "", receiptUri = null, timestamp = now),
        Expense(title = "Bus", amount = 20.0, category = "Transport", notes = "", receiptUri = null, timestamp = now - 86400000L),
        Expense(title = "Lunch", amount = 100.0, category = "Food", notes = "", receiptUri = null, timestamp = now - 2 * 86400000L)
    )
    ReportScreen(expenses = demo, onBack = {})
}
