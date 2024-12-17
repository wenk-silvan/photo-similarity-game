package ch.wenksi.photosimilaritygame

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import ch.wenksi.photosimilaritygame.ui.screens.login.LoginScreen
import ch.wenksi.photosimilaritygame.ui.screens.login.LoginViewModel

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    onAuthenticationSuccess: (String) -> Unit,
    onClickRegisterInstead: () -> Unit,
) {
    val context = LocalContext.current
    LoginScreen(
        onClickLogin = { userName ->
            loginWithBiometrics(
                context = context as FragmentActivity,
                onAuthenticationSuccess = { onAuthenticationSuccess(userName) },
                onAuthenticationError = {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                },
            )
        },
        onClickRegisterInstead = onClickRegisterInstead,
    )
}

fun loginWithBiometrics(
    context: FragmentActivity,
    onAuthenticationSuccess: () -> Unit,
    onAuthenticationError: (String) -> Unit,
) {
    BiometricPrompt.PromptInfo.Builder()
        .setTitle(context.getString(R.string.login_screen__prompt_title))
        .setNegativeButtonText(context.getString(R.string.login_screen__prompt_cancel))
        .build()
        .apply {
            BiometricPrompt(context, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(code: Int, error: CharSequence) =
                    onAuthenticationError(error.toString())

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) =
                    onAuthenticationSuccess()
            }).authenticate(this)
        }
}
