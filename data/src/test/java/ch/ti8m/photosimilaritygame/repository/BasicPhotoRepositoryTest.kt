package ch.wenksi.photosimilaritygame.repository

import ch.wenksi.photosimilaritygame.datasource.randomphoto.PhotosRemote
import ch.wenksi.photosimilaritygame.domain.model.Resource
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BasicPhotoRepositoryTest {

    // region constants
    private val url = Resource.Success("url")

    // endregion constants

    // region helper fields
    private val photosRemote = mockk<PhotosRemote>()

    // endregion helper fields

    lateinit var SUT: BasicPhotoRepository

    @Before
    fun setUp() {
        setupSuccess()
        SUT = BasicPhotoRepository(photosRemote)
    }

    @Test
    fun getRandomPhotoUrlCallsRemote() = runTest {
        SUT.getRandomPhotoUrl()

        coVerify(exactly = 1) { photosRemote.getRandomPhotoUrl() }
        confirmVerified(photosRemote)
    }

    @Test
    fun getsRandomPhoto() = runTest {
        val urlResource = SUT.getRandomPhotoUrl()

        assertThat(urlResource).isInstanceOf(Resource.Success::class.java)
        assertThat(urlResource).isEqualTo(url)
    }

    @Test
    fun getRandomPhotoReturnsNetworkErrorWhenNetworkExceptionOccurs() = runTest {
        coEvery { photosRemote.getRandomPhotoUrl() } returns Resource.NetworkError()

        val urlResource = SUT.getRandomPhotoUrl()

        assertThat(urlResource).isInstanceOf(Resource.NetworkError::class.java)
    }

    @Test
    fun getRandomPhotoReturnsServerErrorWhenRemoteExceptionOccurs() = runTest {
        coEvery { photosRemote.getRandomPhotoUrl() } returns Resource.ServerError()
        val urlResource = SUT.getRandomPhotoUrl()
        assertThat(urlResource).isInstanceOf(Resource.ServerError::class.java)
    }

    private fun setupSuccess() {
        coEvery { photosRemote.getRandomPhotoUrl() } returns url
    }
}