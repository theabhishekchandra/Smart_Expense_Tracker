package com.abhishek.spendly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abhishek.spendly.ui.theme.AppSpacing

/**
 * A translucent "glass" stat chip meant to sit on top of a [GradientCard] (or any
 * other saturated brand-color surface) - white text/border at low alpha instead of a
 * real blur (kept dependency-free and consistent across API 24+).
 */
@Composable
fun GlassStatTile(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(AppSpacing.md))
            .background(Color.White.copy(alpha = 0.16f))
            .border(
                BorderStroke(1.dp, Color.White.copy(alpha = 0.28f)),
                RoundedCornerShape(AppSpacing.md)
            )
            .padding(horizontal = AppSpacing.md, vertical = AppSpacing.sm)
    ) {
        icon?.invoke()
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.85f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
