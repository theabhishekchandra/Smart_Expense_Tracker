package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.ui.tooling.preview.Preview
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceTopBar(
    title: String,
    showBackButton: Boolean = false,
    showSearch: Boolean = true,
    showFilter: Boolean = true,
    showNotifications: Boolean = true,
    showMenu: Boolean = true,
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(
            text = title, style = MaterialTheme.typography.titleLarge,
            maxLines = 1, softWrap = true) },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (showSearch) {
                IconButton(onClick = onSearchClick) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
            if (showFilter) {
                IconButton(onClick = onFilterClick) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter")
                }
            }
            if (showNotifications) {
                IconButton(onClick = onNotificationClick) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }
            }
            if (showMenu) {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewFinanceTopBarWithDarkMode() {
    SmartExpenseTrackerTheme(
        true,
        false
    ){
        FinanceTopBar(
            title = "Reports",
            showBackButton = true,
            showSearch = true,
            showFilter = false, // hide filter
            showNotifications = true,
            showMenu = true
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFinanceTopBarWithLightMode() {
    SmartExpenseTrackerTheme(
        true,
        false
    ){
    FinanceTopBar(
            title = "Expenses",
            showBackButton = false,
            showSearch = true,
            showFilter = true,
            showNotifications = false, // hide notifications
            showMenu = true
        )
    }
}
