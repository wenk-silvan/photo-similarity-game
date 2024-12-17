package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.Credentials
import ch.wenksi.photosimilaritygame.domain.model.User
import ch.wenksi.photosimilaritygame.domain.repository.UserRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler

class RegisterUserUseCase(
    private val userRepository: UserRepository,
    private val resourceHandler: ResourceHandler,
) {
    suspend operator fun invoke(credentials: Credentials): Resource<User> {
        return if (userRepository.userExists(credentials.userName)) {
            Resource.UserExists()
        } else {
            val user = userRepository.register(credentials)
            resourceHandler.handleSuccess(user)
        }
    }
}
