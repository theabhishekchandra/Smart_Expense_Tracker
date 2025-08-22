package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.core.navigation.ScreenRoutes
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme

@Composable
fun BottomNavigationBar(
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, ScreenRoutes.Home.route),
        BottomNavItem("Documents", Icons.Default.Description, ScreenRoutes.ExpenseList.route),
        BottomNavItem("Voice", Icons.Default.Mic, ScreenRoutes.Voice.route),
        BottomNavItem("Report", Icons.Default.Assessment, ScreenRoutes.Reports.route),
        BottomNavItem("Profile", Icons.Default.Person, ScreenRoutes.Profile.route)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 4.dp
    ) {
        items.forEach { item ->
            val isSelected = selectedRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(item.route) },
                icon = {
                    if (item.label == "Voice") {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(37.dp)
                        )
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                label = {
                    if (item.label != "Voice") {
                        Text(
                            text = item.label,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                alwaysShowLabel = item.label != "Voice"
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarLightPreview() {
    SmartExpenseTrackerTheme(darkTheme = false) {
        BottomNavigationBar(
            selectedRoute = ScreenRoutes.Home.route,
            onItemSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarDarkPreview() {
    SmartExpenseTrackerTheme(darkTheme = true) {
        BottomNavigationBar(
            selectedRoute = ScreenRoutes.Home.route,
            onItemSelected = {}
        )
    }
}
