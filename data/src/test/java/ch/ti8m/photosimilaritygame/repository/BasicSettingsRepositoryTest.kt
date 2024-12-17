package ch.wenksi.photosimilaritygame.repository

import ch.wenksi.photosimilaritygame.datasource.sharedpreferences.SettingsSharedPreferences
import ch.wenksi.photosimilaritygame.domain.model.Settings
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class BasicSettingsRepositoryTest {

    // region helper fields
    private val resourceHandler = ResourceHandler()
    private val sharedPrefs = mockk<SettingsSharedPreferences>()
    private val settings = Settings()
    // endregion helper fields

    lateinit var SUT: BasicSettingsRepository

    @Before
    fun setUp() {
        setupSuccess()
        SUT = BasicSettingsRepository(resourceHandler, sharedPrefs)

    }

    @Test
    fun getSettingsCallsSharedPrefsOnce() {
        SUT.getSettings()

        coVerify(exactly = 1) { sharedPrefs.read() }
        confirmVerified(sharedPrefs)
    }

    @Test
    fun updateSettingsCallsSharedPrefsOnce() {
        SUT.updateSettings(settings)

        coVerify(exactly = 1) { sharedPrefs.write(settings) }
        confirmVerified(sharedPrefs)
    }

    private fun setupSuccess() {
        every { sharedPrefs.read() } returns settings
        every { sharedPrefs.write(settings) } returns Unit
    }
}
