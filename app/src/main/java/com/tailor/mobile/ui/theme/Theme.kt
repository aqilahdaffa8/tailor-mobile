package com.tailor.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PureWhite,
    onPrimary = Zinc900,
    primaryContainer = Zinc800,
    onPrimaryContainer = PureWhite,
    secondary = Zinc400,
    onSecondary = Zinc950,
    background = Zinc950,
    onBackground = Zinc100,
    surface = Zinc900,
    onSurface = Zinc100,
    surfaceVariant = Zinc800,
    onSurfaceVariant = Zinc400,
    outline = Zinc700,
    outlineVariant = Zinc800
)

private val LightColorScheme = lightColorScheme(
    primary = Zinc900,          // Charcoal Primary (CTA & Primary Action)
    onPrimary = PureWhite,
    primaryContainer = Zinc800, // Charcoal Hover / Accent
    onPrimaryContainer = PureWhite,
    secondary = Zinc600,        // Text Secondary
    onSecondary = PureWhite,
    background = Zinc50,        // Canvas Background (FAFAFA)
    onBackground = Zinc900,
    surface = PureWhite,        // Card / Container Surface (FFFFFF)
    onSurface = Zinc900,
    surfaceVariant = Zinc100,   // Border Subtle / Surface Subtle (F4F4F5)
    onSurfaceVariant = Zinc600,
    outline = Zinc200,          // Border / Divider (E4E4E7)
    outlineVariant = Zinc100
)

@Composable
fun TailorMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We intentionally enforce True Neutral Charcoal & Zinc without Android 12 dynamic wallpaper tinting
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = TailorShapes,
        content = content
    )
}