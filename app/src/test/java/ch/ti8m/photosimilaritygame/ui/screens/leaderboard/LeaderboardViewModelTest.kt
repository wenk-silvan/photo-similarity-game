package ch.wenksi.photosimilaritygame.ui.screens.leaderboard

import android.net.Uri
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.usecase.ClearLeaderboardUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.GetResultsUseCase
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

class LeaderboardViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    // region constants
    private val cameraPhotoUri = mockk<Uri>()
    private val getResultsResponse = Resource.Success(listOf(
        Result(
            userName = "userName",
            similarityScore = 50,
            randomPhotoUrl = "randomPhotoUrl",
            cameraPhotoUri = cameraPhotoUri,
            randomPhotoLabels = emptyList(),
            cameraPhotoLabels = emptyList(),
        )
    ))
    private val clearLeaderboardResponse = Resource.Success(true)
    // endregion constants

    // region helper fields
    private val getResultsUseCase = mockk<GetResultsUseCase>()
    private val clearLeaderboardUseCase = mockk<ClearLeaderboardUseCase>()
    // endregion helper fields

    lateinit var SUT: LeaderboardViewModel

    @Before
    fun setUp() {
        setupSuccess()
        SUT = LeaderboardViewModel(getResultsUseCase, clearLeaderboardUseCase)
    }

    @Test
    fun getResultsCallsUseCaseOnce() {
        SUT.getResults()

        coVerify(exactly = 2) { getResultsUseCase() } // +1 from viewModel init function
        confirmVerified(getResultsUseCase)
    }

    @Test
    fun getResultsSetsResultsInUiState() {
        SUT.getResults()

        assertThat((SUT.uiState as LeaderboardViewModel.UiState.Success).results)
            .isEqualTo(getResultsResponse.data)
    }

    @Test
    fun clearLeaderboardCallsUseCase() {
        SUT.clearLeaderboard()

        coVerify(exactly = 1) { clearLeaderboardUseCase() }
        confirmVerified(clearLeaderboardUseCase)
    }

    private fun setupSuccess() {
        coEvery { getResultsUseCase() } returns getResultsResponse
        coEvery { clearLeaderboardUseCase() } returns clearLeaderboardResponse
    }
}
