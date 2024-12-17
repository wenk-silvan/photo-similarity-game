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
class RegisterUserUseCaseTest {
    // region helper fields
    private val credentials = Credentials("userName", "password")
    private val resourceHandler = ResourceHandler()
    private val user = Resource.Success(User(credentials.userName))
    private val userRepository = mockk<UserRepository>()
    // endregion helper fields

    lateinit var SUT: RegisterUserUseCase

    @Before
    fun setUp() {
        setupSuccess()
        SUT = RegisterUserUseCase(userRepository, resourceHandler)
    }

    @Test
    fun registerUserNormally() = runTest {
        SUT(credentials)

        coVerify(exactly = 1) { userRepository.userExists(credentials.userName) }
        coVerify(exactly = 1) { userRepository.register(credentials) }
        confirmVerified(userRepository)
    }

    @Test
    fun doNotRegisterWhenUsernameAlreadyExists() = runTest {
        coEvery { userRepository.userExists(credentials.userName) } returns true

        val resource = SUT(credentials)

        assertThat(resource).isInstanceOf(Resource.UserExists::class.java)
    }

    @Test
    fun useCaseReturnsUser() = runTest {
        val resource = SUT(credentials)

        assertThat(resource).isEqualTo(user)
    }

    private fun setupSuccess() {
        coEvery { userRepository.userExists(credentials.userName) } returns false
        coEvery { userRepository.register(credentials) } returns user.data
    }
}
