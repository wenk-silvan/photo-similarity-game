package ch.wenksi.photosimilaritygame.ui.screens.photo

import ch.wenksi.photosimilaritygame.domain.logic.ConnectivityObserver
import ch.wenksi.photosimilaritygame.domain.model.Settings
import ch.wenksi.photosimilaritygame.domain.usecase.GetRandomPhotoUrlUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.GetSettingsUseCase
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.helper.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PhotoViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    // region constants
    private val randomPhotoUrl = "randomPhotoUrl"
    // endregion constants

    // region helper fields
    private val getRandomPhotoUrlUseCase = mockk<GetRandomPhotoUrlUseCase>()
    private val getSettingsUseCase = mockk<GetSettingsUseCase>()
    private val connectivityObserver = mockk<ConnectivityObserver>()
    private val randomPhotoUrlResource = Resource.Success(randomPhotoUrl)
    private val settingsResource = Resource.Success(Settings())
    // endregion helper fields

    lateinit var SUT: PhotoViewModel

    @Before
    fun setUp() {
        setupSuccess()
        SUT = PhotoViewModel(getRandomPhotoUrlUseCase, getSettingsUseCase, connectivityObserver)
    }

    @Test
    fun startGameConnectionAvailableUrlSet() {
        SUT.startGame(ConnectivityObserver.Status.Available)

        assertThat((SUT.uiState as PhotoViewModel.UiState.Online).randomPhotoUrl)
            .isEqualTo(randomPhotoUrlResource.data)
    }

    @Test
    fun startGameConnectionAvailableCallsUseCaseOnce() {
        SUT.startGame(ConnectivityObserver.Status.Available)

        coVerify(exactly = 1) { getRandomPhotoUrlUseCase() }
        confirmVerified(getRandomPhotoUrlUseCase)
    }

    private fun setupSuccess() {
        coEvery { getRandomPhotoUrlUseCase() } returns randomPhotoUrlResource
        coEvery { getSettingsUseCase() } returns settingsResource
        every { connectivityObserver.observe() } returns flow {}
    }
}
