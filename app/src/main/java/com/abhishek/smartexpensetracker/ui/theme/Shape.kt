package com.abhishek.smartexpensetracker.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Rounded-corner scale used across cards/buttons/sheets so every screen shares the same
// "roundness" instead of each screen picking its own RoundedCornerShape(_.dp) ad hoc.
// Bumped up for the "vibrant gradient fintech" look - large, soft, floating surfaces.
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

/** Extra-large hero-card corner radius, for gradient hero surfaces that go beyond [Shapes.extraLarge]. */
val HeroCornerRadius = 32.dp
