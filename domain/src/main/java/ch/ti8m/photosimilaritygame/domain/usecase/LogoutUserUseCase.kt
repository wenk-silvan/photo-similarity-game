package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.repository.UserRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler
import timber.log.Timber

class LogoutUserUseCase(
    private val userRepository: UserRepository,
    private val resourceHandler: ResourceHandler,
) {
    operator fun invoke(): Resource<Any> {
        return try {
            return Resource.Success(userRepository.logout())
        } catch (e: Exception) {
            Timber.e("Error while logout: $e")
            resourceHandler.handleException(e)
        }
    }
}
