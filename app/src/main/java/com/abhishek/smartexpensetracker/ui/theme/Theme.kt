package com.abhishek.smartexpensetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

private val DefaultLightColors = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = WhiteColor,
    surface = LightFillColor,
    secondaryContainer = LightFillColor,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = GreyColor,
    onSurface = GreyColor,
    outline = OutlineColorLight
)

private val DefaultDarkColors = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = DarkBackground,
    surface = DarkSurface,
    secondaryContainer = LightFillColor,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = DarkText,
    onSurface = DarkText,
    outline = OutlineColorDark
)
private val BusinessLightColors = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = WhiteColor,
    surface = LightFillColor,
    secondaryContainer = LightFillColor,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = GreyColor,
    onSurface = GreyColor,
    outline = OutlineColorLight
)

private val BusinessDarkColors = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = DarkBackground,
    surface = DarkSurface,
    secondaryContainer = LightFillColor,
    onPrimary = WhiteColor,
    onSecondary = WhiteColor,
    onBackground = DarkText,
    onSurface = DarkText,
    outline = OutlineColorDark
)

@Composable
fun SmartExpenseTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    businessMode: Boolean,
    content: @Composable () -> Unit
) {
    val colors = when {
        businessMode -> if (darkTheme) BusinessDarkColors else BusinessLightColors
        else -> if (darkTheme) DefaultDarkColors else DefaultLightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}


