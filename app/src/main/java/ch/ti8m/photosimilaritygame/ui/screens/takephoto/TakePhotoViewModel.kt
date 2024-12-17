package ch.wenksi.photosimilaritygame.ui.screens.takephoto

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ch.wenksi.photosimilaritygame.ui.UiText
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

class TakePhotoViewModel : ViewModel() {
    var uiState by mutableStateOf(UiState())
        private set

    fun switchCamera() {
        uiState = uiState.copy(
            lensFacing = if (uiState.lensFacing == CameraSelector.LENS_FACING_FRONT)
                CameraSelector.LENS_FACING_BACK
            else CameraSelector.LENS_FACING_FRONT
        )
    }

    fun takePhoto(
        fileNameFormat: String = "yyyy-MM-dd-HH-mm-ss-SSS",
        executor: Executor,
        outputDirectory: File,
        onImageCaptured: (Uri) -> Unit,
        onError: (ImageCaptureException) -> Unit,
    ) {
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(fileNameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )
        uiState.imageCapture.takePicture(
            ImageCapture
                .OutputFileOptions
                .Builder(photoFile)
                .setMetadata(getImageCaptureMetadata())
                .build(),
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(e: ImageCaptureException) {
                    Timber.e("app", "Take photo error:", e)
                    onError(e)
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    onImageCaptured(savedUri)
                }
            })
    }

    private fun getImageCaptureMetadata(): ImageCapture.Metadata {
        val isFrontCamera = uiState.lensFacing == CameraSelector.LENS_FACING_FRONT
        return ImageCapture.Metadata().apply {
            isReversedHorizontal = isFrontCamera
        }
    }

    data class UiState(
        val lensFacing: Int = CameraSelector.LENS_FACING_BACK,
        val imageCapture: ImageCapture = ImageCapture.Builder().build(),
        val error: UiText? = null,
    )
}

