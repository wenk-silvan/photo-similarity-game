package ch.wenksi.photosimilaritygame.ui.screens.result

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.ui.screens.shared.*
import ch.wenksi.photosimilaritygame.ui.theme.Sizes

@Composable
fun ResultScreen(
    uiState: ResultViewModel.UiState,
    onClickLeaderboard: () -> Unit,
    onClickPlayAgain: () -> Unit,
    onClickTryAgain: () -> Unit,
) {
    BackHandler {
        onClickPlayAgain()
    }

    when (uiState) {
        is ResultViewModel.UiState.Success -> {
            Column(
                modifier = Modifier
                    .testTag(Destination.Result.route)
                    .fillMaxSize()
                    .padding(Sizes.screenContentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                RankText(rank = uiState.result.rank)

                val weightModifier = Modifier.weight(1f)
                val result = uiState.result
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    ResultPhotosAndScorePortrait(weightModifier, result)
                } else {
                    ResultPhotosAndScoreLandscape(weightModifier, result)
                }

                BottomActionBar(
                    modifier = Modifier.height(Sizes.bottomActionBarRegular),
                    onClickPlayAgain = onClickPlayAgain,
                    onClickTryAgain = onClickTryAgain,
                    onClickLeaderboard = onClickLeaderboard,
                )
            }
        }
        is ResultViewModel.UiState.Loading -> LoadingBox()
    }
}

@Composable
fun RankText(rank: Int) {
    Text(
        text = stringResource(R.string.result_screen__rank) + ": #$rank",
        style = MaterialTheme.typography.headlineMedium,
    )
}

@Composable
private fun BottomActionBar(
    modifier: Modifier = Modifier,
    onClickPlayAgain: () -> Unit = {},
    onClickTryAgain: () -> Unit = {},
    onClickLeaderboard: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Column(
            modifier = Modifier.weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = onClickTryAgain) {
                Icon(
                    Icons.Default.RestartAlt,
                    stringResource(R.string.result_screen__button_try_again),
                )
            }
        }
        Column(
            modifier = Modifier.weight(0.4f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = onClickPlayAgain) {
                Text(text = stringResource(R.string.result_screen__button_new_game))
            }
        }
        Column(
            modifier = Modifier.weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(onClick = onClickLeaderboard) {
                Icon(
                    Icons.Default.EmojiEvents,
                    stringResource(R.string.bottom_action_bar__leaderboard_content_description)
                )
            }
        }
    }
}

@AllPreviews
@Composable
private fun PreviewSuccessAll() {
    ThemedPreview {
        ResultScreen(
            uiState = ResultViewModel.UiState.Success(),
            onClickLeaderboard = {},
            onClickTryAgain = {},
            onClickPlayAgain = {},
        )
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    ThemedPreview {
        ResultScreen(
            uiState = ResultViewModel.UiState.Loading,
            onClickLeaderboard = {},
            onClickTryAgain = {},
            onClickPlayAgain = {},
        )
    }
}
