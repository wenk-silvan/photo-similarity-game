package ch.wenksi.photosimilaritygame.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.domain.model.Credentials
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.usecase.LoginSessionUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.LoginUserUseCase
import ch.wenksi.photosimilaritygame.ui.UiText
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUserUseCase: LoginUserUseCase,
    private val loginSessionUseCase: LoginSessionUseCase,
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    fun login(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            val resource = loginUserUseCase(Credentials(
                userName = uiState.userName,
                password = uiState.password,
            ))
            when (resource) {
                is Resource.Success -> {
                    onSuccess(resource.data.userName)
                    uiState = uiState.copy(
                        userName = resource.data.userName,
                        password = "",
                        error = null,
                    )
                }
                is Resource.InvalidCredentials -> {
                    setError(UiText.StringResource(R.string.register_wrong_credentials))
                }
                else -> setError(UiText.StringResource(R.string.error_screen__server_error))
            }
        }
    }

    fun trySessionLogin(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            val resource = loginSessionUseCase()
            if (resource is Resource.Success) {
                onSuccess(resource.data.userName)
                uiState = uiState.copy(
                    userName = resource.data.userName,
                    password = "",
                    error = null,
                )
            }
        }
    }

    fun setUserName(value: String) {
        uiState = uiState.copy(
            userName = value
        )
    }

    fun setPassword(value: String) {
        uiState = uiState.copy(
            password = value
        )
    }

    fun togglePasswordVisibility() {
        uiState = uiState.copy(passwordObscured = !uiState.passwordObscured)
    }

    private fun setError(value: UiText?) {
        uiState = uiState.copy(
            error = value
        )
    }

    data class UiState(
        val userName: String = "",
        val password: String = "",
        val passwordObscured: Boolean = true,
        val error: UiText? = null,
    )
}
