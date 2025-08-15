package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelector(categories: List<String>, selected: String, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Category", style = MaterialTheme.typography.bodyLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.forEach { cat ->
                FilterChip(selected = selected == cat, onClick = { onSelect(cat) }, label = { Text(cat) })
            }
        }
    }
}