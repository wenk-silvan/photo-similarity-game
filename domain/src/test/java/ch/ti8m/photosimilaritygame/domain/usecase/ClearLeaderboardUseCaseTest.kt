package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.repository.LeaderboardRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.usecase.ClearLeaderboardUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ClearLeaderboardUseCaseTest {

    // region helper fields
    private val leaderboardRepository = mockk<LeaderboardRepository>()
    // endregion helper fields

    lateinit var SUT: ClearLeaderboardUseCase

    @Before
    fun setUp() {
        setupSuccess()
        SUT = ClearLeaderboardUseCase(leaderboardRepository)
    }

    @Test
    fun useCaseCallsRepositoryOnce() = runTest {
        SUT()

        coVerify(exactly = 1) { leaderboardRepository.deleteResults() }
        confirmVerified(leaderboardRepository)
    }

    private fun setupSuccess() {
        coEvery { leaderboardRepository.deleteResults() } returns Resource.Success(true)
    }
}
