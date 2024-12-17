package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.repository.LeaderboardRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource

class GetResultUseCase(private val repository: LeaderboardRepository) {
    suspend operator fun invoke(index: Int): Resource<Result> = repository.getResult(index)
}
