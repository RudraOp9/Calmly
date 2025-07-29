package com.calmly.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val LightColorScheme = lightColorScheme(
    primary = CalmBlue,
    onPrimary = Color.White,
    primaryContainer = CalmBlue.copy(alpha = 0.1f),
    onPrimaryContainer = CalmBlue,

    secondary = SoftTeal,
    onSecondary = Color.White,
    secondaryContainer = SoftTeal.copy(alpha = 0.1f),
    onSecondaryContainer = SoftTeal,

    tertiary = SoftLavender,
    onTertiary = DarkGray,
    tertiaryContainer = SoftLavender.copy(alpha = 0.3f),
    onTertiaryContainer = DarkGray,

    background = WarmBeige,
    onBackground = DarkGray,

    surface = Color.White,
    onSurface = DarkGray,
    surfaceVariant = LightGray,
    onSurfaceVariant = CharcoalGray.copy(alpha = 0.7f),

    outline = MediumGray,
    outlineVariant = LightGray,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRed.copy(alpha = 0.1f),
    onErrorContainer = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = SoftTeal,
    onPrimary = DarkBackground,
    primaryContainer = SoftTeal.copy(alpha = 0.2f),
    onPrimaryContainer = SoftTeal,

    secondary = CalmBlue,
    onSecondary = DarkBackground,
    secondaryContainer = CalmBlue.copy(alpha = 0.2f),
    onSecondaryContainer = CalmBlue,

    tertiary = SoftLavender,
    onTertiary = DarkBackground,
    tertiaryContainer = SoftLavender.copy(alpha = 0.2f),
    onTertiaryContainer = SoftLavender,

    background = DarkBackground,
    onBackground = DarkOnSurface,

    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurface.copy(alpha = 0.8f),
    onSurfaceVariant = DarkOnSurface.copy(alpha = 0.7f),

    outline = MediumGray.copy(alpha = 0.3f),
    outlineVariant = MediumGray.copy(alpha = 0.1f),

    error = ErrorRed.copy(red = 0.9f),
    onError = DarkBackground,
    errorContainer = ErrorRed.copy(alpha = 0.2f),
    onErrorContainer = ErrorRed.copy(red = 0.9f)
)


@Composable
fun CalmlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}