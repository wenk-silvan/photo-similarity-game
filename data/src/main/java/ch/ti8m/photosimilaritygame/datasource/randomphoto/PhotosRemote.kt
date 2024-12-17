package ch.wenksi.photosimilaritygame.datasource.randomphoto

import ch.wenksi.photosimilaritygame.domain.model.Resource

interface PhotosRemote {
    suspend fun getRandomPhotoUrl(): Resource<String>
}
