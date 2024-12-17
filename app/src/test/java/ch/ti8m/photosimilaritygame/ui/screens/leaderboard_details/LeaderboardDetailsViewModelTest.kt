package ch.wenksi.photosimilaritygame.ui.screens.leaderboard_details

import android.net.Uri
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.usecase.GetResultUseCase
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

class LeaderboardDetailsViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    // region helper fields
    private val getResultUseCase = mockk<GetResultUseCase>()
    private val cameraPhotoUri = mockk<Uri>()
    private val resultIndex = 0
    private val emptyList: List<String> = emptyList()
    private val getResultResponse = Resource.Success(
        Result(
            userName = "userName",
            similarityScore = 50,
            randomPhotoUrl = "randomPhotoUrl",
            cameraPhotoUri = cameraPhotoUri,
            randomPhotoLabels = emptyList,
            cameraPhotoLabels = emptyList,
        )
    )
    // endregion helper fields

    lateinit var SUT: LeaderboardDetailsViewModel

    @Before
    fun setUp() {
        setupSuccess()
        SUT = LeaderboardDetailsViewModel(getResultUseCase)
    }

    @Test
    fun getResultCallsUseCaseOnce() {
        SUT.getResult(resultIndex)

        coVerify(exactly = 1) { getResultUseCase(resultIndex) }
        confirmVerified(getResultUseCase)
    }

    @Test
    fun getResultSetsResultInUiState() {
        SUT.getResult(resultIndex)

        assertThat((SUT.uiState as LeaderboardDetailsViewModel.UiState.Success).result)
            .isEqualTo(getResultResponse.data)
    }

    private fun setupSuccess() {
        coEvery { getResultUseCase(resultIndex) } returns getResultResponse
    }
}