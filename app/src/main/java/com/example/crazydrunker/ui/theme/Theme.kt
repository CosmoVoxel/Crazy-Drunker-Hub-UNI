package com.example.crazydrunker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = Neutral0,
    primaryContainer = PrimaryLight.copy(alpha = 0.1f),
    onPrimaryContainer = PrimaryLight,
    secondary = SecondaryLight,
    onSecondary = Neutral0,
    secondaryContainer = SecondaryLight.copy(alpha = 0.1f),
    onSecondaryContainer = SecondaryLight,
    tertiary = PrimaryLight.copy(alpha = 0.8f),
    onTertiary = Neutral0,
    tertiaryContainer = PrimaryLight.copy(alpha = 0.1f),
    onTertiaryContainer = PrimaryLight,
    error = Error,
    onError = Neutral0,
    errorContainer = Error.copy(alpha = 0.1f),
    onErrorContainer = Error,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = Neutral10,
    onSurfaceVariant = Neutral80,
    outline = Neutral20,
    outlineVariant = Neutral20,
    scrim = Neutral80.copy(alpha = 0.5f)
)

private val DarkColors = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = Neutral100,
    primaryContainer = PrimaryDark.copy(alpha = 0.1f),
    onPrimaryContainer = PrimaryDark,
    secondary = SecondaryDark,
    onSecondary = Neutral100,
    secondaryContainer = SecondaryDark.copy(alpha = 0.1f),
    onSecondaryContainer = SecondaryDark,
    tertiary = PrimaryDark.copy(alpha = 0.8f),
    onTertiary = Neutral100,
    tertiaryContainer = PrimaryDark.copy(alpha = 0.1f),
    onTertiaryContainer = PrimaryDark,
    error = Error,
    onError = Neutral0,
    errorContainer = Error.copy(alpha = 0.1f),
    onErrorContainer = Error,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = Neutral80,
    onSurfaceVariant = Neutral20,
    outline = Neutral50,
    outlineVariant = Neutral80,
    scrim = Neutral0.copy(alpha = 0.5f)
)

@Composable
fun CrazyDrunkerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = CocktailTypography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}