package ch.wenksi.photosimilaritygame.ui.screens.shared

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Celebration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.ui.theme.Sizes

@Preview(showBackground = true)
@Composable
private fun ResultPhotoAndScorePortraitPreview() {
    ResultPhotosAndScorePortrait(
        result = Result(
            randomPhotoUrl = "randomPhotoUrl",
            cameraPhotoUri = Uri.EMPTY,
            similarityScore = 50,
            cameraPhotoLabels = listOf("Elephant"),
            randomPhotoLabels = listOf("Tree"),
            userName = "",
        ))
}

@Preview(
    showBackground = true,
    widthDp = 1000,
    heightDp = 400,
)
@Composable
private fun ResultPhotoAndScoreLandscapePreview() {
    ResultPhotosAndScoreLandscape(
        result = Result(
            randomPhotoUrl = "randomPhotoUrl",
            cameraPhotoUri = Uri.EMPTY,
            similarityScore = 50,
            cameraPhotoLabels = listOf("Elephant"),
            randomPhotoLabels = listOf("Tree"),
            userName = "",
        )
    )
}


@Composable
fun ResultPhotosAndScorePortrait(
    modifier: Modifier = Modifier,
    result: Result,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Row {
            RandomPhoto(
                url = result.randomPhotoUrl,
                modifier = Modifier.weight(1f),
                labels = result.randomPhotoLabels,
            )
            Spacer(modifier = Modifier.width(Sizes.s10))
            CameraPhoto(
                uri = result.cameraPhotoUri,
                modifier = Modifier.weight(1f),
                labels = result.cameraPhotoLabels,
            )
        }
        SimilarityScore(
            score = result.similarityScore,
        )
    }
}

@Composable
fun ResultPhotosAndScoreLandscape(
    modifier: Modifier = Modifier,
    result: Result,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RandomPhoto(
            url = result.randomPhotoUrl,
            modifier = Modifier.weight(1f),
            labels = result.randomPhotoLabels,
        )
        SimilarityScore(
            score = result.similarityScore,
            modifier = Modifier.weight(1f),
        )
        CameraPhoto(
            uri = result.cameraPhotoUri,
            modifier = Modifier.weight(1f),
            labels = result.cameraPhotoLabels,
        )
    }
}

@Composable
private fun RandomPhoto(
    modifier: Modifier = Modifier,
    url: String,
    labels: List<String> = emptyList(),
) {
    PhotoWithTitle(
        title = stringResource(id = R.string.photo_with_title__random_photo_title),
        modifier = modifier,
        textStyleTitle = MaterialTheme.typography.headlineSmall,
        imageUri = url,
        labels = labels,
    )
}

@Composable
private fun CameraPhoto(
    modifier: Modifier = Modifier,
    uri: Uri,
    labels: List<String> = emptyList(),
) {
    uri.path?.let {
        PhotoWithTitle(
            title = stringResource(id = R.string.photo_with_title__camera_photo_title),
            modifier = modifier,
            textStyleTitle = MaterialTheme.typography.headlineSmall,
            imageUri = it,
            labels = labels,
        )
    }
}

@Composable
private fun SimilarityScore(
    modifier: Modifier = Modifier,
    score: Int,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.result_screen__similarity_score_title),
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "$score%",
            style = MaterialTheme.typography.headlineLarge,
        )
        Icon(
            imageVector = Icons.Sharp.Celebration,
            contentDescription = stringResource(R.string.result_screen__icon_content_description),
            modifier = Modifier.size(100.dp)
        )
    }
}
