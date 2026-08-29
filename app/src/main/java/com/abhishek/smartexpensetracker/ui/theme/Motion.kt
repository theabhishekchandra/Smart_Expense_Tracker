package com.abhishek.smartexpensetracker.ui.theme

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

/**
 * Shared motion constants and Navigation-Compose transition specs, so every screen
 * transition and micro-interaction in the app shares the same feel instead of each
 * screen (or none) picking its own durations. See ui/theme/README notes in Gradients.kt
 * for the sibling "vibrant gradient fintech" color system.
 */
object Motion {
    const val SCREEN_TRANSITION_MS = 320
    const val QUICK_MS = 150
    const val STANDARD_MS = 300

    val standardEasing = FastOutSlowInEasing

    /** Snappy spring for press/pop micro-interactions (buttons, chips, FAB). */
    val bouncySpring = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMedium
    )

    /** Gentle spring for larger entrance moves (logo, hero cards). */
    val gentleSpring = spring<Float>(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    )

    // Default per-destination transitions applied once at the root NavHost so all
    // (26+) destinations animate consistently with zero per-screen wiring.
    val screenEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(SCREEN_TRANSITION_MS, easing = standardEasing)
        ) + fadeIn(tween(SCREEN_TRANSITION_MS))
    }
    val screenExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(SCREEN_TRANSITION_MS, easing = standardEasing)
        ) + fadeOut(tween(SCREEN_TRANSITION_MS))
    }
    val screenPopEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(SCREEN_TRANSITION_MS, easing = standardEasing)
        ) + fadeIn(tween(SCREEN_TRANSITION_MS))
    }
    val screenPopExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(SCREEN_TRANSITION_MS, easing = standardEasing)
        ) + fadeOut(tween(SCREEN_TRANSITION_MS))
    }

    /** Cross-fade only, for destinations where a directional slide reads oddly (e.g. bottom-tab switches). */
    val fadeEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        fadeIn(tween(STANDARD_MS))
    }
    val fadeExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        fadeOut(tween(STANDARD_MS))
    }
}
