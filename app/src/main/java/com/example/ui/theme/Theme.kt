package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RamDarkColorScheme = darkColorScheme(
    primary = RamLimeAccent,
    onPrimary = Color(0xFF0F1B07),
    primaryContainer = RamLimeContainer,
    onPrimaryContainer = RamLimeGlow,
    secondary = RamElectricBlue,
    onSecondary = Color.Black,
    secondaryContainer = RamSurfaceVariant,
    onSecondaryContainer = RamTextPrimary,
    tertiary = RamLimeAccent,
    onTertiary = Color.Black,
    background = RamDarkBackground,
    onBackground = RamTextPrimary,
    surface = RamSurface,
    onSurface = RamTextPrimary,
    surfaceVariant = RamSurfaceVariant,
    onSurfaceVariant = RamTextSecondary,
    outline = RamBorder,
    error = RamLiveRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RamDarkColorScheme,
        typography = Typography,
        content = content
    )
}
