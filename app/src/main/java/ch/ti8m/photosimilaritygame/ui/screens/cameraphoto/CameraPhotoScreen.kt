package ch.wenksi.photosimilaritygame.ui.screens.cameraphoto

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.ui.screens.shared.AllPreviews
import ch.wenksi.photosimilaritygame.ui.screens.shared.PhotoWithTitle
import ch.wenksi.photosimilaritygame.ui.screens.shared.ThemedPreview
import ch.wenksi.photosimilaritygame.ui.theme.Sizes

@Composable
fun CameraPhotoScreen(
    randomPhotoUrl: String,
    cameraPhotoUrl: String,
    onClickRetake: () -> Unit = {},
    onClickResult: () -> Unit = {},
) {
    Column(
        Modifier
            .testTag(Destination.CameraPhoto.route)
            .fillMaxSize()
            .padding(Sizes.screenContentPadding),
    ) {
        Photos(
            modifier = Modifier.weight(1f),
            cameraPhotoUrl = cameraPhotoUrl,
            randomPhotoUrl = randomPhotoUrl,
        )
        BottomActionBar(
            modifier = Modifier.height(Sizes.bottomActionBarLarge),
            onClickRetake = onClickRetake,
            onClickResult = onClickResult,
        )
    }
}

@Composable
private fun Photos(
    modifier: Modifier = Modifier,
    randomPhotoUrl: String,
    cameraPhotoUrl: String,
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(modifier) {
            PhotoWithTitle(
                modifier = modifier.weight(1f),
                imageUri = randomPhotoUrl,
                title = stringResource(R.string.photo_with_title__random_photo_title),
            )
            PhotoWithTitle(
                modifier = modifier.weight(1f),
                imageUri = cameraPhotoUrl,
                title = stringResource(R.string.photo_with_title__camera_photo_title),
            )
        }
    } else {
        Row(modifier) {
            PhotoWithTitle(
                modifier = modifier.weight(1f),
                imageUri = randomPhotoUrl,
                title = stringResource(R.string.photo_with_title__random_photo_title),
            )
            PhotoWithTitle(
                modifier = modifier.weight(1f),
                imageUri = cameraPhotoUrl,
                title = stringResource(R.string.photo_with_title__camera_photo_title),
            )
        }
    }
}

@Composable
private fun BottomActionBar(
    modifier: Modifier = Modifier,
    onClickRetake: () -> Unit = {},
    onClickResult: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        Column(
            modifier = Modifier.weight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = onClickRetake) {
                Text(text = stringResource(R.string.take_photo_screen__retake))
            }
            Button(onClick = onClickResult) {
                Text(text = stringResource(R.string.take_photo_screen__result))
            }
        }
        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@AllPreviews
@Composable
private fun Preview() {
    ThemedPreview {
        CameraPhotoScreen(
            randomPhotoUrl = "randomPhotoUrl",
            cameraPhotoUrl = "cameraPhotoUrl",
        )
    }
}