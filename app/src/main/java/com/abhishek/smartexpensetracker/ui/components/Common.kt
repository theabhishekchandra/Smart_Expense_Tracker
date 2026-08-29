package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing

@Composable
fun VerticalSpacer(height: Dp = AppSpacing.lg) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun DividerLine() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant,
        thickness = 1.dp,
        modifier = Modifier.fillMaxWidth()
    )
}