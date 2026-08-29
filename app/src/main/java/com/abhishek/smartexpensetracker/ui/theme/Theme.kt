package com.abhishek.smartexpensetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ------------------ COLOR PALETTES ------------------ //
// "Vibrant gradient fintech" system: each flavor's primary->tertiary pair is the
// 2-stop brand gradient used for hero surfaces (see Gradients.kt) - vivid, saturated
// hues instead of muted corporate tones, while keeping a shared neutral/dark-mode base
// across flavors for consistency.

// PERSONAL BASIC - vivid blue -> violet, cyan accent
private val PersonalBasicLight = lightColorScheme(
    primary = Color(0xFF155EEF),
    onPrimary = WhiteColor,
    primaryContainer = Color(0xFFDCE6FF),
    onPrimaryContainer = Color(0xFF001A4D),
    secondary = Color(0xFF06B6D4),
    onSecondary = WhiteColor,
    secondaryContainer = Color(0xFFCFF7FC),
    onSecondaryContainer = Color(0xFF003239),
    tertiary = Color(0xFF7C3AED),
    onTertiary = WhiteColor,
    tertiaryContainer = Color(0xFFEDE1FF),
    onTertiaryContainer = Color(0xFF29005C),
    error = DangerColor,
    onError = WhiteColor,
    errorContainer = Color(0xFFFFDAD8),
    onErrorContainer = Color(0xFF410004),
    background = WhiteColor,
    onBackground = GreyColor,
    surface = WhiteColor,
    onSurface = GreyColor,
    surfaceVariant = LightFillColor,
    onSurfaceVariant = Color(0xFF444A5A),
    outline = OutlineColorLight,
    outlineVariant = LightSurfaceVariant,
)
private val PersonalBasicDark = darkColorScheme(
    primary = Color(0xFF9DBBFF),
    onPrimary = Color(0xFF002B73),
    primaryContainer = Color(0xFF0A3EA6),
    onPrimaryContainer = Color(0xFFDCE6FF),
    secondary = Color(0xFF4DE8FF),
    onSecondary = Color(0xFF003339),
    secondaryContainer = Color(0xFF004E58),
    onSecondaryContainer = Color(0xFFCFF7FC),
    tertiary = Color(0xFFC9A6FF),
    onTertiary = Color(0xFF3D0080),
    tertiaryContainer = Color(0xFF5417AE),
    onTertiaryContainer = Color(0xFFEDE1FF),
    error = DangerColorDark,
    onError = Color(0xFF690003),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD8),
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC4C8DA),
    outline = OutlineColorDark,
    outlineVariant = Color(0xFF333953),
)

// PERSONAL PREMIUM - violet -> magenta, amber accent
private val PersonalPremiumLight = lightColorScheme(
    primary = Color(0xFF7C3AED),
    onPrimary = WhiteColor,
    primaryContainer = Color(0xFFEDE1FF),
    onPrimaryContainer = Color(0xFF29005C),
    secondary = Color(0xFFF59E0B),
    onSecondary = Color(0xFF3A2600),
    secondaryContainer = Color(0xFFFFE8B3),
    onSecondaryContainer = Color(0xFF3A2600),
    tertiary = Color(0xFFDB2777),
    onTertiary = WhiteColor,
    tertiaryContainer = Color(0xFFFFD9E8),
    onTertiaryContainer = Color(0xFF3E001F),
    error = DangerColor,
    onError = WhiteColor,
    errorContainer = Color(0xFFFFDAD8),
    onErrorContainer = Color(0xFF410004),
    background = WhiteColor,
    onBackground = GreyColor,
    surface = WhiteColor,
    onSurface = GreyColor,
    surfaceVariant = Color(0xFFF6EFFC),
    onSurfaceVariant = Color(0xFF4A4458),
    outline = OutlineColorLight,
    outlineVariant = LightSurfaceVariant,
)
private val PersonalPremiumDark = darkColorScheme(
    primary = Color(0xFFCBB2FF),
    onPrimary = Color(0xFF3D0080),
    primaryContainer = Color(0xFF5417AE),
    onPrimaryContainer = Color(0xFFEDE1FF),
    secondary = Color(0xFFFFC24B),
    onSecondary = Color(0xFF3F2E00),
    secondaryContainer = Color(0xFF5C4300),
    onSecondaryContainer = Color(0xFFFFE8B3),
    tertiary = Color(0xFFFF9EC7),
    onTertiary = Color(0xFF5C0033),
    tertiaryContainer = Color(0xFF7F0049),
    onTertiaryContainer = Color(0xFFFFD9E8),
    error = DangerColorDark,
    onError = Color(0xFF690003),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD8),
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCBC5DC),
    outline = OutlineColorDark,
    outlineVariant = Color(0xFF3A3650),
)

// BUSINESS BASIC - deep blue -> emerald, coral accent
private val BusinessBasicLight = lightColorScheme(
    primary = Color(0xFF0B4FCC),
    onPrimary = WhiteColor,
    primaryContainer = Color(0xFFD9E5FF),
    onPrimaryContainer = Color(0xFF001A45),
    secondary = Color(0xFFFF6B4A),
    onSecondary = WhiteColor,
    secondaryContainer = Color(0xFFFFDBCE),
    onSecondaryContainer = Color(0xFF3A1200),
    tertiary = Color(0xFF0EA472),
    onTertiary = WhiteColor,
    tertiaryContainer = Color(0xFFC6F3DE),
    onTertiaryContainer = Color(0xFF00210F),
    error = DangerColor,
    onError = WhiteColor,
    errorContainer = Color(0xFFFFDAD8),
    onErrorContainer = Color(0xFF410004),
    background = WhiteColor,
    onBackground = GreyColor,
    surface = WhiteColor,
    onSurface = GreyColor,
    surfaceVariant = LightFillColor,
    onSurfaceVariant = Color(0xFF434A56),
    outline = OutlineColorLight,
    outlineVariant = LightSurfaceVariant,
)
private val BusinessBasicDark = darkColorScheme(
    primary = Color(0xFFA9C4FF),
    onPrimary = Color(0xFF00296B),
    primaryContainer = Color(0xFF003C99),
    onPrimaryContainer = Color(0xFFD9E5FF),
    secondary = Color(0xFFFFB39C),
    onSecondary = Color(0xFF5C2000),
    secondaryContainer = Color(0xFF832D0E),
    onSecondaryContainer = Color(0xFFFFDBCE),
    tertiary = Color(0xFF7EDBB3),
    onTertiary = Color(0xFF00391F),
    tertiaryContainer = Color(0xFF00512F),
    onTertiaryContainer = Color(0xFFC6F3DE),
    error = DangerColorDark,
    onError = Color(0xFF690003),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD8),
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC3C7D4),
    outline = OutlineColorDark,
    outlineVariant = Color(0xFF383E52),
)

// BUSINESS PREMIUM - indigo -> gold, rose accent
private val BusinessPremiumLight = lightColorScheme(
    primary = Color(0xFF4338CA),
    onPrimary = WhiteColor,
    primaryContainer = Color(0xFFE1DEFF),
    onPrimaryContainer = Color(0xFF0F0068),
    secondary = Color(0xFFE11D5B),
    onSecondary = WhiteColor,
    secondaryContainer = Color(0xFFFFD9E1),
    onSecondaryContainer = Color(0xFF3E0018),
    tertiary = Color(0xFFB8860B),
    onTertiary = WhiteColor,
    tertiaryContainer = Color(0xFFFFE7A8),
    onTertiaryContainer = Color(0xFF2A1D00),
    error = DangerColor,
    onError = WhiteColor,
    errorContainer = Color(0xFFFFDAD8),
    onErrorContainer = Color(0xFF410004),
    background = WhiteColor,
    onBackground = GreyColor,
    surface = WhiteColor,
    onSurface = GreyColor,
    surfaceVariant = Color(0xFFEEECFB),
    onSurfaceVariant = Color(0xFF45455A),
    outline = OutlineColorLight,
    outlineVariant = LightSurfaceVariant,
)
private val BusinessPremiumDark = darkColorScheme(
    primary = Color(0xFFC2BFFF),
    onPrimary = Color(0xFF1E1478),
    primaryContainer = Color(0xFF332893),
    onPrimaryContainer = Color(0xFFE1DEFF),
    secondary = Color(0xFFFFB0C6),
    onSecondary = Color(0xFF650025),
    secondaryContainer = Color(0xFF8E0037),
    onSecondaryContainer = Color(0xFFFFD9E1),
    tertiary = Color(0xFFF0C556),
    onTertiary = Color(0xFF3A2E00),
    tertiaryContainer = Color(0xFF544300),
    onTertiaryContainer = Color(0xFFFFE7A8),
    error = DangerColorDark,
    onError = Color(0xFF690003),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD8),
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFC7C4DC),
    outline = OutlineColorDark,
    outlineVariant = Color(0xFF3D3A54),
)

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
        shapes = AppShapes,
        content = content
    )
}
