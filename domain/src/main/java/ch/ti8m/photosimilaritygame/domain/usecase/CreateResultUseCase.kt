package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.CreateResultRequest
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.repository.LeaderboardRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource

class CreateResultUseCase(
    private val repository: LeaderboardRepository,
    private val getSimilarityScoreUseCase: GetSimilarityScoreUseCase,
) {
    suspend operator fun invoke(request: CreateResultRequest): Resource<Result> {
        val similarityScore =
            getSimilarityScoreUseCase(request.randomPhotoUrl, request.cameraPhotoUri)
        return if (similarityScore is Resource.Success) {
            repository.storeResult(
                Result(
                    userName = request.userName,
                    similarityScore = similarityScore.data.score,
                    randomPhotoUrl = request.randomPhotoUrl,
                    cameraPhotoUri = request.cameraPhotoUri,
                    randomPhotoLabels = similarityScore.data.randomPhotoLabels,
                    cameraPhotoLabels = similarityScore.data.cameraPhotoLabels,
                ))
        } else
            Resource.ServerError()
    }
}
