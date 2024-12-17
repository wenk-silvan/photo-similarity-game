package ch.wenksi.photosimilaritygame.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
fun LoginScreen(
    onClickLogin: (String) -> Unit,
    onClickRegisterInstead: () -> Unit,
) {
    Column(
        modifier = Modifier
            .testTag(Destination.Login.route)
            .fillMaxSize()
            .padding(Sizes.screenContentPadding)
    ) {
        var userName by rememberSaveable { mutableStateOf("") }
        val onLogin = { onClickLogin(userName) }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Header()
            Spacer(modifier = Modifier.height(Sizes.s10))
            UserNameTextField(
                value = userName,
                onValueChange = { userName = it },
                onDone = onLogin
            )
        }
        BottomActionBar(
            modifier = Modifier.height(Sizes.bottomActionBarLarge),
            onClickLogin = onLogin,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserNameTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        label = { Text(stringResource(R.string.label_user_name)) },
        onValueChange = { onValueChange(it)},
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.form_icon_user_name),
            )
        },
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        trailingIcon = {
            ClearTextFieldIconButton { onValueChange("") }
        },
    )
}

@Composable
private fun Logo(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.clip(CircleShape),
        painter = painterResource(R.mipmap.ic_logo_adaptive_fore),
        contentDescription = stringResource(R.string.logo),
        contentScale = ContentScale.Crop,
    )
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
            onClickLogin = {},
            onClickRegisterInstead = {},
        )
    }
}
