@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)

package ch.wenksi.photosimilaritygame.ui.screens.login

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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.ui.screens.shared.AllPreviews
import ch.wenksi.photosimilaritygame.ui.screens.shared.ClearTextFieldIconButton
import ch.wenksi.photosimilaritygame.ui.screens.shared.PasswordVisibilityTextFieldIconButton
import ch.wenksi.photosimilaritygame.ui.screens.shared.ThemedPreview
import ch.wenksi.photosimilaritygame.ui.theme.Sizes

@Composable
fun LoginScreen(
    uiState: LoginViewModel.UiState,
    onClickLogin: () -> Unit,
    onClickRegisterInstead: () -> Unit,
    onSetUserName: (String) -> Unit,
    onSetPassword: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
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
                onClickDone = onClickLogin,
                onPasswordChange = { onSetPassword(it) },
                onTogglePasswordVisibility = { onTogglePasswordVisibility() },
                onUserNameChange = { onSetUserName(it) },
            )
        }
        BottomActionBar(
            modifier = Modifier.height(Sizes.bottomActionBarLarge),
            onClickLogin = onClickLogin,
            onClickRegisterInstead = onClickRegisterInstead,
        )
    }
}

@Composable
private fun Header() {
    Logo(modifier = Modifier.size(250.dp))
    Spacer(modifier = Modifier.height(Sizes.s60))
    Text(
        text = stringResource(R.string.login_screen__login),
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
    textFieldWidthModifier: Modifier = Modifier.fillMaxWidth(),
    uiState: LoginViewModel.UiState,
    onUserNameChange: (value: String) -> Unit,
    onPasswordChange: (value: String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
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
                Row {
                    PasswordVisibilityTextFieldIconButton(!uiState.passwordObscured) {
                        onTogglePasswordVisibility()
                    }
                    ClearTextFieldIconButton { onPasswordChange("") }
                }
            },
            visualTransformation = if (uiState.passwordObscured) PasswordVisualTransformation() else VisualTransformation.None,
        )
    }
}

@Composable
private fun BottomActionBar(
    modifier: Modifier = Modifier,
    onClickLogin: () -> Unit,
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
                Text(text = stringResource(R.string.login_screen__register_instead))
            }
            Button(onClick = onClickLogin) {
                Text(text = stringResource(R.string.login_screen__login))
            }
        }
        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@AllPreviews
@Composable
private fun Preview() {
    ThemedPreview {
        LoginScreen(
            uiState = LoginViewModel.UiState(),
            onClickLogin = {},
            onClickRegisterInstead = {},
            onTogglePasswordVisibility = {},
            onSetUserName = {},
            onSetPassword = {},
        )
    }
}
