@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package ch.wenksi.photosimilaritygame.ui.screens.register

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.ui.screens.shared.AllPreviews
import ch.wenksi.photosimilaritygame.ui.screens.shared.ClearTextFieldIconButton
import ch.wenksi.photosimilaritygame.ui.screens.shared.ThemedPreview
import ch.wenksi.photosimilaritygame.ui.theme.Sizes

@Composable
fun RegisterScreen(
    uiState: RegisterViewModel.UiState,
    onClickLoginInstead: () -> Unit,
    onClickRegister: () -> Unit,
    onSetPassword: (String) -> Unit,
    onSetRepeatPassword: (String) -> Unit,
    onSetUserName: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .testTag(Destination.Login.route)
            .fillMaxSize()
            .padding(Sizes.screenContentPadding)
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            var textFieldWidthModifier = Modifier.width(400.dp)
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Header()
                textFieldWidthModifier = Modifier.fillMaxWidth()
            }
            Form(
                modifier = Modifier.fillMaxWidth(),
                textFieldWidthModifier = textFieldWidthModifier,
                uiState = uiState,
                onClickDone = onClickRegister,
                onPasswordChange = { onSetPassword(it) },
                onRepeatPasswordChange = { onSetRepeatPassword(it) },
                onUserNameChange = { onSetUserName(it) },
            )
        }
        BottomActionBar(
            modifier = Modifier.height(Sizes.bottomActionBarLarge),
            onClickRegister = onClickRegister,
            onClickRegisterInstead = onClickLoginInstead,
        )
    }
}

@Composable
private fun Header() {
    Logo(modifier = Modifier.size(250.dp))
    Spacer(modifier = Modifier.height(Sizes.s60))
    Text(
        text = stringResource(R.string.register_screen__register),
        style = MaterialTheme.typography.headlineLarge,
    )
    Spacer(modifier = Modifier.height(Sizes.s10))
}

@Composable
private fun Logo(
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier.clip(CircleShape),
        painter = painterResource(R.mipmap.ic_logo_adaptive_fore),
        contentDescription = stringResource(R.string.logo),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun Form(
    modifier: Modifier = Modifier,
    textFieldWidthModifier: Modifier,
    uiState: RegisterViewModel.UiState,
    onUserNameChange: (value: String) -> Unit,
    onPasswordChange: (value: String) -> Unit,
    onRepeatPasswordChange: (value: String) -> Unit,
    onClickDone: () -> Unit,
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            modifier = textFieldWidthModifier,
            value = uiState.userName,
            label = { Text(stringResource(R.string.label_user_name)) },
            onValueChange = { onUserNameChange(it) },
            singleLine = true,
            isError = uiState.error != null,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.form_icon_user_name),
                )
            },
            trailingIcon = {
                ClearTextFieldIconButton { onUserNameChange("") }
            },
        )
        Spacer(modifier = Modifier.height(Sizes.s10))
        OutlinedTextField(
            modifier = textFieldWidthModifier,
            value = uiState.password,
            label = { Text(stringResource(R.string.label_password)) },
            onValueChange = { onPasswordChange(it) },
            isError = uiState.error != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = stringResource(R.string.form_icon_password),
                )
            },
            trailingIcon = {
                ClearTextFieldIconButton { onPasswordChange("") }
            },
        )
        Spacer(modifier = Modifier.height(Sizes.s10))
        OutlinedTextField(
            modifier = textFieldWidthModifier,
            value = uiState.repeatPassword,
            label = { Text(stringResource(R.string.label_repeat_password)) },
            onValueChange = { onRepeatPasswordChange(it) },
            isError = uiState.error != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                onClickDone()
                keyboardController?.hide()
            }),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Password,
                    contentDescription = stringResource(R.string.form_icon_password),
                )
            },
            trailingIcon = {
                ClearTextFieldIconButton { onRepeatPasswordChange("") }
            },
        )
    }

}

@Composable
private fun BottomActionBar(
    modifier: Modifier = Modifier,
    onClickRegister: () -> Unit,
    onClickRegisterInstead: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Spacer(
            modifier = Modifier.weight(0.3f),
        )
        Column(
            modifier = Modifier.weight(0.4f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextButton(onClick = onClickRegisterInstead) {
                Text(text = stringResource(R.string.register_screen__login_instead))
            }
            Button(onClick = onClickRegister) {
                Text(text = stringResource(R.string.register_screen__register))
            }
        }
        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@AllPreviews
@Composable
private fun Preview() {
    ThemedPreview {
        RegisterScreen(
            uiState = RegisterViewModel.UiState(),
            onClickRegister = {},
            onClickLoginInstead = {},
            onSetPassword = {},
            onSetUserName = {},
            onSetRepeatPassword = {},
        )
    }
}

