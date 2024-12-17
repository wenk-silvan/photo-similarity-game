package ch.wenksi.photosimilaritygame.ui.screens.takephoto

import android.content.Context
import android.content.res.Configuration
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Lens
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.ui.screens.shared.AllPreviews
import ch.wenksi.photosimilaritygame.ui.screens.shared.ThemedPreview
import ch.wenksi.photosimilaritygame.ui.theme.Sizes
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun TakePhotoScreen(
    uiState: TakePhotoViewModel.UiState,
    snackbarHostState: SnackbarHostState,
    onSwitchCamera: () -> Unit,
    onTakePhoto: () -> Unit,
) {
    CameraPermissionDialog(snackbarHostState)
    Box(
        modifier = Modifier.testTag(Destination.TakePhoto.route),
        contentAlignment = if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
            Alignment.BottomCenter
        else Alignment.CenterEnd,
    ) {
        CameraPreview(
            imageCapture = uiState.imageCapture,
            lensFacing = uiState.lensFacing,
        )
        BottomActionBar(
            onTakePhoto = onTakePhoto,
            onSwitchCamera = onSwitchCamera,
        )
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    imageCapture: ImageCapture,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val preview = androidx.camera.core.Preview.Builder().build()
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(preview) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }
    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        factory = { previewView },
    )
}

@Composable
private fun BottomActionBarPortrait(
    modifier: Modifier = Modifier,
    onTakePhoto: () -> Unit,
    onSwitchCamera: () -> Unit,
) {
    Row(
        modifier = modifier.padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        Column(
            modifier = Modifier.weight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TakePhotoButton(onTakePhoto)
        }
        Column(
            modifier = Modifier.weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SwitchCameraButton(onSwitchCamera)
        }
    }
}

@Composable
fun BottomActionBar(
    modifier: Modifier = Modifier,
    onTakePhoto: () -> Unit,
    onSwitchCamera: () -> Unit,
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
        BottomActionBarPortrait(modifier, onTakePhoto, onSwitchCamera)
    else BottomActionBarLandscape(modifier, onTakePhoto, onSwitchCamera)
}

@Composable
private fun BottomActionBarLandscape(
    modifier: Modifier = Modifier,
    onTakePhoto: () -> Unit,
    onSwitchCamera: () -> Unit,
) {
    Column(
        modifier = modifier.padding(end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.weight(0.3f),
            verticalArrangement = Arrangement.Center,
        ) {
            SwitchCameraButton(onSwitchCamera)
        }
        Column(
            modifier = Modifier.weight(0.4f),
            verticalArrangement = Arrangement.Center,
        ) {
            TakePhotoButton(onTakePhoto)
        }
        Spacer(modifier = Modifier.weight(0.3f))
    }
}

@Composable
fun TakePhotoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.Lens,
            contentDescription = stringResource(R.string.camera_screen__button_content_description),
            tint = Color.White,
            modifier = Modifier
                .size(Sizes.s60)
                .padding(1.dp)
                .border(1.dp, Color.White, CircleShape),
        )
    }
}

@Composable
fun SwitchCameraButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.Cameraswitch,
            contentDescription = stringResource(R.string.camera_screen__button_content_description),
            tint = Color.White,
            modifier = Modifier.size(Sizes.s30),
        )
    }
}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        val cameraProvider = ProcessCameraProvider.getInstance(this)
        cameraProvider.addListener(
            { continuation.run { resume(cameraProvider.get()) } },
            ContextCompat.getMainExecutor(this)
        )
    }

@AllPreviews
@Composable
private fun Preview() {
    ThemedPreview {
        TakePhotoScreen(
            uiState = TakePhotoViewModel.UiState(),
            snackbarHostState = SnackbarHostState(),
            onTakePhoto = {},
            onSwitchCamera = {},
        )
    }
}
