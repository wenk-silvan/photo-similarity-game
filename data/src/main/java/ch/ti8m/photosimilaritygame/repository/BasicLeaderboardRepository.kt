package ch.wenksi.photosimilaritygame.repository

import android.net.Uri
import ch.wenksi.photosimilaritygame.datasource.localdb.leaderboard.ResultDao
import ch.wenksi.photosimilaritygame.datasource.localdb.leaderboard.ResultEntity
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.repository.LeaderboardRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler

private const val LABELS_SEPARATOR = ","

class BasicLeaderboardRepository(
    private val resultDao: ResultDao,
    private val resourceHandler: ResourceHandler,
) : LeaderboardRepository {

    override suspend fun storeResult(result: Result): Resource<Result> {
        return try {
            val entityId = resultDao.insert(
                ResultEntity(
                    userName = result.userName,
                    similarityScore = result.similarityScore,
                    randomPhotoUrl = result.randomPhotoUrl,
                    cameraPhotoUriPath = result.cameraPhotoUri.path.toString(),
                    randomPhotoLabels = result.randomPhotoLabels.joinToString(LABELS_SEPARATOR),
                    cameraPhotoLabels = result.cameraPhotoLabels.joinToString(LABELS_SEPARATOR),
                )
            )
            val resultWithRank = result.copy(rank = getRank(entityId))
            resourceHandler.handleSuccess(resultWithRank)
        } catch (e: Exception) {
            resourceHandler.handleException(e)
        }
    }

    override suspend fun getResults(): Resource<List<Result>> {
        return try {
            val results = getResultsFromDb()
            resourceHandler.handleSuccess(results)
        } catch (e: Exception) {
            resourceHandler.handleException(e)
        }
    }

    override suspend fun getResult(index: Int): Resource<Result> {
        return try {
            val result = getResultsFromDb()[index]
            resourceHandler.handleSuccess(result)
        } catch (e: Exception) {
            resourceHandler.handleException(e)
        }
    }

    override suspend fun deleteResults(): Resource<Boolean> {
        return try {
            resultDao.delete()
            resourceHandler.handleSuccess(true)
        } catch (e: Exception) {
            resourceHandler.handleException(e)
        }
    }

    private suspend fun getResultsFromDb(): List<Result> {
        return resultDao
            .getAll()
            .sortedWith(compareByDescending { it.similarityScore })
            .mapIndexed { index, entity ->
                Result(
                    userName = entity.userName,
                    similarityScore = entity.similarityScore,
                    randomPhotoUrl = entity.randomPhotoUrl,
                    cameraPhotoUri = Uri.parse(entity.cameraPhotoUriPath),
                    rank = index + 1,
                    randomPhotoLabels = entity.randomPhotoLabels.split(LABELS_SEPARATOR),
                    cameraPhotoLabels = entity.cameraPhotoLabels.split(LABELS_SEPARATOR),
                )
            }
    }

    private suspend fun getRank(id: Long): Int {
        return (resultDao
            .getAll()
            .sortedWith(compareByDescending { it.similarityScore })
            .indexOfFirst { r -> r.id == id }) + 1
    }
}
