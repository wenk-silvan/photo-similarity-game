package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.repository.UserRepository

class UpdateUserNameUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(old: String, new: String) {
        repository.updateUserName(old, new)
    }
}
