package ch.wenksi.photosimilaritygame.ui.screens.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun PhotoWithTitle(
    modifier: Modifier = Modifier,
    imageUri: String = "",
    title: String = "",
    textStyleTitle: TextStyle = MaterialTheme.typography.headlineLarge,
    textStyleLabels: TextStyle = MaterialTheme.typography.bodyMedium,
    labels: List<String>? = null,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = textStyleTitle,
        )
        SubcomposeAsyncImage(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            model = imageUri,
            loading = { CircularProgressIndicator() },
            contentDescription = null
        )
        labels?.let {
            Text(
                text = it.joinToString(separator = ", "),
                style = textStyleLabels,
            )
        }
    }
}

@DarkModePreview
@LightModePreview
@Composable
private fun PhotoWithTitlePreview() {
    ThemedPreview {
        PhotoWithTitle(
            modifier = Modifier,
            imageUri = "imageUri",
            title = "title",
            labels = listOf("Forest", "Tree"),
        )
    }
}
