package ch.wenksi.photosimilaritygame.domain.model

import android.net.Uri

data class Result(
    val userName: String,
    val similarityScore: Int,
    val randomPhotoUrl: String,
    val cameraPhotoUri: Uri,
    val rank: Int = 0,
    val randomPhotoLabels: List<String>,
    val cameraPhotoLabels: List<String>,
)
