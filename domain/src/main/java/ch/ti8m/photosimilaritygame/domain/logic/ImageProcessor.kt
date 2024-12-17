package ch.wenksi.photosimilaritygame.domain.logic

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabelerOptionsBase
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ImageProcessor(
    private val context: Context,
    private val confidenceThreshold: Float = 0.7f,
    private val options: ImageLabelerOptionsBase = ImageLabelerOptions
        .Builder()
        .setConfidenceThreshold(confidenceThreshold)
        .build(),
    private val labeler: ImageLabeler = ImageLabeling.getClient(options),
) {
    suspend fun findLabels(image: InputImage): List<ImageLabel> {
        return suspendCoroutine { continuation ->
            labeler.process(image)
                .addOnSuccessListener { continuation.resume(it) }
                .addOnFailureListener { throw(it) }
        }
    }

    fun calculateScore(labelsA: List<ImageLabel>, labelsB: List<ImageLabel>): Int {
        return if (labelsA.isEmpty() || labelsB.isEmpty()) 0
        else {
            val commonLabels = findCommonLabels(labelsA, labelsB)
            ((commonLabels.size.toFloat() / labelsA.size.toFloat()) * 100).toInt()
        }
    }

    suspend fun toImage(imagePath: String): InputImage {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imagePath)
            .allowHardware(true)
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap
        return InputImage.fromBitmap(bitmap, 0)
    }

    private fun findCommonLabels(
        labelsA: List<ImageLabel>,
        labelsB: List<ImageLabel>,
    ): Set<ImageLabel> {
        return labelsA
            .filter { a -> labelsB.any { b -> b.text == a.text } }
            .toSet()
    }
}
