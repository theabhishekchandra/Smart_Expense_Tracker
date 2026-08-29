package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing

@Composable
fun SwitchAuthText(
    prompt: String,
    actionText: String,
    onActionClick: () -> Unit,
    arrangement: Arrangement.Horizontal = Arrangement.Center
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppSpacing.md),
        horizontalArrangement = arrangement
    ) {
        Text(text = prompt, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = actionText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { onActionClick() }
        )
    }
}
