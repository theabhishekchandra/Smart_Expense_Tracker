package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarTotal(total: Double) {
    TopAppBar(title = { Text(text = "Total Spent Today: ₹${"%.2f".format(total)}") })
}
