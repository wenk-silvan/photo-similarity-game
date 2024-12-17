package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.repository.PhotoRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetRandomPhotoUrlUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    // region constants
    private val photoUrl = "photoUrl"
    private val expectedUrlResource = Resource.Success(photoUrl)
    // endregion constants

    // region helper fields
    private val photoRepository = mockk<PhotoRepository>()
    // endregion helper fields

    lateinit var SUT: GetRandomPhotoUrlUseCase

    @Before
    fun setUp() {
        setupSuccess()
        SUT = GetRandomPhotoUrlUseCase(photoRepository)
    }

    @Test
    fun useCaseCallsRepositoryOnce() = runTest {
        SUT()

        coVerify(exactly = 1) { photoRepository.getRandomPhotoUrl() }
        confirmVerified(photoRepository)
    }

    @Test
    fun useCaseReturnsRandomPhotoUrl() = runTest {
        val urlResource = SUT()

        assertThat(urlResource).isEqualTo(expectedUrlResource)
    }

    private fun setupSuccess() {
        coEvery { photoRepository.getRandomPhotoUrl() } returns expectedUrlResource
    }
}
