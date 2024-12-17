package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.repository.LeaderboardRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource

class GetResultsUseCase(private val repository: LeaderboardRepository) {
    suspend operator fun invoke(): Resource<List<Result>> = repository.getResults()
}
