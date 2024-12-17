package ch.wenksi.photosimilaritygame.domain.repository

import ch.wenksi.photosimilaritygame.domain.model.Settings
import ch.wenksi.photosimilaritygame.domain.model.Resource

interface SettingsRepository {
    fun getSettings(): Resource<Settings>
    fun updateSettings(settings: Settings)
}
