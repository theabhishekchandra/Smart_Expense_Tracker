package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    searchItems: List<String>,
    placeHolder: String = "Search...",
    modifier: Modifier = Modifier
) {
    val filteredItems = remember(query, searchItems) {
        searchItems.filter { it.contains(query, ignoreCase = true) }
    }

    Column(modifier = modifier) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {
                onSearch(query)
                onActiveChange(false)
            },
            active = active,
            placeholder = { Text(text = placeHolder) },
            onActiveChange = onActiveChange,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = SearchBarDefaults.inputFieldShape,
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = {
                        onQueryChange("") // Clear the search text
                        // Optional: onActiveChange(false) // If you also want to collapse the search
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear Search")
                    }
                }
//                if (active) {
//                    IconButton(onClick = {
//                        if (query.isNotEmpty()) onQueryChange("") else onActiveChange(false)
//                    }) {
//                        Icon(Icons.Default.Close, contentDescription = "Clear or Close")
//                    }
//                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            filteredItems.forEach { item ->
                ListItem(
                    headlineContent = { Text(item) },
                    modifier = Modifier.clickable {
                        onQueryChange(item)
                        onSearch(item)
                        onActiveChange(false)
                    }
                )
            }
        }
    }
}


@Preview
@Composable
private fun AppSearchBarPreview() {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val items = listOf("Apple", "Banana", "Cherry", "Date", "Elderberry")

    SmartExpenseTrackerTheme {
        Surface {
            AppSearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = { result ->
                    // Do something with the selected item or query
                    println("User searched: $result")
                },
                active = active,
                onActiveChange = { active = it },
                searchItems = items
            )

        }
    }

}