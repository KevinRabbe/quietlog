package com.kevinrabbe.quietlog.core.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Calm, muted dark palette — no flashy colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9ECAFF),           // Soft blue
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF00497D),
    onPrimaryContainer = Color(0xFFD1E4FF),

    secondary = Color(0xFFB8C8E8),         // Muted slate blue
    onSecondary = Color(0xFF22324A),
    secondaryContainer = Color(0xFF384960),
    onSecondaryContainer = Color(0xFFD4E4F7),

    tertiary = Color(0xFFA8C7A0),          // Soft sage green
    onTertiary = Color(0xFF113916),
    tertiaryContainer = Color(0xFF27502B),
    onTertiaryContainer = Color(0xFFC4E3BA),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF1A1C1E),        // Deep near-black
    onBackground = Color(0xFFE2E2E6),

    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C7CF),

    outline = Color(0xFF8D9199),
    outlineVariant = Color(0xFF43474E),

    inverseSurface = Color(0xFFE2E2E6),
    inverseOnSurface = Color(0xFF2F3033),
    inversePrimary = Color(0xFF0061A4),
)

@Composable
fun QuietLogTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = QuietLogTypography,
        content = content
    )
}
