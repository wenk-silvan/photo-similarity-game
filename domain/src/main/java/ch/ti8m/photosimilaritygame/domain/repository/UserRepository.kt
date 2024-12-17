package ch.wenksi.photosimilaritygame.domain.repository

import ch.wenksi.photosimilaritygame.domain.model.Credentials
import ch.wenksi.photosimilaritygame.domain.model.User

interface UserRepository {
    suspend fun credentialsAreCorrect(credentials: Credentials): Boolean
    fun getSession(): User
    suspend fun hasOngoingSession(): Boolean
    suspend fun login(userName: String): User
    fun logout()
    suspend fun register(credentials: Credentials): User
    suspend fun updateUserName(old: String, new: String)
    suspend fun userExists(userName: String): Boolean
}
