package com.kaizencheck.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Teal200,
    onPrimary = Teal900,
    primaryContainer = Teal800,
    onPrimaryContainer = Color.White,
    secondary = Teal200,
    onSecondary = Teal900,
    surface = Teal900,
    onSurface = Color.White,
    surfaceVariant = Teal800,
    onSurfaceVariant = Teal200
)

private val LightColorScheme = lightColorScheme(
    primary = KaizenPrimary,
    onPrimary = Color.White,
    primaryContainer = Teal50,
    onPrimaryContainer = Teal900,
    secondary = KaizenSecondary,
    onSecondary = Teal900,
    surface = Color.White,
    onSurface = Teal900,
    surfaceVariant = KaizenSurface,
    onSurfaceVariant = Teal800
)

@Composable
fun KaizenCheckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
