package com.abhishek.smartexpensetracker.ui.screens.expense

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.abhishek.smartexpensetracker.data.model.Expense
import com.abhishek.smartexpensetracker.data.model.UserRole
import com.abhishek.smartexpensetracker.data.model.ExpenseStatus
import com.abhishek.smartexpensetracker.ui.components.BaseScaffold
import com.abhishek.smartexpensetracker.core.navigation.NavManager
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.data.model.ExpenseDM
import com.abhishek.smartexpensetracker.ui.screens.login.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailScreen(
    expense: ExpenseDM,
    userRole: UserRole,
    navManager: NavManager? = null,
    viewModel: ExpenseViewModel? = hiltViewModel()
) {
    val context = LocalContext.current

    BaseScaffold(
        topBar = {
            TopAppBar(title = { Text("Expense Detail") })
        },
        navManager = navManager,
        currentRoute = ScreenRoutes.ExpenseDetail.route,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(expense.title, style = MaterialTheme.typography.titleLarge)
                Text("Amount: ₹${"%.2f".format(expense.amount)}", style = MaterialTheme.typography.titleMedium)
                Text("Category: ${expense.category}", style = MaterialTheme.typography.bodyMedium)
                Text("Notes: ${expense.notes ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Date: ${SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(expense.timestamp))}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (!expense.userName.isNullOrEmpty()) {
                    Text("Added by: ${expense.userName}", style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray))
                }

                // Receipt Image
                expense.receiptUri?.let {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = "Receipt",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                    // Approve/Reject only for Admin or Approver
                    if (userRole == UserRole.ADMIN || userRole == UserRole.APPROVER) {
                        Button(
                            onClick = {
//                                viewModel?.approveExpense(expense)
                                Toast.makeText(context, "Approved ${expense.title}", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Approve")
                            Spacer(Modifier.width(8.dp))
                            Text("Approve")
                        }

                        Button(
                            onClick = {
//                                viewModel?.rejectExpense(expense)
                                Toast.makeText(context, "Rejected ${expense.title}", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Reject")
                            Spacer(Modifier.width(8.dp))
                            Text("Reject")
                        }
                    }

                    // Edit/Delete only for Personal, Admin, Entry Only
                    if (userRole == UserRole.PERSONAL || userRole == UserRole.ADMIN || userRole == UserRole.ENTRY_ONLY) {
                        Button(
                            onClick = { viewModel?.editExpense(expense) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                            Spacer(Modifier.width(8.dp))
                            Text("Edit")
                        }

                        Button(
                            onClick = {
                                viewModel?.deleteExpense(expense)
                                Toast.makeText(context, "Deleted ${expense.title}", Toast.LENGTH_SHORT).show()
                                navManager?.navigateBack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                            Spacer(Modifier.width(8.dp))
                            Text("Delete")
                        }
                    }
                }
            }
        }
    )
}
