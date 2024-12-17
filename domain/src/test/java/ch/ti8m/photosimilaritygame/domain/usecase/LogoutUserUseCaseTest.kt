package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.repository.UserRepository
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler
import ch.wenksi.photosimilaritygame.domain.usecase.LogoutUserUseCase
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class LogoutUserUseCaseTest {
    // region helper fields
    private val userRepository = mockk<UserRepository>()
    private val resourceHandler = ResourceHandler()
    // endregion helper fields

    lateinit var SUT: LogoutUserUseCase

    @Before
    fun setUp() {
        setupSuccess()
        SUT = LogoutUserUseCase(userRepository, resourceHandler)
    }

    @Test
    fun useCaseCallsRepository() {
        SUT()

        verify(exactly = 1) { userRepository.logout() }
        confirmVerified(userRepository)
    }

    private fun setupSuccess() {
        every { userRepository.logout() } returns Unit
    }
}