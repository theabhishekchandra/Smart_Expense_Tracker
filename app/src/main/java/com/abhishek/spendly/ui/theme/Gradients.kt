package com.abhishek.spendly.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * The two stops of the signature "hero" gradient for the *active* color scheme.
 *
 * `primary`/`tertiary` are vivid/saturated in every flavor's *light* scheme, but in
 * the *dark* schemes those same roles are deliberately pale, low-saturation tones
 * meant for text/icons drawn on a dark surface - using them as a big background fill
 * would wash the hero gradient out to a pastel color, killing contrast for the white
 * text/icons every caller draws on top. `primaryContainer`/`tertiaryContainer` are the
 * inverse: pale in light schemes, deep and saturated in dark schemes - so they're the
 * right pick for a dark-mode hero fill instead.
 */
@Composable
private fun heroGradientStops(): List<Color> {
    val isDark = isAppDarkTheme()
    val start = if (isDark) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary
    val end = if (isDark) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.tertiary
    return listOf(start, end)
}

/**
 * The app's signature "hero" gradient - primary -> tertiary of the *active* color
 * scheme (swapped for their saturated dark-mode equivalents, see [heroGradientStops]),
 * so every [AppFlavor] and theme gets its own coherent 2-stop brand gradient
 * automatically, instead of one hardcoded pair of colors that would clash with a
 * flavor's own hue. Use for splash backgrounds, dashboard hero/balance cards, primary
 * CTA buttons, and other high-emphasis brand surfaces.
 */
@Composable
fun heroGradient(): Brush = Brush.linearGradient(colors = heroGradientStops())

/** Vertical variant of [heroGradient], for tall hero surfaces (splash, onboarding). */
@Composable
fun heroGradientVertical(): Brush = Brush.verticalGradient(colors = heroGradientStops())

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
