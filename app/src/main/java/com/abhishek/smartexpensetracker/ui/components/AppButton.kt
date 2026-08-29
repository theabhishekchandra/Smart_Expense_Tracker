package com.abhishek.smartexpensetracker.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abhishek.smartexpensetracker.ui.theme.AppSpacing
import com.abhishek.smartexpensetracker.ui.theme.Motion
import com.abhishek.smartexpensetracker.ui.theme.SmartExpenseTrackerTheme
import com.abhishek.smartexpensetracker.ui.theme.heroGradient

/**
 * The app's primary CTA button: filled with the brand [heroGradient] and a soft
 * tinted shadow, with a bounce-scale press interaction, instead of a flat single
 * color - the signature "vibrant gradient fintech" action button used app-wide.
 */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val shape = MaterialTheme.shapes.small
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = Motion.bouncySpring,
        label = "appButtonScale"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = modifier
            .height(52.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 10.dp else 0.dp,
                shape = shape,
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                spotColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
            )
            .clip(shape)
            .background(if (enabled) heroGradient() else SolidColor(MaterialTheme.colorScheme.surfaceVariant)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = null,
        contentPadding = PaddingValues(horizontal = AppSpacing.md, vertical = AppSpacing.sm),
        shape = shape
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
private fun AppButtonPreview() {
    SmartExpenseTrackerTheme(
        true,
    ) {
        AppButton(
            text = "Button",
            onClick = {},
            modifier = Modifier.width(300.dp)
        )
    }
}
