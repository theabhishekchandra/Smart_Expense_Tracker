package com.abhishek.spendly.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.abhishek.spendly.ui.theme.Motion

/**
 * A money amount that animates ("counts up/down") whenever [amount] changes, instead
 * of snapping instantly. Used for the highest-visibility totals in the app (home
 * balance, top-bar totals, expense/budget amounts) as part of the motion system.
 */
@Composable
fun AnimatedAmountText(
    amount: Double,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineSmall,
    color: Color = Color.Unspecified,
    prefix: String = "₹",
    decimals: Int = 2,
) {
    val animated = remember { Animatable(0f) }
    LaunchedEffect(amount) {
        animated.animateTo(
            targetValue = amount.toFloat(),
            animationSpec = tween(durationMillis = 700, easing = Motion.standardEasing)
        )
    }
    Text(
        text = "$prefix${"%,.${decimals}f".format(animated.value)}",
        modifier = modifier,
        style = style,
        color = color
    )
}
