package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.repository.PhotoRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource

class GetRandomPhotoUrlUseCase(private val repository: PhotoRepository) {
    suspend operator fun invoke(): Resource<String> = repository.getRandomPhotoUrl()
}
