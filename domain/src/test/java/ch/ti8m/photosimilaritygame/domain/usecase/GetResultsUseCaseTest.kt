package ch.wenksi.photosimilaritygame.domain.usecase

import android.net.Uri
import ch.wenksi.photosimilaritygame.domain.model.CreateResultRequest
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.repository.LeaderboardRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
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
class GetResultsUseCaseTest {

    // region helper fields
    private val leaderboardRepository = mockk<LeaderboardRepository>()
    private val mockUri = mockk<Uri>()
    private val request = CreateResultRequest("userName", "randomPhotoUrl", mockUri)
    private val results = listOf(
        Result(
            userName = request.userName,
            similarityScore = 50,
            randomPhotoUrl = request.randomPhotoUrl,
            cameraPhotoUri = request.cameraPhotoUri,
            randomPhotoLabels = emptyList(),
            cameraPhotoLabels = emptyList(),
        )
    )
    // endregion helper fields

    lateinit var SUT: GetResultsUseCase

    @Before
    fun setUp() {
        setupSuccess()
        SUT = GetResultsUseCase(leaderboardRepository)
    }

    @Test
    fun useCaseCallsRepositoryOnce() = runTest {
        SUT()

        coVerify(exactly = 1) { leaderboardRepository.getResults() }
        confirmVerified(leaderboardRepository)
    }

    @Test
    fun useCaseReturnsResults() = runTest {
        val actualResult = SUT()

        assertThat(actualResult).isInstanceOf(Resource.Success::class.java)
        assertThat((actualResult as Resource.Success).data).isEqualTo(results)
    }

    private fun setupSuccess() {
        coEvery { leaderboardRepository.getResults() } returns Resource.Success(results)
    }
}
