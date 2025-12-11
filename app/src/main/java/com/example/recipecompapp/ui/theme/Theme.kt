package com.example.recipecompapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val RecipeDarkColorScheme = darkColorScheme(
    primary = PrimaryColorDark,
    onPrimary = TextPrimaryColorDark,
    error = AccentColorDark,
    tertiary = AccentBlueDark,
    tertiaryContainer = SliderTrackColorDark,
    background = BackgroundColorDark,
    surface = SurfaceColorDark,
    outline = DividerColorDark,
    onSurface = SurfaceColorDark,
    onSurfaceVariant = SurfaceVariantColorDark,
    onSecondary = TextSecondaryColorDark,
)

private val RecipeLightColorScheme = lightColorScheme(

    primary = PrimaryColor,
    onPrimary = TextPrimaryColor,
    error = AccentColor,
    tertiary = AccentBlue,
    tertiaryContainer = SliderTrackColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    outline = DividerColor,
    onSurface = SurfaceColor,
    onSurfaceVariant = SurfaceVariantColor,
    onSecondary = TextSecondaryColor,
)

@Composable
fun RecipeCompAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        RecipeDarkColorScheme
    } else {
        RecipeLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = recipesAppTypography,
        content = content
    )
}