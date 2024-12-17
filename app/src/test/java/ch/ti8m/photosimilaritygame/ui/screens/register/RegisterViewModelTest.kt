package ch.wenksi.photosimilaritygame.ui.screens.register

import ch.wenksi.photosimilaritygame.domain.model.Credentials
import ch.wenksi.photosimilaritygame.domain.model.User
import ch.wenksi.photosimilaritygame.domain.usecase.RegisterUserUseCase
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.helper.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegisterViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    // region helper fields
    private val credentials = Credentials(
        userName = "userName",
        password = "password"
    )
    private val user = User(userName = credentials.userName)
    private val registerUserUseCase = mockk<RegisterUserUseCase>()

    private lateinit var SUT: RegisterViewModel
    // endregion helper fields

    @Before
    fun setUp() {
        SUT = RegisterViewModel(registerUserUseCase)
        setupSuccess()
    }

    @Test
    fun registerCallsUseCaseOnce() {
        val onSuccess: (String) -> Unit = { assertThat(true).isTrue() }

        SUT.register(onSuccess)

        coVerify(exactly = 1) { registerUserUseCase(credentials) }
        confirmVerified(registerUserUseCase)
    }

    @Test
    fun registerDoesNotCallUseCaseWhenPasswordsDoNotMatch() {
        SUT.setRepeatPassword("")
        val onSuccess: (String) -> Unit = { assertThat(true).isTrue() }

        SUT.register(onSuccess)

        coVerify(exactly = 0) { registerUserUseCase.invoke(credentials) }
        confirmVerified(registerUserUseCase)
        assertThat(SUT.uiState.error).isNotNull()
    }

    @Test
    fun registerDoesNotCallUseCaseWhenPasswordsIsEmpty() {
        val cred = Credentials(credentials.userName, "")
        coEvery { registerUserUseCase.invoke(cred) } returns Resource.Success(user)
        SUT.setUserName(cred.userName)
        SUT.setPassword(cred.password)
        SUT.setRepeatPassword(cred.password)
        val onSuccess: (String) -> Unit = { assertThat(true).isTrue() }

        SUT.register(onSuccess)

        coVerify(exactly = 0) { registerUserUseCase(cred) }
        confirmVerified(registerUserUseCase)
        assertThat(SUT.uiState.error).isNotNull()
    }

    @Test
    fun registerResetsCredentialsAndErrorInUiState() {
        val onSuccess: (String) -> Unit = { assertThat(true).isTrue() }

        SUT.register(onSuccess)

        assertThat(SUT.uiState.userName).isEmpty()
        assertThat(SUT.uiState.password).isEmpty()
        assertThat(SUT.uiState.error).isNull()
    }

    @Test
    fun registerSetsErrorInUiStateWhenCredentialsAreInvalid() {
        coEvery { registerUserUseCase(credentials) } returns Resource.InvalidCredentials()

        SUT.register {}

        assertThat(SUT.uiState.error).isNotNull()
    }

    @Test
    fun registerSetsErrorInUiStateWhenUsernameExists() {
        coEvery { registerUserUseCase(credentials) } returns Resource.UserExists()

        SUT.register {}

        assertThat(SUT.uiState.error).isNotNull()
    }

    @Test
    fun userNameGetsSetInUiState() {
        assertThat(SUT.uiState.userName).isEqualTo(credentials.userName)
    }

    @Test
    fun passwordGetsSetInUiState() {
        assertThat(SUT.uiState.password).isEqualTo(credentials.password)
    }

    @Test
    fun repeatPasswordGetsSetInUiState() {
        assertThat(SUT.uiState.repeatPassword).isEqualTo(credentials.password)
    }

    private fun setupSuccess() {
        SUT.setUserName(credentials.userName)
        SUT.setPassword(credentials.password)
        SUT.setRepeatPassword(credentials.password)

        coEvery { registerUserUseCase(credentials) } returns Resource.Success(user)
    }
}
