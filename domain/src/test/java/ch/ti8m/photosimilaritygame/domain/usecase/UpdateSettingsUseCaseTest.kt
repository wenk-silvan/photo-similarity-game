package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.Settings
import ch.wenksi.photosimilaritygame.domain.repository.SettingsRepository
import ch.wenksi.photosimilaritygame.domain.usecase.UpdateSettingsUseCase
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class UpdateSettingsUseCaseTest {

    // region helper fields
    private val repository = mockk<SettingsRepository>()
    private val settings = Settings()
    // endregion helper fields

    lateinit var SUT: UpdateSettingsUseCase

    @Before
    fun setUp() {
        setupSuccess()
        SUT = UpdateSettingsUseCase(repository)
    }

    @Test
    fun useCaseCallsRepositoryOnce() {
        SUT(settings)

        coVerify(exactly = 1) { repository.updateSettings(settings) }
        confirmVerified(repository)
    }

    private fun setupSuccess() {
        every { repository.updateSettings(settings) } returns Unit
    }
}
