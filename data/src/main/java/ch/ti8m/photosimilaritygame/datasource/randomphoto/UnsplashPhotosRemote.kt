package ch.wenksi.photosimilaritygame.datasource.randomphoto

import ch.wenksi.photosimilaritygame.datasource.randomphoto.model.UnsplashPhoto
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.ResourceHandler
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class UnsplashPhotosRemote(
    private val client: HttpClient,
    private val resourceHandler: ResourceHandler,
) : PhotosRemote {

    companion object {
        private const val BASE_URL = "https://api.unsplash.com"
        private const val ACCESS_KEY = "qBMu3sJivTS7MWzj1JVPY5j6vdCBidHwvjQk-ViNxkw"
        const val RANDOM_PHOTO = "$BASE_URL/photos/random"
        const val HEADER_AUTHORIZATION = "Client-ID $ACCESS_KEY"
    }

    override suspend fun getRandomPhotoUrl(): Resource<String> {
        return try {
            val photo: UnsplashPhoto = client.get {
                url(RANDOM_PHOTO)
                headers {
                    append(HttpHeaders.Authorization, HEADER_AUTHORIZATION)
                }
            }.body()
            resourceHandler.handleSuccess(photo.urls.small)
        } catch (e: Exception) {
            resourceHandler.handleException(e)
        }
    }
}
