package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing
import com.abhishek.smartexpensetracker.ui.theme.HeroCornerRadius
import com.abhishek.smartexpensetracker.ui.theme.heroGradient

/**
 * The signature "hero" surface of the vibrant-gradient-fintech design: a large
 * rounded card filled with [heroGradient] and a soft, brand-tinted shadow (instead of
 * a flat grey Material shadow). Use for dashboard balance cards, splash backgrounds,
 * and other high-emphasis brand moments - not for routine list items.
 */
@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    gradient: Brush = heroGradient(),
    shape: Shape = RoundedCornerShape(HeroCornerRadius),
    elevation: Dp = 18.dp,
    contentPadding: PaddingValues = PaddingValues(AppSpacing.lg),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f),
                spotColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.35f)
            )
            .clip(shape)
            .background(gradient)
            .padding(contentPadding),
        content = content
    )
}
