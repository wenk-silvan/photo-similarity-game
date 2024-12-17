package ch.wenksi.photosimilaritygame.mockdata

import ch.wenksi.photosimilaritygame.datasource.randomphoto.PhotosRemote
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler

class MockPhotosRemote(
    private val error: Exception? = null,
    private val resourceHandler: ResourceHandler = ResourceHandler()
) : PhotosRemote {

    override suspend fun getRandomPhotoUrl(): Resource<String> {
        if (error != null) return resourceHandler.handleException(error)
        return resourceHandler.handleSuccess("https://images.unsplash.com/photo-1661916124236-cf98146c0f27?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwzNjM4NTJ8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjM1NzcyMjg&ixlib=rb-1.2.1&q=80&w=400")
    }
}
