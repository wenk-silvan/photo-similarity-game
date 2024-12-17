package ch.wenksi.photosimilaritygame.ui.screens.leaderboard_details

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.ui.screens.shared.*
import ch.wenksi.photosimilaritygame.ui.theme.Sizes

@Composable
fun LeaderboardDetailsScreen(
    uiState: LeaderboardDetailsViewModel.UiState,
    onClickBack: () -> Unit,
) {
    when (uiState) {
        is LeaderboardDetailsViewModel.UiState.Success -> {
            Column(modifier = Modifier
                .testTag(Destination.LeaderboardDetails.route)
                .fillMaxSize()
                .padding(Sizes.screenContentPadding)) {
                Title(text = "#${uiState.result.rank} - ${uiState.result.userName}")

                val weightModifier = Modifier.weight(1f)
                val result = uiState.result
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ResultPhotosAndScorePortrait(weightModifier, result)
                } else {
                    ResultPhotosAndScoreLandscape(weightModifier, result)
                }

                BottomActionBar(
                    modifier = Modifier.height(Sizes.bottomActionBarRegular),
                    onClickBack = onClickBack,
                )
            }
        }
        is LeaderboardDetailsViewModel.UiState.Loading -> LoadingBox()
        is LeaderboardDetailsViewModel.UiState.Error -> ErrorScreen(
            message = stringResource(R.string.leaderboard_screen__error)
        )
    }
}

@Composable
private fun BottomActionBar(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
) {
    Row(modifier = modifier) {
        TextButton(
            onClick = onClickBack,
            modifier = Modifier
                .weight(0.3f)
                .fillMaxSize(),
        ) {
            Icon(imageVector = Icons.Default.ChevronLeft,
                contentDescription = stringResource(R.string.leaderboard_screen__back_button_content_description))
        }
        Spacer(modifier = Modifier.weight(0.4f))
        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@AllPreviews
@Composable
private fun PreviewSuccessAll() {
    ThemedPreview {
        LeaderboardDetailsScreen(
            uiState = LeaderboardDetailsViewModel.UiState.Success(
                result = Result(
                    userName = "Max Muster",
                    similarityScore = 50,
                    randomPhotoUrl = "",
                    cameraPhotoUri = Uri.EMPTY,
                    rank = 1,
                    cameraPhotoLabels = listOf("Elephant", "Tree"),
                    randomPhotoLabels = listOf("Coconut", "Tree"),
                )
            ),
            onClickBack = {},
        )
    }
}

@Preview
@Composable
fun PreviewLoading() {
    ThemedPreview {
        LeaderboardDetailsScreen(
            uiState = LeaderboardDetailsViewModel.UiState.Loading,
            onClickBack = {},
        )
    }
}


@Preview
@Composable
fun PreviewError() {
    ThemedPreview {
        LeaderboardDetailsScreen(
            uiState = LeaderboardDetailsViewModel.UiState.Error,
            onClickBack = {},
        )
    }
}
