package ch.wenksi.photosimilaritygame.domain.model

import android.net.Uri

data class CreateResultRequest(
    val userName: String,
    val randomPhotoUrl: String,
    val cameraPhotoUri: Uri,
)
