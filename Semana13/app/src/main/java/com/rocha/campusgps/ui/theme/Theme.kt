package com.rocha.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TecsupCyanLight,
    onPrimary = Color.Black,
    primaryContainer = TecsupCyanDark,
    onPrimaryContainer = TecsupCyanContainer,
    secondary = TecsupCyan,
    onSecondary = Color.Black,
    secondaryContainer = TecsupCyanDark,
    onSecondaryContainer = TecsupCyanLight,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF334155)
)

private val LightColorScheme = lightColorScheme(
    primary = TecsupCyan,
    onPrimary = Color.White,
    primaryContainer = TecsupCyanContainer,
    onPrimaryContainer = TecsupCyanDark,
    secondary = TecsupCyanLight,
    onSecondary = Color.White,
    secondaryContainer = TecsupCyanContainer,
    onSecondaryContainer = TecsupCyanDark,
    tertiary = TecsupCyanDark,
    onTertiary = Color.White,
    background = Color.White,
    onBackground = TecsupTextPrimary,
    surface = Color.White,
    onSurface = TecsupTextPrimary,
    surfaceVariant = TecsupCyanSurface,
    onSurfaceVariant = TecsupTextSecondary,
    outline = TecsupBorder,
    outlineVariant = Color(0xFFF1F5F9),
    errorContainer = CampusErrorBg,
    onErrorContainer = CampusErrorText
)

@Composable
fun CampusGPSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
