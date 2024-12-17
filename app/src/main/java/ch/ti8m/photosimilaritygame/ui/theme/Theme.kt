package ch.wenksi.photosimilaritygame.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val lightColorScheme = lightColorScheme(
    primary = color.light.primary,
    onPrimary = color.light.onPrimary,
    primaryContainer = color.light.primaryContainer,
    onPrimaryContainer = color.light.onPrimaryContainer,
    secondary = color.light.secondary,
    onSecondary = color.light.onSecondary,
    secondaryContainer = color.light.secondaryContainer,
    onSecondaryContainer = color.light.onSecondaryContainer,
    tertiary = color.light.tertiary,
    onTertiary = color.light.onTertiary,
    tertiaryContainer = color.light.tertiaryContainer,
    onTertiaryContainer = color.light.onTertiaryContainer,
    error = color.light.error,
    onError = color.light.onError,
    errorContainer = color.light.errorContainer,
    onErrorContainer = color.light.onErrorContainer,
    background = color.light.background,
    onBackground = color.light.onBackground,
    surface = color.light.surface,
    onSurface = color.light.onSurface,
    surfaceVariant = color.light.surfaceVariant,
    onSurfaceVariant = color.light.onSurfaceVariant,
    outline = color.light.outline,
)

private val darkColorScheme = darkColorScheme(
    primary = color.dark.primary,
    onPrimary = color.dark.onPrimary,
    primaryContainer = color.dark.primaryContainer,
    onPrimaryContainer = color.dark.onPrimaryContainer,
    secondary = color.dark.secondary,
    onSecondary = color.dark.onSecondary,
    secondaryContainer = color.dark.secondaryContainer,
    onSecondaryContainer = color.dark.onSecondaryContainer,
    tertiary = color.dark.tertiary,
    onTertiary = color.dark.onTertiary,
    tertiaryContainer = color.dark.tertiaryContainer,
    onTertiaryContainer = color.dark.onTertiaryContainer,
    error = color.dark.error,
    onError = color.dark.onError,
    errorContainer = color.dark.errorContainer,
    onErrorContainer = color.dark.onErrorContainer,
    background = color.dark.background,
    onBackground = color.dark.onBackground,
    surface = color.dark.surface,
    onSurface = color.dark.onSurface,
    surfaceVariant = color.dark.surfaceVariant,
    onSurfaceVariant = color.dark.onSurfaceVariant,
    outline = color.dark.outline,
)

@Composable
fun PhotoSimilarityGameTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    disableDynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !disableDynamicColor -> {
            if (isDarkTheme) dynamicDarkColorScheme(LocalContext.current)
            else dynamicLightColorScheme(LocalContext.current)
        }
        isDarkTheme -> darkColorScheme
        else -> lightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        content()
    }
}

private data class ThemeColor(
    val title: String,
    val background: Color,
    val onBackground: Color,

    )

@Preview
@Composable
private fun ColorPaletteLight() {
    val l = color.light
    Column {
        ColorRow(listOf(
            ThemeColor("Primary", l.primary, l.onPrimary),
            ThemeColor("On Primary", l.onPrimary, l.primary),
            ThemeColor("Primary Container", l.primaryContainer, l.onPrimaryContainer),
            ThemeColor("On Primary Container", l.onPrimaryContainer, l.primaryContainer),
        ))
        ColorRow(listOf(
            ThemeColor("Secondary", l.secondary, l.onSecondary),
            ThemeColor("On Secondary", l.onSecondary, l.secondary),
            ThemeColor("Secondary Container", l.secondaryContainer, l.onSecondaryContainer),
            ThemeColor("On Secondary Container", l.onSecondaryContainer, l.secondaryContainer),
        ))
        ColorRow(listOf(
            ThemeColor("Tertiary", l.tertiary, l.onTertiary),
            ThemeColor("On Tertiary", l.onTertiary, l.tertiary),
            ThemeColor("Tertiary Container", l.tertiaryContainer, l.onTertiaryContainer),
            ThemeColor("On Tertiary Container", l.onTertiaryContainer, l.tertiaryContainer),
        ))
        ColorRow(listOf(
            ThemeColor("Error", l.error, l.onError),
            ThemeColor("On Error", l.onError, l.error),
            ThemeColor("Error Container", l.errorContainer, l.onErrorContainer),
            ThemeColor("On Error Container", l.onErrorContainer, l.errorContainer),
        ))
        ColorRow(listOf(
            ThemeColor("Background", l.background, l.onBackground),
            ThemeColor("On Background", l.onBackground, l.background),
            ThemeColor("Surface", l.surface, l.onSurface),
            ThemeColor("On Surface", l.onSurface, l.surface),
        ))
        ColorRow(listOf(
            ThemeColor("Surface Variant", l.surfaceVariant, Color.Black),
            ThemeColor("On Surface Variant", l.onSurfaceVariant, Color.White),
            ThemeColor("Outline", l.outline, Color.White),
        ))
    }
}

@Preview
@Composable
private fun ColorPaletteDark() {
    val l = color.dark
    Column {
        ColorRow(listOf(
            ThemeColor("Primary", l.primary, l.onPrimary),
            ThemeColor("On Primary", l.onPrimary, l.primary),
            ThemeColor("Primary Container", l.primaryContainer, l.onPrimaryContainer),
            ThemeColor("On Primary Container", l.onPrimaryContainer, l.primaryContainer),
        ))
        ColorRow(listOf(
            ThemeColor("Secondary", l.secondary, l.onSecondary),
            ThemeColor("On Secondary", l.onSecondary, l.secondary),
            ThemeColor("Secondary Container", l.secondaryContainer, l.onSecondaryContainer),
            ThemeColor("On Secondary Container", l.onSecondaryContainer, l.secondaryContainer),
        ))
        ColorRow(listOf(
            ThemeColor("Tertiary", l.tertiary, l.onTertiary),
            ThemeColor("On Tertiary", l.onTertiary, l.tertiary),
            ThemeColor("Tertiary Container", l.tertiaryContainer, l.onTertiaryContainer),
            ThemeColor("On Tertiary Container", l.onTertiaryContainer, l.tertiaryContainer),
        ))
        ColorRow(listOf(
            ThemeColor("Error", l.error, l.onError),
            ThemeColor("On Error", l.onError, l.error),
            ThemeColor("Error Container", l.errorContainer, l.onErrorContainer),
            ThemeColor("On Error Container", l.onErrorContainer, l.errorContainer),
        ))
        ColorRow(listOf(
            ThemeColor("Background", l.background, l.onBackground),
            ThemeColor("On Background", l.onBackground, l.background),
            ThemeColor("Surface", l.surface, l.onSurface),
            ThemeColor("On Surface", l.onSurface, l.surface),
        ))
        ColorRow(listOf(
            ThemeColor("Surface Variant", l.surfaceVariant, Color.White),
            ThemeColor("On Surface Variant", l.onSurfaceVariant, Color.Black),
            ThemeColor("Outline", l.outline, Color.Black),
        ))
    }
}

@Composable
private fun ColorRow(themeColors: List<ThemeColor>) {
    LazyRow {
        items(themeColors) { themeColor ->
            ColorBox(themeColor)
        }
    }
}

@Composable
private fun ColorBox(themeColor: ThemeColor) {
    Box(modifier = Modifier
        .width(100.dp)
        .height(50.dp)
        .clip(RectangleShape)
        .background(themeColor.background)) {
        Text(text = themeColor.title, color = themeColor.onBackground, fontSize = 8.sp)
    }
}