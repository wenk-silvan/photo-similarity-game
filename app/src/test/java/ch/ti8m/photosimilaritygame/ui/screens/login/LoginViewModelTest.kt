package ch.wenksi.photosimilaritygame.ui.screens.login

import ch.wenksi.photosimilaritygame.domain.model.Credentials
import ch.wenksi.photosimilaritygame.domain.model.User
import ch.wenksi.photosimilaritygame.domain.usecase.LoginSessionUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.LoginUserUseCase
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.helper.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    // region helper fields
    private val credentials = Credentials(
        userName = "userName",
        password = "password"
    )
    private val user = User(userName = credentials.userName)
    private val loginUserUseCase = mockk<LoginUserUseCase>()
    private val loginSessionUseCase = mockk<LoginSessionUseCase>()
    // endregion helper fields

    private lateinit var SUT: LoginViewModel

    @Before
    fun setUp() {
        setupSuccess()
        SUT = LoginViewModel(loginUserUseCase, loginSessionUseCase)
    }

    private fun setupSuccess() {
        coEvery { loginUserUseCase(credentials) } returns Resource.Success(user)
        coEvery { loginSessionUseCase() } returns Resource.Success(user)
    }

    @Test
    fun loginCallsUseCaseOnce() {
        SUT.setUserName(credentials.userName)
        SUT.setPassword(credentials.password)
        val onSuccess = { userName: String ->
            assertThat(userName).isEqualTo(credentials.userName)
        }

        SUT.login(onSuccess)

        coVerify(exactly = 1) { loginUserUseCase(credentials) }
        confirmVerified(loginUserUseCase)
    }

    @Test
    fun successfulLoginReturnsUserName() {
        SUT.setUserName(credentials.userName)
        SUT.setPassword(credentials.password)
        val onSuccess = { userName: String ->
            assertThat(userName).isEqualTo(credentials.userName)
        }

        SUT.login(onSuccess)
    }

    @Test
    fun loginResetsPasswordInUiState() {
        SUT.setUserName(credentials.userName)
        SUT.setPassword(credentials.password)
        val onSuccess = { userName: String ->
            assertThat(userName).isEqualTo(credentials.userName)
        }
        assertThat(SUT.uiState.password).isNotEmpty()

        SUT.login(onSuccess)

        assertThat(SUT.uiState.password).isEqualTo("")
    }

    @Test
    fun loginSetsErrorInUiStateWhenCredentialsAreInvalid() {
        SUT.setUserName(credentials.userName)
        SUT.setPassword(credentials.password)
        coEvery { loginUserUseCase.invoke(credentials) } returns Resource.InvalidCredentials()

        SUT.login {}

        assertThat(SUT.uiState.error).isNotNull()
    }

    @Test
    fun trySessionLoginCallsUseCaseOnce() {
        val onSuccess = { userName: String ->
            assertThat(userName).isEqualTo(credentials.userName)
        }

        SUT.trySessionLogin(onSuccess)

        coVerify { loginSessionUseCase() }
        confirmVerified(loginSessionUseCase)
    }

    @Test
    fun userNameGetsSetInUiState() {
        SUT.setUserName(credentials.userName)

        assertThat(SUT.uiState.userName).isEqualTo(credentials.userName)
    }

    @Test
    fun passwordGetsSetInUiState() {
        SUT.setPassword(credentials.password)

        assertThat(SUT.uiState.password).isEqualTo(credentials.password)
    }
}
