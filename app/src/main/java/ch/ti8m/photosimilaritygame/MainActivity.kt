package ch.wenksi.photosimilaritygame

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import ch.wenksi.photosimilaritygame.domain.logic.ConnectivityObserver
import ch.wenksi.photosimilaritygame.ui.theme.PhotoSimilarityGameTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.android.ext.android.inject

class MainActivity : FragmentActivity() {

    private val connectivityObserver: ConnectivityObserver by inject()
    private val snackbarHostState = SnackbarHostState()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()
            val snackbarHostStateRemembered = remember { snackbarHostState }

            ConnectivityHandler(snackbarHostStateRemembered)
            SystemBarsHandler()

            PhotoSimilarityGameTheme {
                Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostStateRemembered) }) {
                    Surface(modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = it),
                        color = MaterialTheme.colorScheme.background) {
                        NavigationContainer(
                            navController = navController,
                            snackbarHostState = snackbarHostStateRemembered,
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ConnectivityHandler(snackbarHostState: SnackbarHostState) {
        val status by connectivityObserver.observe()
            .collectAsState(initial = ConnectivityObserver.Status.Unavailable)

        if (status == ConnectivityObserver.Status.Unavailable) LaunchedEffect(status) {
            snackbarHostState.showSnackbar(
                message = getString(R.string.connectivity_offline),
                duration = SnackbarDuration.Short,
            )
        }
    }

    @Composable
    fun SystemBarsHandler() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()

        LaunchedEffect(systemUiController, useDarkIcons) {
            systemUiController.setSystemBarsColor(color = Color.Transparent,
                darkIcons = useDarkIcons)
        }
    }
}
