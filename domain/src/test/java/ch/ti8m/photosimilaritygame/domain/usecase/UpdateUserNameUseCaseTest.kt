package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.repository.UserRepository
import ch.wenksi.photosimilaritygame.domain.usecase.UpdateUserNameUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateUserNameUseCaseTest {
    // region helper fields
    private val old = "old"
    private val new = "new"
    private val repository = mockk<UserRepository>()
    // endregion helper fields

    lateinit var SUT: UpdateUserNameUseCase

    @Before
    fun setUp() {
        setupSuccess()
        SUT = UpdateUserNameUseCase(repository)
    }

    @Test
    fun useCaseCallsRepositoryOnce() = runTest {
        SUT("old", "new")

        coVerify(exactly = 1) { repository.updateUserName(old, new) }
        confirmVerified(repository)
    }

    private fun setupSuccess() {
        coEvery { repository.updateUserName(old, new) } returns Unit
    }
}
