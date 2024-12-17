package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.Credentials
import ch.wenksi.photosimilaritygame.domain.model.User
import ch.wenksi.photosimilaritygame.domain.repository.UserRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUserUseCaseTest {
    // region helper fields
    private val credentials = Credentials("userName", "password")
    private val resourceHandler = ResourceHandler()
    private val user = User(credentials.userName)
    private val userRepository = mockk<UserRepository>()
    // endregion helper fields

    lateinit var SUT: LoginUserUseCase

    @Before
    fun setUp() {
        setupSuccess()
        SUT = LoginUserUseCase(userRepository, resourceHandler)
    }

    @Test
    fun loginCalledWhenCredentialsCorrect() = runTest {
        SUT(credentials)

        coVerify(exactly = 1) { userRepository.login(credentials.userName) }
        coVerify(exactly = 1) { userRepository.hasOngoingSession() }
        coVerify(exactly = 1) { userRepository.credentialsAreCorrect(credentials) }
        confirmVerified(userRepository)
    }

    @Test
    fun loginNotCalledWhenOngoingSession() = runTest {
        coEvery { userRepository.hasOngoingSession() } returns true

        SUT(credentials)

        coVerify(exactly = 0) { userRepository.login(credentials.userName) }
        coVerify(exactly = 1) { userRepository.hasOngoingSession() }
        confirmVerified(userRepository)
    }

    @Test
    fun loginNotCalledWhenCredentialsInvalid() = runTest {
        coEvery { userRepository.credentialsAreCorrect(credentials) } returns false

        SUT(credentials)

        coVerify(exactly = 0) { userRepository.login(credentials.userName) }
        coVerify(exactly = 1) { userRepository.hasOngoingSession() }
        coVerify(exactly = 1) { userRepository.credentialsAreCorrect(credentials) }
        confirmVerified(userRepository)
    }

    @Test
    fun useCaseReturnsUser() = runTest {
        val resource = SUT(credentials)

        assertThat(resource).isInstanceOf(Resource.Success::class.java)
        assertThat((resource as Resource.Success).data).isEqualTo(user)
    }

    private fun setupSuccess() {
        coEvery { userRepository.hasOngoingSession() } returns false
        coEvery { userRepository.credentialsAreCorrect(credentials) } returns true
        coEvery { userRepository.login(credentials.userName) } returns user
    }
}
