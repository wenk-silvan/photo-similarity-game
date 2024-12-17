package ch.wenksi.photosimilaritygame.domain.usecase

import android.net.Uri
import ch.wenksi.photosimilaritygame.domain.logic.ImageProcessor
import ch.wenksi.photosimilaritygame.domain.model.SimilarityScore
import ch.wenksi.photosimilaritygame.domain.model.Resource
import timber.log.Timber

class GetSimilarityScoreUseCase(private val imageProcessor: ImageProcessor) {
    suspend operator fun invoke(urlImage: String, uriImage: Uri): Resource<SimilarityScore> {
        return try {
            val randomImage = imageProcessor.toImage(urlImage)
            val randomLabels = imageProcessor.findLabels(randomImage)
            val cameraImage = imageProcessor.toImage(uriImage.path!!)
            val cameraLabels = imageProcessor.findLabels(cameraImage)
            Resource.Success(SimilarityScore(
                score = imageProcessor.calculateScore(randomLabels, cameraLabels),
                randomPhotoLabels = randomLabels.map { l -> l.text },
                cameraPhotoLabels = cameraLabels.map { l -> l.text },
            ))
        } catch (e: Exception) {
            Timber.e("Error while calculating similarity score: $e")
            Resource.ServerError()
        }
    }
}
