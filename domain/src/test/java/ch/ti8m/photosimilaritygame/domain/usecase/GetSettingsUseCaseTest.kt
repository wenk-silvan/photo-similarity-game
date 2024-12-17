package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.Settings
import ch.wenksi.photosimilaritygame.domain.repository.SettingsRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.usecase.GetSettingsUseCase
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class GetSettingsUseCaseTest {

    // region helper fields
    private val repository = mockk<SettingsRepository>()
    private val settingsResource = Resource.Success(Settings())
    // endregion helper fields

    lateinit var SUT: GetSettingsUseCase

    @Before
    fun setUp() {
        setupSuccess()
        SUT = GetSettingsUseCase(repository)
    }

    @Test
    fun useCaseCallsRepositoryOnce() {
        SUT()

        coVerify(exactly = 1) { repository.getSettings() }
        confirmVerified(repository)
    }

    private fun setupSuccess() {
        every { repository.getSettings() } returns settingsResource
    }
}