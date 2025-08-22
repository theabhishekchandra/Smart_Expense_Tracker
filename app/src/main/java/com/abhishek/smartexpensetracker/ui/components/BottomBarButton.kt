package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onFirstClick ,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary,
                contentColor = MaterialTheme.colorScheme.secondary
            ),
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = firstButtonText)
        }

        Button(
            onClick = onSecondClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = secondButtonEnabled,
            border = BorderStroke(
                width = 0.5.dp,
                color = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small
        ) {
            Text(text = secondButtonText)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    SmartExpenseTrackerTheme {
        BottomBarButton(
            onFirstClick = {},
            firstButtonText = "Cancel",
            onSecondClick = {},
            secondButtonText = "Make Payment",
            secondButtonEnabled = true
        )
    }
}