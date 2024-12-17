package ch.wenksi.photosimilaritygame.ui.screens.settings

import android.content.Context
import ch.wenksi.photosimilaritygame.domain.model.Settings
import ch.wenksi.photosimilaritygame.domain.usecase.GetSettingsUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.LogoutUserUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.UpdateSettingsUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.UpdateUserNameUseCase
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.helper.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    // region helper fields
    private val logoutUserUseCase = mockk<LogoutUserUseCase>()
    private val getSettingsUseCase = mockk<GetSettingsUseCase>()
    private val updateSettingsUseCase = mockk<UpdateSettingsUseCase>()
    private val updateUserNameUseCase = mockk<UpdateUserNameUseCase>()
    private val context = mockk<Context>(relaxed = true)
    private val settings = Settings()
    private val settingsResource = Resource.Success(settings)
    private val old = "old"
    private val new = "new"
    // endregion helper fields

    lateinit var SUT: SettingsViewModel

    @Before
    fun setUp() {
        setupSuccess()
        SUT = SettingsViewModel(
            context,
            logoutUserUseCase,
            getSettingsUseCase,
            updateSettingsUseCase,
            updateUserNameUseCase)
    }

    @Test
    fun savingUsernameCallsUseCaseOnce() = runTest {
        SUT.setEditUserName(new)

        SUT.saveUserName(old, new) {}

        coVerify(exactly = 1) { updateUserNameUseCase(old, new) }
        confirmVerified(updateUserNameUseCase)
    }

    @Test
    fun savingEmptyUsernameDoesNotCallUseCase() = runTest {
        SUT.saveUserName(old, new) {}

        coVerify(exactly = 0) { updateUserNameUseCase(old, "") }
        confirmVerified(updateUserNameUseCase)
    }

    @Test
    fun togglingGeneratePhotoLocallyUpdatesUiState() {
        val value = !SUT.uiState.settings.generatePhotoLocally

        SUT.setGenerateLocally(value)

        assertThat(SUT.uiState.settings.generatePhotoLocally).isEqualTo(value)
    }

    @Test
    fun togglingGeneratePhotoLocallyCallsUseCaseOnce() {
        val value = SUT.uiState.settings.generatePhotoLocally

        SUT.setGenerateLocally(value)

        coVerify(exactly = 1) { updateSettingsUseCase(settings) }
        confirmVerified(updateSettingsUseCase)
    }

    @Test
    fun loggingOutUserCallsUseCaseOnce() {
        SUT.logout({})

        coVerify(exactly = 1) { logoutUserUseCase() }
        confirmVerified(logoutUserUseCase)
    }

    private fun setupSuccess() {
        every { logoutUserUseCase() } returns Resource.Success(Any())
        every { getSettingsUseCase() } returns settingsResource
        every { updateSettingsUseCase(settings) } returns Unit
        coEvery { updateUserNameUseCase(old, new) } returns Unit
    }
}