package com.abhishek.smartexpensetracker.ui.screens.entry

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.data.model.DateFilter
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.ui.components.BaseScaffold
import com.abhishek.smartexpensetracker.ui.components.CategorySelector
import com.abhishek.smartexpensetracker.ui.components.ReceiptUploader
import com.abhishek.smartexpensetracker.ui.components.SaveButton
import com.abhishek.smartexpensetracker.ui.components.TextInputField

import com.abhishek.smartexpensetracker.ui.screens.login.viewmodel.ExpenseViewModel
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddExpenseScreen(
    navManager: NavManager?,
    viewModel: ExpenseViewModel? = hiltViewModel()
) {
    val ctx = LocalContext.current

    val uiState by viewModel!!.uiState.collectAsState()
    var filterExpanded by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Staff") }
    var notes by remember { mutableStateOf("") }
    var receiptUri by remember { mutableStateOf<String?>(null) }
    var duplicateWarning by remember { mutableStateOf(false) }
    var showAnim by remember { mutableStateOf(false) }

    var existingExpenses by remember { mutableStateOf(emptyList<Expense>()) }

    val categories = listOf("Staff", "Travel", "Food", "Utility")
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        receiptUri = uri?.toString()
    }

    LaunchedEffect(Unit) {
        viewModel.loadExpenses(DateFilter.ALL)
        existingExpenses = uiState.expenses
    }

    BaseScaffold(
        topBar = { Text("Add Expense") },
        navManager = navManager,
        currentRoute = ScreenRoutes.AddExpense.route,
        floatingActionButton = {
            SaveButton(showAnim) {
                val amt = amount.toDoubleOrNull() ?: 0.0
                if (title.isBlank() || amt <= 0.0) {
                    Toast.makeText(
                        ctx,
                        if (title.isBlank()) "Title required" else "Amount > 0",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@SaveButton
                }

                val isDuplicate = existingExpenses.any {
                    it.title.equals(title.trim(), ignoreCase = true) &&
                            it.amount == amt && it.category == category
                }
                if (isDuplicate && !duplicateWarning) {
                    Toast.makeText(
                        ctx,
                        "Duplicate detected! Press Save again to confirm.",
                        Toast.LENGTH_LONG
                    ).show()
                    duplicateWarning = true
                    return@SaveButton
                }

                viewModel.addExpense(
                    Expense(
                        title = title.trim(), amount = amt, category = category,
                        notes = notes.take(100), receiptUri = receiptUri
                    )
                )
                showAnim = true
                duplicateWarning = false
                title = ""; amount = ""; category = "Staff"; notes = ""; receiptUri = null
                Toast.makeText(ctx, "Expense added", Toast.LENGTH_SHORT).show()
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextInputField(value = title, onValueChange = { title = it }, label = "Title")
                TextInputField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Amount (₹)",
                    isNumber = true
                )
                CategorySelector(
                    categories = categories,
                    selected = category,
                    onSelect = { category = it })
                TextInputField(
                    value = notes,
                    onValueChange = { if (it.length <= 100) notes = it },
                    label = "Notes (optional)"
                )
                ReceiptUploader(uri = receiptUri, onUpload = { launcher.launch("image/*") })
            }
        },
    )
}

/** --- Preview --- **/
@Preview(showBackground = true)
@Composable
fun PreviewEntryScreenReusable() {
    SmartExpenseTrackerTheme {
        AddExpenseScreen(
            null
        )
    }
}
