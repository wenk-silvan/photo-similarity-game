package ch.wenksi.photosimilaritygame.datasource.randomphoto.model

import ch.wenksi.photosimilaritygame.datasource.randomphoto.Photo
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashPhoto(
    override val urls: Urls = Urls(),
) : Photo

@Serializable
data class Urls(
    val small: String = "",
)
