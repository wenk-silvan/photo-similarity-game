package ch.wenksi.photosimilaritygame.domain.logic

import android.content.Context
import ch.wenksi.photosimilaritygame.domain.logic.ImageProcessor
import com.google.common.truth.Truth.assertThat
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class ImageProcessorTest {

    // region constants
    private val context = mockk<Context>()
    private val imageLabelerOptions = mockk<ImageLabelerOptions>()
    private val imageLabeler = mockk<ImageLabeler>()
    // endregion constants

    lateinit var SUT: ImageProcessor

    @Before
    fun setUp() {
        SUT = ImageProcessor(
            context = context,
            options = imageLabelerOptions,
            labeler = imageLabeler
        )
    }

    @Test
    fun calculateScore_shouldReturnScoreWhenBothImagesHaveLabelsAndMatch() {
        val labelsA = listOf(
            ImageLabel("Airplane", 0.8f, 0),
            ImageLabel("Sky", 0.6f, 0),
            ImageLabel("Vehicle", 0.9f, 0),
        )
        val labelsB = listOf(
            ImageLabel("Clouds", 0.7f, 0),
            ImageLabel("Sky", 0.9f, 0),
        )

        val score = SUT.calculateScore(labelsA, labelsB)

        assertThat(score).isEqualTo(33)
    }

    @Test
    fun calculateScore_shouldReturnScoreWhenBothImagesHaveLabelsNoMatch() {
        val labelsA = listOf(
            ImageLabel("Airplane", 0.8f, 0),
            ImageLabel("Sky", 0.6f, 0),
            ImageLabel("Vehicle", 0.9f, 0),
        )
        val labelsB = listOf(
            ImageLabel("Clouds", 0.7f, 0),
        )

        val score = SUT.calculateScore(labelsA, labelsB)

        assertThat(score).isEqualTo(0)
    }

    @Test
    fun calculateScore_shouldReturnScoreWhenImageAHasLabels() {
        val labelsA = listOf(
            ImageLabel("Airplane", 0.8f, 0),
            ImageLabel("Sky", 0.6f, 0),
            ImageLabel("Vehicle", 0.9f, 0),
        )

        val score = SUT.calculateScore(labelsA, emptyList())

        assertThat(score).isEqualTo(0)
    }

    @Test
    fun calculateScore_shouldReturnScoreWhenImageBHasLabels() {
        val labelsB = listOf(
            ImageLabel("Clouds", 0.7f, 0),
        )

        val score = SUT.calculateScore(emptyList(), labelsB)

        assertThat(score).isEqualTo(0)
    }

    @Test
    fun calculateScore_shouldReturnScoreWhenNoLabels() {
        val score = SUT.calculateScore(emptyList(), emptyList())

        assertThat(score).isEqualTo(0)
    }
}