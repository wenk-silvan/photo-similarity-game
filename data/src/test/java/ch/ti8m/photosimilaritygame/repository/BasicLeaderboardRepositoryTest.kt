package ch.wenksi.photosimilaritygame.repository

import android.net.Uri
import ch.wenksi.photosimilaritygame.datasource.localdb.leaderboard.ResultDao
import ch.wenksi.photosimilaritygame.datasource.localdb.leaderboard.ResultEntity
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@Suppress("OPT_IN_IS_NOT_ENABLED")
@OptIn(ExperimentalCoroutinesApi::class)
class BasicLeaderboardRepositoryTest {
    // region helper fields
    private val mockUri = mockk<Uri>()
    private val resultEntity = ResultEntity(
        userName = "userName",
        similarityScore = 10,
        randomPhotoUrl = "randomPhotoUrl",
        cameraPhotoUriPath = "cameraPhotoUriPath",
        randomPhotoLabels = "randomPhotoLabels",
        cameraPhotoLabels = "cameraPhotoLabels",
    )
    private val resultEntities = listOf(resultEntity)
    private val results = listOf(
        Result(
            userName = resultEntity.userName,
            similarityScore = resultEntity.similarityScore,
            randomPhotoUrl = resultEntity.randomPhotoUrl,
            cameraPhotoUri = mockUri,
            rank = 1,
            randomPhotoLabels = listOf(resultEntity.randomPhotoLabels),
            cameraPhotoLabels = listOf(resultEntity.cameraPhotoLabels),
        ),
        Result(
            userName = resultEntity.userName,
            similarityScore = resultEntity.similarityScore,
            randomPhotoUrl = resultEntity.randomPhotoUrl,
            cameraPhotoUri = mockUri,
            rank = 2,
            randomPhotoLabels = listOf(resultEntity.randomPhotoLabels),
            cameraPhotoLabels = listOf(resultEntity.cameraPhotoLabels),
        ),
    )
    private val resultDao = mockk<ResultDao>()
    private val resourceHandler = ResourceHandler()
    // endregion helper fields

    lateinit var SUT: BasicLeaderboardRepository

    @Before
    fun setUp() {
        setupSuccess()
        SUT = BasicLeaderboardRepository(resultDao, resourceHandler)
    }

    @Test
    fun storeResultReturnsResult() = runTest {
        coEvery { resultDao.insert(resultEntity) } returns 0

        val resource = SUT.storeResult(results.first())

        assertThat(resource).isInstanceOf(Resource.Success::class.java)
        assertThat((resource as Resource.Success).data).isEqualTo(results.first())
    }

    @Test
    fun storeResultReturnsServerErrorWhenExceptionOccurs() = runTest {
        coEvery { resultDao.insert(resultEntity) } throws Exception()

        val resource = SUT.storeResult(results.first())

        assertThat(resource).isInstanceOf(Resource.ServerError::class.java)
    }

    @Test
    fun getResultsReturnsServerErrorWhenExceptionOccurs() = runTest {
        coEvery { resultDao.getAll() } throws Exception()

        val resource = SUT.getResults()

        assertThat(resource).isInstanceOf(Resource.ServerError::class.java)
    }

    @Test
    fun deleteResultsReturnsServerErrorWhenExceptionOccurs() = runTest {
        coEvery { resultDao.delete() } throws Exception()

        val resource = SUT.deleteResults()

        assertThat(resource).isInstanceOf(Resource.ServerError::class.java)
    }

    private fun setupSuccess() {
        coEvery { resultDao.getAll() } returns resultEntities
        coEvery { mockUri.path } returns resultEntity.cameraPhotoUriPath
    }
}
