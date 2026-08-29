package com.abhishek.spendly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

/**
 * The app's signature "hero" gradient - primary -> tertiary of the *active* color
 * scheme, so every [AppFlavor] (and dark mode) gets its own coherent 2-stop brand
 * gradient automatically, instead of one hardcoded pair of colors that would clash
 * with a flavor's own hue. Use for splash backgrounds, dashboard hero/balance cards,
 * primary CTA buttons, and other high-emphasis brand surfaces.
 */
@Composable
fun heroGradient(): Brush = Brush.linearGradient(
    colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
)

/** Vertical variant of [heroGradient], for tall hero surfaces (splash, onboarding). */
@Composable
fun heroGradientVertical(): Brush = Brush.verticalGradient(
    colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)
)

/** A softer version of [heroGradient] for large backgrounds where full saturation would be too loud. */
@Composable
fun heroGradientSoft(): Brush = Brush.linearGradient(
    colors = listOf(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.tertiaryContainer
    )
)

/** Subtle neutral surface gradient for cards that want depth without brand color. */
@Composable
fun subtleSurfaceGradient(): Brush = Brush.verticalGradient(
    colors = listOf(MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.surfaceVariant)
)
