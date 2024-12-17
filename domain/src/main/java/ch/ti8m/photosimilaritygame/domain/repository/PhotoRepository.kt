package ch.wenksi.photosimilaritygame.domain.repository

import ch.wenksi.photosimilaritygame.domain.model.Resource

interface PhotoRepository {
    suspend fun getRandomPhotoUrl(): Resource<String>
}
