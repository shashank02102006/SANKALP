package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SankalpDarkColorScheme = darkColorScheme(
    primary = MutedMilitaryGreen,
    onPrimary = OffWhite,
    primaryContainer = DarkOlive,
    onPrimaryContainer = OffWhite,
    secondary = GoldAccent,
    onSecondary = CharcoalBlack,
    secondaryContainer = DarkOlive,
    onSecondaryContainer = GoldAccent,
    tertiary = MilitaryGreenBright,
    onTertiary = CharcoalBlack,
    background = CharcoalBlack,
    onBackground = OffWhite,
    surface = DarkOlive,
    onSurface = OffWhite,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = MutedGrey,
    outline = BorderOlive,
    outlineVariant = BorderOlive,
    error = ErrorRed,
    onError = OffWhite
)

@Composable
fun SankalpTheme(
    darkTheme: Boolean = true, // Military dark theme enforced
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SankalpDarkColorScheme,
        typography = Typography,
        content = content
    )
}
