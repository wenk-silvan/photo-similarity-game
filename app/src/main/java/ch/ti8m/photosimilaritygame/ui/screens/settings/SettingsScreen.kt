package ch.wenksi.photosimilaritygame.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Summarize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.ui.screens.shared.AllPreviews
import ch.wenksi.photosimilaritygame.ui.screens.shared.ClearTextFieldIconButton
import ch.wenksi.photosimilaritygame.ui.screens.shared.ThemedPreview
import ch.wenksi.photosimilaritygame.ui.screens.shared.Title
import ch.wenksi.photosimilaritygame.ui.theme.Sizes
import ch.wenksi.photosimilaritygame.ui.theme.Typography

@Composable
fun SettingsScreen(
    uiState: SettingsViewModel.UiState,
    userName: String,
    onSaveUserName: (String) -> Unit,
    onClickBack: () -> Unit,
    onClickLogout: () -> Unit,
    onToggleGenerateLocally: (Boolean) -> Unit,
    onClickRules: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
    onClickTermsConditions: () -> Unit,
) {
    Column(
        Modifier
            .testTag(Destination.Settings.route)
            .fillMaxSize()
            .padding(Sizes.screenContentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Title(text = stringResource(R.string.settings_screen__title))
        ToggleButtonRow(
            text = stringResource(R.string.settings_screen__generate_locally),
            value = uiState.settings.generatePhotoLocally,
            onToggle = onToggleGenerateLocally,
        )
        UserNameTextField(userName, uiState, onSaveUserName)
        FurtherInfoButtons(onClickRules, onClickPrivacyPolicy, onClickTermsConditions)
        Spacer(modifier = Modifier.weight(1f))
        LogoutButton(onClickLogout)
        BottomActionBar(
            modifier = Modifier.height(Sizes.bottomActionBarRegular),
            onClickBack = onClickBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserNameTextField(
    userName: String,
    uiState: SettingsViewModel.UiState,
    onSaveUserName: (String) -> Unit,
) {
    var editUserName: String by rememberSaveable { mutableStateOf(userName) }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Sizes.s30, vertical = Sizes.s20),
        value = editUserName,
        label = { Text(stringResource(R.string.label_user_name)) },
        onValueChange = { editUserName = it },
        singleLine = true,
        isError = uiState.isError,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            onSaveUserName(editUserName)
        }),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.form_icon_user_name),
            )
        },
        trailingIcon = {
            ClearTextFieldIconButton { editUserName = "" }
        },
    )
    TextButton(onClick = {
        onSaveUserName(editUserName)
    }) {
        Text(text = stringResource(R.string.settings_screen__change_username))
    }
}

@Composable
private fun ToggleButtonRow(
    modifier: Modifier = Modifier,
    text: String,
    value: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = Sizes.s30,
                vertical = Sizes.s20,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = value,
            onCheckedChange = onToggle,
        )
    }
}

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    OutlinedButton(onClick) {
        Text(text = stringResource(R.string.settings_screen_logout))
    }
}

@Composable
fun FurtherInfoButtons(
    onClickRules: () -> Unit,
    onClickPrivacyPolicy: () -> Unit,
    onClickTermsConditions: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Sizes.s30, vertical = Sizes.s20),
        horizontalArrangement = Arrangement.Center,
    ) {
        IconTextButton(
            onClick = onClickRules,
            text = stringResource(R.string.settings_screen__rules),
            vector = Icons.Outlined.Summarize,
        )
        Spacer(modifier = Modifier.size(Sizes.s10))
        IconTextButton(
            onClick = onClickPrivacyPolicy,
            text = stringResource(R.string.settings_screen__privacy_policy),
            vector = Icons.Outlined.Policy,
        )
        Spacer(modifier = Modifier.size(Sizes.s10))
        IconTextButton(
            onClick = onClickTermsConditions,
            text = stringResource(R.string.settings_screen__terms_conditions),
            vector = Icons.Outlined.Gavel,
        )
    }
}

@Composable
private fun IconTextButton(onClick: () -> Unit, text: String, vector: ImageVector) {
    Button(onClick, modifier = Modifier.width(Sizes.s120)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(vector, text)
            Spacer(modifier = Modifier.size(Sizes.s5))
            Text(
                text = text,
                style = Typography.bodySmall,
            )
        }
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
private fun Preview() {
    ThemedPreview {
        SettingsScreen(
            uiState = SettingsViewModel.UiState(),
            onClickBack = {},
            onClickLogout = {},
            onSaveUserName = {},
            onToggleGenerateLocally = {},
            userName = "userName",
            onClickPrivacyPolicy = {},
            onClickRules = {},
            onClickTermsConditions = {},
        )
    }
}
