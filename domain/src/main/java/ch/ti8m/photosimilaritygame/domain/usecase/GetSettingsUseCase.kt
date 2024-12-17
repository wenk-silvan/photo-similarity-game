package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.Settings
import ch.wenksi.photosimilaritygame.domain.repository.SettingsRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource

class GetSettingsUseCase(private val repository: SettingsRepository) {

    operator fun invoke(): Resource<Settings> {
        return repository.getSettings()
    }
}
