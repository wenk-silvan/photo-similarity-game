package ch.wenksi.photosimilaritygame.domain.model

data class SimilarityScore(
    val score: Int,
    val randomPhotoLabels: List<String>,
    val cameraPhotoLabels: List<String>,
)
