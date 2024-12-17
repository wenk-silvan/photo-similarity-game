package ch.wenksi.photosimilaritygame.ui.screens.photo

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.ui.screens.shared.AllPreviews
import ch.wenksi.photosimilaritygame.ui.screens.shared.ThemedPreview
import ch.wenksi.photosimilaritygame.ui.theme.Sizes
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage

@Composable
fun RandomPhotoScreen(
    uiState: PhotoViewModel.UiState,
    userName: String,
    onClickTakePhoto: (String) -> Unit,
    onClickPick: () -> Unit,
    onClickSettings: () -> Unit,
    onClickLeaderboard: () -> Unit,
) {
    Column(
        modifier = Modifier
            .testTag(Destination.Photo.route)
            .fillMaxSize()
            .padding(Sizes.screenContentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Title(userName = userName)

        Spacer(modifier = Modifier.height(Sizes.s20))

        val modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        when (uiState) {
            is PhotoViewModel.UiState.Online -> OnlineImage(
                modifier = modifier,
                url = uiState.randomPhotoUrl
            )
            is PhotoViewModel.UiState.Loading -> LoadingBox(modifier)
            is PhotoViewModel.UiState.Offline -> OfflineImage(
                modifier = modifier,
                uri = uiState.galleryPhotoUri,
                onClick = onClickPick,
            )
        }

        Spacer(modifier = Modifier.height(Sizes.s20))

        BottomActionBar(
            modifier = Modifier.height(Sizes.bottomActionBarRegular),
            disableTakePhotoButton = (uiState is PhotoViewModel.UiState.Offline && uiState.galleryPhotoUri == Uri.EMPTY),
            onClickTakePhoto = {
                when (uiState) {
                    is PhotoViewModel.UiState.Online -> onClickTakePhoto(uiState.randomPhotoUrl)
                    is PhotoViewModel.UiState.Offline -> onClickTakePhoto(uiState.galleryPhotoUri.toString())
                    else -> {}
                }
            },
            onClickSettings = onClickSettings,
            onClickLeaderboard = onClickLeaderboard,
        )
    }
}

@Composable
private fun Title(
    modifier: Modifier = Modifier,
    userName: String,
) {
    Text(
        modifier = modifier,
        text = "${stringResource(R.string.random_photo_screen__welcome)}, $userName!",
        style = MaterialTheme.typography.labelMedium,
    )
}

@Composable
private fun OnlineImage(
    modifier: Modifier = Modifier,
    url: String,
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = url,
        loading = { LoadingBox() },
        contentDescription = null
    )
}

@Composable
private fun OfflineImage(
    modifier: Modifier = Modifier,
    uri: Uri,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uri != Uri.EMPTY) AsyncImage(
            modifier = Modifier
                .weight(1f)
                .clickable { onClick() },
            model = uri.toString(),
            contentDescription = "",
        )
        else Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            TextButton(onClick = { onClick() }) {
                Text(text = stringResource(R.string.randomPhotoScreen_pick))
            }
        }
        Spacer(modifier = Modifier.height(Sizes.s30))
        Text(
            modifier = Modifier.padding(horizontal = Sizes.s15),
            text = stringResource(R.string.randomPhotoScreen_offline),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Composable
fun LoadingBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(200.dp))
    }
}

@Composable
private fun BottomActionBar(
    modifier: Modifier = Modifier,
    disableTakePhotoButton: Boolean = false,
    onClickTakePhoto: () -> Unit,
    onClickSettings: () -> Unit,
    onClickLeaderboard: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            modifier = Modifier.weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsButton(onClick = onClickSettings)
        }
        Column(
            modifier = Modifier.weight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TakePhotoButton(
                enabled = !disableTakePhotoButton,
                onClick = onClickTakePhoto,
            )
        }
        Column(
            modifier = Modifier.weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LeaderboardButton(onClick = onClickLeaderboard)
        }
    }
}

@Composable
private fun LeaderboardButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            Icons.Default.EmojiEvents,
            stringResource(R.string.bottom_action_bar__leaderboard_content_description)
        )
    }
}

@Composable
private fun SettingsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            Icons.Default.Settings,
            stringResource(R.string.settings_content_description)
        )
    }
}

@Composable
private fun TakePhotoButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
    ) {
        Text(text = stringResource(R.string.photo_with_title__take_photo_label))
    }
}

@AllPreviews
@Composable
private fun PreviewSuccessAll() {
    ThemedPreview {
        RandomPhotoScreen(
            uiState = PhotoViewModel.UiState.Online("url"),
            userName = "userName",
            onClickTakePhoto = {},
            onClickSettings = {},
            onClickLeaderboard = {},
            onClickPick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    ThemedPreview {
        RandomPhotoScreen(
            uiState = PhotoViewModel.UiState.Loading,
            userName = "userName",
            onClickTakePhoto = {},
            onClickSettings = {},
            onClickLeaderboard = {},
            onClickPick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewNoInternet() {
    ThemedPreview {
        RandomPhotoScreen(
            uiState = PhotoViewModel.UiState.Offline(Uri.EMPTY),
            userName = "userName",
            onClickTakePhoto = {},
            onClickSettings = {},
            onClickLeaderboard = {},
            onClickPick = {},
        )
    }
}
