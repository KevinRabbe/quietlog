package com.kevinrabbe.quietlog.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kevinrabbe.quietlog.domain.model.AppTheme

// Calm, muted dark palette — no flashy colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA5C8F2),           // Softer, desaturated blue
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

    background = Color(0xFF121212),        // Deeper near-black for a calmer feel
    onBackground = Color(0xFFE2E2E6),

    surface = Color(0xFF121212),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF2D3035),    // Slightly darker surface variant
    onSurfaceVariant = Color(0xFFC3C7CF),

    outline = Color(0xFF8D9199),
    outlineVariant = Color(0xFF43474E),

    inverseSurface = Color(0xFFE2E2E6),
    inverseOnSurface = Color(0xFF2F3033),
    inversePrimary = Color(0xFF0061A4),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0061A4),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),

    secondary = Color(0xFF535F70),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD4E4F7),
    onSecondaryContainer = Color(0xFF101C2B),

    tertiary = Color(0xFF406836),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC4E3BA),
    onTertiaryContainer = Color(0xFF002204),

    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = Color(0xFFFDFBFF),
    onBackground = Color(0xFF1A1C1E),

    surface = Color(0xFFFDFBFF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDFE2EB),
    onSurfaceVariant = Color(0xFF43474E),

    outline = Color(0xFF73777F),
)

@Composable
fun QuietLogTheme(
    theme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (theme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuietLogTypography,
        content = content
    )
}
