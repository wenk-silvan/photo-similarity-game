package ch.wenksi.photosimilaritygame.ui.screens.result

import android.net.Uri
import ch.wenksi.photosimilaritygame.domain.model.CreateResultRequest
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.usecase.CreateResultUseCase
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

class ResultViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    // region constants
    private val userName = "TestUser"
    private val randomPhotoUrl = "randomPhotoUrl"
    // endregion constants

    // region helper fields
    private val cameraPhotoUri = mockk<Uri>()
    private val createResultUseCase = mockk<CreateResultUseCase>()
    private val createResultRequest = CreateResultRequest(userName, randomPhotoUrl, cameraPhotoUri)
    private val result = Resource.Success(
        Result(
            userName = userName,
            similarityScore = 50,
            randomPhotoUrl = randomPhotoUrl,
            cameraPhotoUri = cameraPhotoUri,
            randomPhotoLabels = emptyList(),
            cameraPhotoLabels = emptyList(),
        )
    )
    // endregion helper fields

    lateinit var SUT: ResultViewModel

    @Before
    fun setUp() {
        setupSuccess()
        SUT = ResultViewModel(
            createResultUseCase = createResultUseCase,
        )
    }

    @Test
    fun createResultCallsUseCaseOnce() {
        SUT.createResult(randomPhotoUrl, cameraPhotoUri, userName)

        coVerify { createResultUseCase(createResultRequest) }
        confirmVerified(createResultUseCase)
    }

    @Test
    fun createResultSetsResultInUiState() {
        SUT.createResult(randomPhotoUrl, cameraPhotoUri, userName)

        assertThat((SUT.uiState as ResultViewModel.UiState.Success).result)
            .isEqualTo(result.data)
    }

    private fun setupSuccess() {
        coEvery { createResultUseCase(createResultRequest) } returns result
    }
}
