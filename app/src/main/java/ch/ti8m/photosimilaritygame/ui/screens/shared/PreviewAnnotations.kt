package ch.wenksi.photosimilaritygame.ui.screens.shared

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ch.wenksi.photosimilaritygame.ui.theme.PhotoSimilarityGameTheme

@Preview(
    group = "ScreenSizes",
    name = "2 Phone Portrait",
    showBackground = true,
    widthDp = 411,
    heightDp = 891,
)
@Preview(
    group = "ScreenSizes",
    name = "3 Phone Landscape",
    showBackground = true,
    widthDp = 891,
    heightDp = 411,
)
@Preview(
    group = "ScreenSizes",
    name = "4 Tablet",
    showBackground = true,
    widthDp = 1280,
    heightDp = 800,
)
@Preview(
    group = "ScreenSizes",
    name = "5 Desktop",
    showBackground = true,
    widthDp = 1920,
    heightDp = 1080,
)
annotation class ScreenSizePreviews

@Preview(
    name = "1 Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class DarkModePreview

@Preview(
    name = "2 Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
annotation class LightModePreview

@ScreenSizePreviews
@DarkModePreview
annotation class AllPreviews

@Composable
fun ThemedPreview(content: @Composable () -> Unit = {}) {
    PhotoSimilarityGameTheme(disableDynamicColor = true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}
