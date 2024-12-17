package ch.wenksi.photosimilaritygame.domain.usecase

import android.net.Uri
import ch.wenksi.photosimilaritygame.domain.logic.ImageProcessor
import ch.wenksi.photosimilaritygame.domain.model.SimilarityScore
import ch.wenksi.photosimilaritygame.domain.MainCoroutineRule
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetSimilarityScoreUseCaseTest {
    private val urlImageA = "randomPhotoUrl"
    private val uriImageB = mockk<Uri>()
    private val imageA = mockk<InputImage>()
    private val imageB = mockk<InputImage>()
    private val label = "label"
    private val labelsA = listOf(ImageLabel(label, 0.9f, 1))
    private val labelsB = listOf(ImageLabel(label, 0.9f, 1))
    private val imageProcessor = mockk<ImageProcessor>()
    private val score = 100

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    lateinit var SUT: GetSimilarityScoreUseCase

    @Before
    fun setUp() {
        setupSuccess()
        SUT = GetSimilarityScoreUseCase(imageProcessor)
    }

    @Test
    fun useCaseReturnsSimilarityScore() = runTest {
        val expected = SimilarityScore(
            score = score,
            randomPhotoLabels = listOf(label),
            cameraPhotoLabels = listOf(label),
        )
        val actual = SUT(urlImageA, uriImageB)
        // TODO(mock Uri.getPath)
    }

    private fun setupSuccess() {
        coEvery { imageProcessor.toImage(urlImageA) } returns imageA
        coEvery { imageProcessor.findLabels(imageA) } returns labelsA
        coEvery { imageProcessor.toImage(uriImageB.path!!) } returns imageB
        coEvery { imageProcessor.findLabels(imageB) } returns labelsB
        coEvery { imageProcessor.calculateScore(labelsA, labelsB) } returns score
    }
}
