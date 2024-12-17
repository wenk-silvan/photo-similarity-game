package ch.wenksi.photosimilaritygame.domain.usecase

import ch.wenksi.photosimilaritygame.domain.repository.LeaderboardRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource

class ClearLeaderboardUseCase(private val repository: LeaderboardRepository) {
    suspend operator fun invoke(): Resource<Boolean> = repository.deleteResults()
}
