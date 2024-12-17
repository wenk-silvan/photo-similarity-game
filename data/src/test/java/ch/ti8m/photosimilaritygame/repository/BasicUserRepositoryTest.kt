package ch.wenksi.photosimilaritygame.repository

import ch.wenksi.photosimilaritygame.datasource.localdb.users.UserDao
import ch.wenksi.photosimilaritygame.datasource.localdb.users.UserEntity
import ch.wenksi.photosimilaritygame.datasource.sharedpreferences.UserSharedPreferences
import ch.wenksi.photosimilaritygame.domain.model.Credentials
import ch.wenksi.photosimilaritygame.domain.model.User
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BasicUserRepositoryTest {
    // region helper fields
    private val sessionManager = mockk<UserSharedPreferences>()
    private val credentials = Credentials("userName", "password")
    private val user = User(credentials.userName)
    private val userDao = mockk<UserDao>()
    private val userEntity = UserEntity(
        userName = credentials.userName,
        password = credentials.password,
    )
    // endregion helper fields

    lateinit var SUT: BasicUserRepository

    @Before
    fun setUp() {
        setupSuccess()
        SUT = BasicUserRepository(sessionManager, userDao)
    }

    @Test
    fun loginUserWritesToSessionManager() = runTest {
        every { sessionManager.write(user) } returns Unit

        val actual = SUT.login(credentials.userName)

        assertThat(actual).isEqualTo(user)
        verify(exactly = 1) { sessionManager.write(user) }
        confirmVerified(sessionManager)
    }

    @Test
    fun logoutCallsSessionManager() {
        SUT.logout()

        verify(exactly = 1) { sessionManager.clear() }
        confirmVerified(sessionManager)
    }

    @Test
    fun registerUserReturnsUser() = runTest {
        coEvery { userDao.get(credentials.userName) } returns null

        val actual = SUT.register(credentials)

        assertThat(actual).isEqualTo(user)
    }

    private fun setupSuccess() {
        every { sessionManager.exists() } returns false
        every { sessionManager.write(user) } returns Unit
        every { sessionManager.clear() } returns Unit
        coEvery { userDao.get(credentials.userName) } returns userEntity
        coEvery { userDao.insert(userEntity) } returns 0
    }
}
