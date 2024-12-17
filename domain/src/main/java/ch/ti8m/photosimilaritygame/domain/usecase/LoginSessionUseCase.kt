package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.User
import ch.wenksi.photosimilaritygame.domain.repository.UserRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler
import timber.log.Timber

class LoginSessionUseCase(
    private val userRepository: UserRepository,
    private val resourceHandler: ResourceHandler,
) {
    suspend operator fun invoke(): Resource<User> {
        return try {
            if (userRepository.hasOngoingSession()) {
                val user = userRepository.getSession()
                resourceHandler.handleSuccess(user)
            } else {
                Resource.NoSession()
            }
        } catch (e: Exception) {
            Timber.e("Error while login session: $e")
            resourceHandler.handleException(e)
        }
    }
}
