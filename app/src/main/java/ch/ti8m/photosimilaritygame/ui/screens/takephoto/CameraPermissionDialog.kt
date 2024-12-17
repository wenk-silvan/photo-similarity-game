package ch.wenksi.photosimilaritygame.ui.screens.takephoto

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import ch.wenksi.photosimilaritygame.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun CameraPermissionDialog(snackbarHostState: SnackbarHostState) {
    (LocalContext.current as FragmentActivity).apply {
        val scope = rememberCoroutineScope()
        val launcher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Timber.i("app", "Permission granted")
                    permissionGrantedSnackbar(this, scope, snackbarHostState)
                } else {
                    Timber.i("app", "Permission denied")
                    permissionDeniedSnackbar(this, scope, snackbarHostState) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:$packageName")),
                        )
                    }
                }
            }

        SideEffect {
            when {
                ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                    Timber.i("app", "Permission previously granted")
                }
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA) -> {
                    Timber.i("app", "Show camera permission dialog")
                    launcher.launch(Manifest.permission.CAMERA)
                }
                else -> launcher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

private fun permissionDeniedSnackbar(
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    action: () -> Unit,
) = scope.launch {
    snackbarHostState.showSnackbar(
        message = context.getString(R.string.camera_request_denied),
        actionLabel = context.getString(R.string.camera_request_denied_action),
        withDismissAction = true,
        duration = SnackbarDuration.Short,
    ).let { result ->
        if (result == SnackbarResult.ActionPerformed) {
            action()
        }
    }
}

private fun permissionGrantedSnackbar(
    context: Context,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
) = scope.launch {
    snackbarHostState.showSnackbar(
        message = context.getString(R.string.camera_request_granted),
        withDismissAction = true,
        duration = SnackbarDuration.Short,
    )
}