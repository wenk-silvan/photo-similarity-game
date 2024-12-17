package ch.wenksi.photosimilaritygame.repository

import ch.wenksi.photosimilaritygame.datasource.randomphoto.PhotosRemote
import ch.wenksi.photosimilaritygame.domain.repository.PhotoRepository
import ch.wenksi.photosimilaritygame.domain.model.Resource

class BasicPhotoRepository(private val remote: PhotosRemote) : PhotoRepository {

    override suspend fun getRandomPhotoUrl(): Resource<String> {
        return remote.getRandomPhotoUrl()
    }
}
