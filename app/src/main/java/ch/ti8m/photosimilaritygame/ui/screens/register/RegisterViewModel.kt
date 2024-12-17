package ch.wenksi.photosimilaritygame.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.domain.model.Credentials
import ch.wenksi.photosimilaritygame.domain.model.User
import ch.wenksi.photosimilaritygame.domain.usecase.RegisterUserUseCase
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.ui.UiText
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    fun register(onSuccess: (String) -> Unit) {
        if (uiState.password == "")
            setError(UiText.StringResource(R.string.register_credentials_empty))
        else if (uiState.password != uiState.repeatPassword)
            setError(UiText.StringResource(R.string.register_password_match))
        else
            viewModelScope.launch {
                val userResource = registerUserUseCase(credentials = Credentials(
                    userName = uiState.userName,
                    password = uiState.password,
                ))
                handleRegisterUserResource(userResource, onSuccess)
            }

    }

    fun setUserName(value: String) {
        uiState = uiState.copy(userName = value)
    }

    fun setPassword(value: String) {
        uiState = uiState.copy(password = value)
    }

    fun setRepeatPassword(value: String) {
        uiState = uiState.copy(repeatPassword = value)
    }

    fun setError(value: UiText?) {
        uiState = uiState.copy(error = value)
    }

    private fun handleRegisterUserResource(
        userResource: Resource<User>,
        onSuccess: (String) -> Unit,
    ) {
        when (userResource) {
            is Resource.Success -> {
                onSuccess(userResource.data.userName)
                uiState = uiState.copy(
                    userName = "",
                    password = "",
                    repeatPassword = "",
                    error = null,
                )
            }
            is Resource.InvalidCredentials -> {
                setError(UiText.StringResource(R.string.register_wrong_credentials))
            }
            is Resource.UserExists -> {
                setError(UiText.StringResource(R.string.register_user_exists))
            }
            else -> throw Exception()
        }
    }

    data class UiState(
        val userName: String = "",
        val password: String = "",
        val repeatPassword: String = "",
        val error: UiText? = null,
    )
}
