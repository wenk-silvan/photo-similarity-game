package ch.wenksi.photosimilaritygame

import androidx.compose.runtime.Composable
import ch.wenksi.photosimilaritygame.ui.screens.login.LoginScreen
import ch.wenksi.photosimilaritygame.ui.screens.login.LoginViewModel

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    onAuthenticationSuccess: (String) -> Unit,
    onClickRegisterInstead: () -> Unit,
) {
    LoginScreen(
        uiState = viewModel.uiState,
        onClickLogin = { viewModel.login(onAuthenticationSuccess) },
        onClickRegisterInstead = onClickRegisterInstead,
        onSetUserName = { viewModel.setUserName(it) },
        onSetPassword = { viewModel.setPassword(it) },
        onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() }
    )
}
