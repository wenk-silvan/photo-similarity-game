package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.Settings
import ch.wenksi.photosimilaritygame.domain.repository.SettingsRepository

class UpdateSettingsUseCase(private val repository: SettingsRepository) {

    operator fun invoke(settings: Settings) {
        repository.updateSettings(settings)
    }
}
