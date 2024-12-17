package ch.wenksi.photosimilaritygame.domain.usecase

import android.net.Uri
import ch.wenksi.photosimilaritygame.domain.model.CreateResultRequest
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.model.SimilarityScore
import ch.wenksi.photosimilaritygame.domain.repository.LeaderboardRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateResultUseCaseTest {

    // region helper fields
    private val getSimilarityScoreUseCase = mockk<GetSimilarityScoreUseCase>()
    private val leaderboardRepository = mockk<LeaderboardRepository>()
    private val mockUri = mockk<Uri>()
    private val similarityScore = Resource.Success(SimilarityScore(
        score = 50,
        randomPhotoLabels = emptyList(),
        cameraPhotoLabels = emptyList(),
    ))
    private val request = CreateResultRequest("userName", "randomPhotoUrl", mockUri)
    private val result = Result(
        userName = request.userName,
        similarityScore = similarityScore.data.score,
        randomPhotoUrl = request.randomPhotoUrl,
        cameraPhotoUri = request.cameraPhotoUri,
        randomPhotoLabels = similarityScore.data.randomPhotoLabels,
        cameraPhotoLabels = similarityScore.data.cameraPhotoLabels,
    )
    // endregion helper fields

    lateinit var SUT: CreateResultUseCase

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        setupSuccess()
        SUT = CreateResultUseCase(leaderboardRepository, getSimilarityScoreUseCase)
    }

    @Test
    fun useCaseCallsGetSimilarityScoreUseCaseOnce() = runTest {
        SUT(request)

        coVerify { getSimilarityScoreUseCase(request.randomPhotoUrl, request.cameraPhotoUri) }
        confirmVerified(getSimilarityScoreUseCase)
    }

    @Test
    fun useCaseCallsRepositoryOnce() = runTest {
        SUT(request)

        coVerify(exactly = 1) { leaderboardRepository.storeResult(result) }
        confirmVerified(leaderboardRepository)
    }

    @Test
    fun useCaseReturnsResult() = runTest {
        val actualResult = SUT(request)

        assertThat(actualResult).isInstanceOf(Resource.Success::class.java)
        assertThat((actualResult as Resource.Success).data).isEqualTo(result)
    }

    private fun setupSuccess() {
        coEvery {
            getSimilarityScoreUseCase.invoke(request.randomPhotoUrl, request.cameraPhotoUri)
        } returns similarityScore
        coEvery { leaderboardRepository.storeResult(result) } returns Resource.Success(result)
    }
}