package ch.wenksi.photosimilaritygame.repository

import ch.wenksi.photosimilaritygame.datasource.sharedpreferences.SettingsSharedPreferences
import ch.wenksi.photosimilaritygame.domain.model.Settings
import ch.wenksi.photosimilaritygame.domain.repository.SettingsRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler

class BasicSettingsRepository(
    private val resourceHandler: ResourceHandler,
    private val settingsSharedPrefs: SettingsSharedPreferences,
) : SettingsRepository {
    override fun getSettings(): Resource<Settings> {
        return try {
            val settings = settingsSharedPrefs.read()
            resourceHandler.handleSuccess(settings)
        } catch (e: Exception) {
            resourceHandler.handleException(e)
        }
    }

    override fun updateSettings(settings: Settings) {
        try {
            settingsSharedPrefs.write(settings)
        } catch (e: Exception) {
            resourceHandler.handleException<Settings>(e)
        }
    }
}