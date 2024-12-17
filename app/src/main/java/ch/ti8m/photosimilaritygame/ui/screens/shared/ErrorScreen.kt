package ch.wenksi.photosimilaritygame.ui.screens.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.R

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    message: String,
    onClickRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag(Destination.Error.route),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
        )
        onClickRetry?.let {
            TextButton(onClick = it, modifier = Modifier.offset(y = 50.dp)) {
                Text(text = stringResource(R.string.retry_error_screen__btn_retry))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorScreenPreview() {
    ErrorScreen(
        message = "message",
        onClickRetry = {}
    )
}
