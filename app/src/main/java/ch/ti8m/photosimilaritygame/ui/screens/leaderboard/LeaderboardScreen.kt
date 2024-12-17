@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package ch.wenksi.photosimilaritygame.ui.screens.leaderboard

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.ui.screens.shared.*
import ch.wenksi.photosimilaritygame.ui.theme.Sizes
import kotlinx.coroutines.launch

@Composable
fun LeaderboardScreen(
    uiState: LeaderboardViewModel.UiState,
    onClickClear: () -> Unit,
    onClickPlayAgain: () -> Unit,
    onClickResult: (Int) -> Unit,
    onClickTryAgain: () -> Unit,
) {
    when (uiState) {
        is LeaderboardViewModel.UiState.Success -> {
            Column(modifier = Modifier
                .testTag(Destination.Leaderboard.route)
                .fillMaxSize()
                .padding(Sizes.screenContentPadding)) {
                ResultList(
                    modifier = Modifier.weight(1f),
                    results = uiState.results,
                    onClickResult = onClickResult,
                )
                BottomActionBar(
                    modifier = Modifier.height(Sizes.bottomActionBarRegular),
                    onClickPlayAgain = onClickPlayAgain,
                    onClickClear = onClickClear,
                    onClickTryAgain = onClickTryAgain
                )
            }
        }
        is LeaderboardViewModel.UiState.Loading -> LoadingBox()
        is LeaderboardViewModel.UiState.Error -> ErrorScreen(
            message = stringResource(R.string.leaderboard_screen__error)
        )
    }
}

@Composable
private fun ResultList(
    modifier: Modifier = Modifier,
    results: List<Result>,
    onClickResult: (Int) -> Unit,
) {
    val state = rememberLazyListState()
    val showScrollToTopButton = remember {
        derivedStateOf {
            state.firstVisibleItemIndex > 0
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = state,
        ) {
            item { Title(text = stringResource(R.string.leaderboard_screen__title)) }
            itemsIndexed(results) { index, item ->
                ResultListItem(
                    result = item,
                    onClickResult = { onClickResult(index) },
                )
            }
        }

        if (showScrollToTopButton.value) {
            ScrollToTopButton(
                state = state,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun ScrollToTopButton(
    state: LazyListState,
    modifier: Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    OutlinedIconButton(
        modifier = modifier,
        onClick = {
            coroutineScope.launch {
                state.animateScrollToItem(0)
            }
        },
    ) {
        Icon(
            imageVector = Icons.Default.ArrowUpward,
            contentDescription = stringResource(R.string.leaderboard_screen__up_button_content_description),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultListItem(
    modifier: Modifier = Modifier,
    result: Result,
    onClickResult: () -> Unit = { },
) {
    ListItem(
        modifier = modifier,
        leadingContent = { Text("#${result.rank}") },
        headlineText = { Text(text = "${result.userName}: ${result.similarityScore}%") },
        trailingContent = {
            IconButton(onClick = onClickResult) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = stringResource(R.string.leaderboard_screen__result_button_content_description)
                )
            }
        },
    )
    Divider()
}

@Composable
private fun BottomActionBar(
    modifier: Modifier = Modifier,
    onClickPlayAgain: () -> Unit = {},
    onClickTryAgain: () -> Unit = {},
    onClickClear: () -> Unit = {},
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
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = stringResource(R.string.result_screen__button_try_again)
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
            TextButton(onClick = onClickClear) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.leaderboard_screen__delete_button_content_description)
                )
            }
        }
    }
}

@AllPreviews
@Composable
private fun PreviewSuccessAll() {
    ThemedPreview {
        LeaderboardScreen(
            uiState = LeaderboardViewModel.UiState.Success(listOf(
                Result("Test", 1, "", Uri.EMPTY, 1, emptyList(), emptyList()),
                Result("Test", 1, "", Uri.EMPTY, 1, emptyList(), emptyList()),
                Result("Test", 1, "", Uri.EMPTY, 1, emptyList(), emptyList()),
                Result("Test", 1, "", Uri.EMPTY, 1, emptyList(), emptyList()),
                Result("Test", 1, "", Uri.EMPTY, 1, emptyList(), emptyList()),
                Result("Test", 1, "", Uri.EMPTY, 1, emptyList(), emptyList()),
                Result("Test", 1, "", Uri.EMPTY, 1, emptyList(), emptyList()),
            )),
            onClickClear = {},
            onClickPlayAgain = {},
            onClickResult = {},
            onClickTryAgain = {},
        )
    }
}

@Preview
@Composable
fun PreviewLoading() {
    ThemedPreview {
        LeaderboardScreen(
            uiState = LeaderboardViewModel.UiState.Loading,
            onClickClear = {},
            onClickPlayAgain = {},
            onClickResult = {},
            onClickTryAgain = {},
        )
    }
}


@Preview
@Composable
fun PreviewError() {
    ThemedPreview {
        LeaderboardScreen(
            uiState = LeaderboardViewModel.UiState.Error,
            onClickClear = {},
            onClickPlayAgain = {},
            onClickResult = {},
            onClickTryAgain = {},
        )
    }
}
