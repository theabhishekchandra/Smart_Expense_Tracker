package com.abhishek.smartexpensetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

private val LightColorScheme = lightColorScheme(
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

private val DarkColorScheme = darkColorScheme(
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
    content: @Composable () -> Unit
) {
//    val dataStore: DataStoreManager? = null
//    val darkTheme = if (dataStore != null) {
//        dataStore.isDarkTheme().collectAsState(initial = null).value
//            ?: isSystemInDarkTheme() // fallback here
//    } else {
//        isSystemInDarkTheme()
//    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


