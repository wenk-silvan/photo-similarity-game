package ch.wenksi.photosimilaritygame.repository

import ch.wenksi.photosimilaritygame.datasource.localdb.users.UserDao
import ch.wenksi.photosimilaritygame.datasource.localdb.users.UserEntity
import ch.wenksi.photosimilaritygame.datasource.sharedpreferences.UserSharedPreferences
import ch.wenksi.photosimilaritygame.domain.model.Credentials
import ch.wenksi.photosimilaritygame.domain.model.User
import ch.wenksi.photosimilaritygame.domain.repository.UserRepository

class BasicUserRepository(
    private val userSharedPrefs: UserSharedPreferences,
    private val userDao: UserDao,
) : UserRepository {

    override suspend fun credentialsAreCorrect(credentials: Credentials): Boolean {
        val existingUser = userDao.get(credentials.userName)
        return existingUser?.password == credentials.password
    }

    override fun getSession(): User = userSharedPrefs.read()

    override suspend fun hasOngoingSession(): Boolean = userSharedPrefs.exists()

    override suspend fun login(userName: String): User {
        val user = User(userName)
        userSharedPrefs.write(user)
        return user
    }

    override fun logout() {
        userSharedPrefs.clear()
    }

    override suspend fun register(credentials: Credentials): User {
        userDao.insert(
            UserEntity(
                userName = credentials.userName,
                password = credentials.password,
            )
        )
        val user = User(userName = credentials.userName)
        userSharedPrefs.write(user)
        return user
    }

    override suspend fun updateUserName(old: String, new: String) {
        userSharedPrefs.clear()
        userSharedPrefs.write(User(new))
        userDao.updateUserName(old, new)
    }

    override suspend fun userExists(userName: String): Boolean = userDao.get(userName) != null
}
