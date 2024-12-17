package ch.wenksi.photosimilaritygame.domain.repository

import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.model.Resource

interface LeaderboardRepository {
    suspend fun storeResult(result: Result): Resource<Result>
    suspend fun getResult(index: Int): Resource<Result>
    suspend fun getResults(): Resource<List<Result>>
    suspend fun deleteResults(): Resource<Boolean>
}
