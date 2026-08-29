package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme


@Composable
fun BottomBarButton(
    onFirstClick: () -> Unit = {},
    firstButtonText: String = "Cancel",
    onSecondClick: () -> Unit = {},
    secondButtonText: String = "Make Payment",
    secondButtonEnabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm)
    ) {
        OutlinedButton(
            onClick = onFirstClick ,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = firstButtonText, style = MaterialTheme.typography.labelLarge)
        }

        Button(
            onClick = onSecondClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = secondButtonEnabled,
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = secondButtonText, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    SmartExpenseTrackerTheme(
        true,

    ){
        BottomBarButton(
            onFirstClick = {},
            firstButtonText = "Cancel",
            onSecondClick = {},
            secondButtonText = "Make Payment",
            secondButtonEnabled = true
        )
    }
}