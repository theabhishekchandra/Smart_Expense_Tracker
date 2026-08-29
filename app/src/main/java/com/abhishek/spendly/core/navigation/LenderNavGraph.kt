package com.abhishek.spendly.core.navigation

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.abhishek.spendly.ui.screens.lender.AddLenderBorrowerScreen
import com.abhishek.spendly.ui.screens.lender.LenderBorrowerDetailsScreen
import com.abhishek.spendly.ui.screens.lender.LenderBorrowerListScreen
import com.abhishek.spendly.ui.screens.lender.LenderViewModel
import com.abhishek.spendly.ui.screens.lender.toListItem
import com.abhishek.spendly.ui.screens.lender.toPersonDetail

fun NavGraphBuilder.lenderNavGraph(navManager: NavManager) {
    navigation(
        startDestination = ScreenRoutes.LenderList.route,
        route = RoutesConst.LENDER_GRAPH
    ) {
        composable(ScreenRoutes.LenderList.route) { navBackStackEntry ->
            val viewModel: LenderViewModel = hiltViewModel(navBackStackEntry)
            val records by viewModel.records.collectAsState()
            val context = LocalContext.current

            LenderBorrowerListScreen(
                list = records.map { it.toListItem() },
                onItemClick = { item -> navManager.navigate(ScreenRoutes.LenderDetails.passLenderId(item.id)) },
                onAddClick = { navManager.navigate(ScreenRoutes.AddLender.route) },
                onEdit = { item -> navManager.navigate(ScreenRoutes.EditLender.passLenderId(item.id)) },
                onDelete = { item ->
                    viewModel.deleteRecord(item.id)
                    Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
                }
            )
        }

        composable(ScreenRoutes.AddLender.route) { navBackStackEntry ->
            val viewModel: LenderViewModel = hiltViewModel(navBackStackEntry)
            AddLenderBorrowerScreen(
                onSave = { name, mobile, amount, isGiven, dueDate, notes ->
                    viewModel.addRecord(name, mobile, amount, isGiven, dueDate, notes)
                    navManager.navigateBack()
                },
                onCancel = { navManager.navigateBack() }
            )
        }

        composable(
            route = ScreenRoutes.EditLender.route,
            arguments = RoutesConst.LENDER_DETAIL_ARGUMENT
        ) { navBackStackEntry ->
            val viewModel: LenderViewModel = hiltViewModel(navBackStackEntry)
            val id = navBackStackEntry.arguments?.getString(RoutesConst.LENDER_ID) ?: ""
            val record = viewModel.records.collectAsState().value.find { it.id == id }

            if (record != null) {
                AddLenderBorrowerScreen(
                    currentName = record.name,
                    currentMobile = record.mobile,
                    currentAmount = record.amount.toString(),
                    currentIsGiven = record.isGiven,
                    currentDueDate = record.dueDate,
                    currentNotes = record.notes,
                    onSave = { name, mobile, amount, isGiven, dueDate, notes ->
                        viewModel.updateRecord(id, name, mobile, amount, isGiven, dueDate, notes)
                        navManager.navigateBack()
                    },
                    onCancel = { navManager.navigateBack() }
                )
            } else {
                LaunchedEffect(Unit) { navManager.navigateBack() }
            }
        }

        composable(
            route = ScreenRoutes.LenderDetails.route,
            arguments = RoutesConst.LENDER_DETAIL_ARGUMENT
        ) { navBackStackEntry ->
            val viewModel: LenderViewModel = hiltViewModel(navBackStackEntry)
            val id = navBackStackEntry.arguments?.getString(RoutesConst.LENDER_ID) ?: ""
            val records by viewModel.records.collectAsState()
            val record = records.find { it.id == id }
            val context = LocalContext.current

            if (record != null) {
                LenderBorrowerDetailsScreen(
                    person = record.toPersonDetail(),
                    onMarkAsPaid = { viewModel.markAsPaid(id) },
                    onSendReminder = {
                        val message = "Hi ${record.name}, reminder: " +
                            (if (record.isGiven) "you owe " else "I owe you ") +
                            "₹${record.amount.toInt()}. Due ${record.dueDate.ifBlank { "soon" }}."
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("smsto:${record.mobile}")
                            putExtra("sms_body", message)
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "No messaging app found", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onEdit = { navManager.navigate(ScreenRoutes.EditLender.passLenderId(id)) },
                    onBack = { navManager.navigateBack() }
                )
            } else {
                LaunchedEffect(Unit) { navManager.navigateBack() }
            }
        }
    }
}
