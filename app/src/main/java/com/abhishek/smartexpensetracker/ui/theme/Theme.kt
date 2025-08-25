package com.abhishek.smartexpensetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ------------------ COLOR PALETTES ------------------ //

// PERSONAL BASIC
private val PersonalBasicLight = lightColorScheme(
    primary = Color(0xFF1976D2), // Blue
    secondary = Color(0xFF4CAF50), // Green
    background = WhiteColor,
    surface = LightFillColor,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = GreyColor,
    onSurface = GreyColor,
    outline = OutlineColorLight
)
private val PersonalBasicDark = darkColorScheme(
    primary = Color(0xFF90CAF9),
    secondary = Color(0xFF81C784),
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = DarkText,
    onSurface = DarkText,
    outline = OutlineColorDark
)

// PERSONAL PREMIUM
private val PersonalPremiumLight = lightColorScheme(
    primary = Color(0xFF6A1B9A), // Purple
    secondary = Color(0xFFFFA000), // Amber
    background = WhiteColor,
    surface = LightFillColor,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = GreyColor,
    onSurface = GreyColor,
    outline = OutlineColorLight
)
private val PersonalPremiumDark = darkColorScheme(
    primary = Color(0xFFBA68C8),
    secondary = Color(0xFFFFD54F),
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = DarkText,
    onSurface = DarkText,
    outline = OutlineColorDark
)

// BUSINESS BASIC
private val BusinessBasicLight = lightColorScheme(
    primary = Color(0xFF0D47A1), // Navy
    secondary = Color(0xFFFF7043), // Deep Orange
    background = WhiteColor,
    surface = LightFillColor,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = GreyColor,
    onSurface = GreyColor,
    outline = OutlineColorLight
)
private val BusinessBasicDark = darkColorScheme(
    primary = Color(0xFF42A5F5),
    secondary = Color(0xFFFFAB91),
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = DarkText,
    onSurface = DarkText,
    outline = OutlineColorDark
)

// BUSINESS PREMIUM
private val BusinessPremiumLight = lightColorScheme(
    primary = Color(0xFF1B1B2F), // Dark Navy
    secondary = Color(0xFFFFC107), // Gold
    background = WhiteColor,
    surface = LightFillColor,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = GreyColor,
    onSurface = GreyColor,
    outline = OutlineColorLight
)
private val BusinessPremiumDark = darkColorScheme(
    primary = Color(0xFF3F51B5),
    secondary = Color(0xFFFFD740),
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = DarkText,
    onSurface = DarkText,
    outline = OutlineColorDark
)

// ------------------ ROLE COLORS ------------------ //
val RoleAdmin = Color(0xFFFFC107)     // Gold
val RoleApprover = Color(0xFF9C27B0)  // Purple
val RoleViewer = Color(0xFF90A4AE)    // Grey
val RoleEntry = Color(0xFF29B6F6)     // Light Blue

// ------------------ ENUM ------------------ //
enum class AppFlavor { PERSONAL_BASIC, PERSONAL_PREMIUM, BUSINESS_BASIC, BUSINESS_PREMIUM }

// ------------------ THEME FUNCTION ------------------ //
@Composable
fun SmartExpenseTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    appFlavor: AppFlavor = AppFlavor.PERSONAL_BASIC,
    content: @Composable () -> Unit
) {
    val colors = when (appFlavor) {
        AppFlavor.PERSONAL_BASIC -> if (darkTheme) PersonalBasicDark else PersonalBasicLight
        AppFlavor.PERSONAL_PREMIUM -> if (darkTheme) PersonalPremiumDark else PersonalPremiumLight
        AppFlavor.BUSINESS_BASIC -> if (darkTheme) BusinessBasicDark else BusinessBasicLight
        AppFlavor.BUSINESS_PREMIUM -> if (darkTheme) BusinessPremiumDark else BusinessPremiumLight
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
