package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.Credentials
import ch.wenksi.photosimilaritygame.domain.model.User
import ch.wenksi.photosimilaritygame.domain.repository.UserRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler
import timber.log.Timber

class LoginUserUseCase(
    private val userRepository: UserRepository,
    private val resourceHandler: ResourceHandler,
) {
    suspend operator fun invoke(credentials: Credentials): Resource<User> {
        return try {
            if (userRepository.hasOngoingSession())
                throw IllegalStateException("Can't log in user '${credentials.userName}' when another session already exists.")

            return if (userRepository.credentialsAreCorrect(credentials)) {
                val user = userRepository.login(credentials.userName)
                Resource.Success(user)
            } else {
                Resource.InvalidCredentials()
            }
        } catch (e: Exception) {
            Timber.e("Error while login session: $e")
            resourceHandler.handleException(e)
        }
    }
}
