package ch.wenksi.photosimilaritygame

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ch.wenksi.photosimilaritygame.domain.logic.ConnectivityObserver
import ch.wenksi.photosimilaritygame.ui.screens.cameraphoto.CameraPhotoScreen
import ch.wenksi.photosimilaritygame.ui.screens.leaderboard.LeaderboardScreen
import ch.wenksi.photosimilaritygame.ui.screens.leaderboard.LeaderboardViewModel
import ch.wenksi.photosimilaritygame.ui.screens.leaderboard_details.LeaderboardDetailsScreen
import ch.wenksi.photosimilaritygame.ui.screens.leaderboard_details.LeaderboardDetailsViewModel
import ch.wenksi.photosimilaritygame.ui.screens.login.LoginViewModel
import ch.wenksi.photosimilaritygame.ui.screens.photo.PhotoViewModel
import ch.wenksi.photosimilaritygame.ui.screens.photo.RandomPhotoScreen
import ch.wenksi.photosimilaritygame.ui.screens.register.RegisterScreen
import ch.wenksi.photosimilaritygame.ui.screens.register.RegisterViewModel
import ch.wenksi.photosimilaritygame.ui.screens.result.ResultScreen
import ch.wenksi.photosimilaritygame.ui.screens.result.ResultViewModel
import ch.wenksi.photosimilaritygame.ui.screens.settings.SettingsScreen
import ch.wenksi.photosimilaritygame.ui.screens.settings.SettingsViewModel
import ch.wenksi.photosimilaritygame.ui.screens.takephoto.TakePhotoScreen
import ch.wenksi.photosimilaritygame.ui.screens.takephoto.TakePhotoViewModel
import org.koin.androidx.compose.getViewModel
import timber.log.Timber

@Composable
fun NavigationContainer(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    startDestination: String = Destination.Login.route,
) {
    val navigationViewModel: NavigationViewModel = getViewModel()
    val photoViewModel: PhotoViewModel = getViewModel()
    val resultViewModel: ResultViewModel = getViewModel()
    val settingsViewModel: SettingsViewModel = getViewModel()
    val onAuthenticationSuccess = { userName: String ->
        navigationViewModel.setLoggedInUserName(userName)
        navController.navToPhotoPopToLoginInclusive()
    }
    val context = LocalContext.current

    NavHost(modifier = modifier,
        navController = navController,
        startDestination = startDestination) {
        val onClickPlayAgain = {
            resultViewModel.resetUiState()
            photoViewModel.startGame(ConnectivityObserver.Status.Available)
            navController.navToPhotoPopToPhotoInclusive()
        }

        val onClickTryAgain = {
            resultViewModel.resetUiState()
            navController.navToPhotoPopToPhotoInclusive()
        }

        composable(route = Destination.Login.route) {
            val viewModel: LoginViewModel = getViewModel()

            LaunchedEffect(true) {
                viewModel.trySessionLogin { onAuthenticationSuccess(it) }
            }
            ErrorSnackbar(
                error = viewModel.uiState.error?.asString(),
                snackbarHostState = snackbarHostState,
            )
            LoginRoute(
                viewModel = viewModel,
                onAuthenticationSuccess = onAuthenticationSuccess,
                onClickRegisterInstead = navController::navToLoginPopToLoginInclusive,
            )
        }

        composable(route = Destination.Register.route) {
            val viewModel: RegisterViewModel = getViewModel()
            ErrorSnackbar(
                error = viewModel.uiState.error?.asString(),
                snackbarHostState = snackbarHostState,
                dismissAction = { viewModel.setError(null) },
            )
            RegisterScreen(
                uiState = viewModel.uiState,
                onClickLoginInstead = { navController.navToLoginPopToRegisterInclusive() },
                onClickRegister = { viewModel.register(onAuthenticationSuccess) },
                onSetPassword = { viewModel.setPassword(it) },
                onSetRepeatPassword = { viewModel.setRepeatPassword(it) },
                onSetUserName = { viewModel.setUserName(it) },
            )
        }

        composable(route = Destination.Photo.route) {
            val photoPicker =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
                    it?.let {
                        photoViewModel.setGalleryPhoto(it)
                        navigationViewModel.setGivenPhoto(it.toString())
                    }
                }
            RandomPhotoScreen(uiState = photoViewModel.uiState,
                userName = navigationViewModel.uiState.loggedInUserName!!,
                onClickTakePhoto = { url ->
                    navigationViewModel.setGivenPhoto(url)
                    navController.navToTakePhoto()
                },
                onClickSettings = { navController.navToSettings() },
                onClickLeaderboard = { navController.navToLeaderboard() },
                onClickPick = {
                    photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                })
        }

        composable(route = Destination.TakePhoto.route) {
            val viewModel: TakePhotoViewModel = getViewModel()
            TakePhotoScreen(
                uiState = viewModel.uiState,
                snackbarHostState = snackbarHostState,
                onSwitchCamera = { viewModel.switchCamera() },
                onTakePhoto = {
                    viewModel.takePhoto(
                        executor = context.mainExecutor,
                        onImageCaptured = { photoUri ->
                            navigationViewModel.setCameraPhoto(photoUri)
                            navController.navToCameraPhoto()
                        },
                        onError = {
                            Timber.e("app", "Error while taking picture: $it")
                        },
                        outputDirectory = context.filesDir,
                    )
                })
        }

        composable(route = Destination.CameraPhoto.route) {
            val uri = navigationViewModel.uiState.cameraPhotoUri!!
            val url = navigationViewModel.uiState.randomPhotoUrl!!
            val userName = navigationViewModel.uiState.loggedInUserName!!

            CameraPhotoScreen(
                cameraPhotoUrl = uri.path.toString(),
                randomPhotoUrl = url,
                onClickResult = {
                    resultViewModel.createResult(
                        randomPhotoUrl = url,
                        cameraPhotoUri = uri,
                        userName = userName,
                    )
                    navController.navToResultPopToPhoto()
                },
                onClickRetake = {
                    navController.navToTakePhoto()
                },
            )
        }

        composable(route = Destination.Result.route) {
            ResultScreen(
                uiState = resultViewModel.uiState,
                onClickLeaderboard = {
                    navController.navToLeaderboardPopToRandomPhoto()
                },
                onClickPlayAgain = onClickPlayAgain,
                onClickTryAgain = onClickTryAgain,
            )
        }

        composable(route = Destination.Leaderboard.route) {
            val viewModel: LeaderboardViewModel = getViewModel()
            LeaderboardScreen(uiState = viewModel.uiState,
                onClickPlayAgain = onClickPlayAgain,
                onClickTryAgain = onClickTryAgain,
                onClickResult = { navController.navToLeaderboardDetails(it) },
                onClickClear = { viewModel.clearLeaderboard() })
        }

        composable(
            route = Destination.LeaderboardDetails.routeWithArgs,
            arguments = Destination.LeaderboardDetails.arguments,
        ) { navBackStackEntry ->
            val resultIndex =
                navBackStackEntry.arguments?.getInt(Destination.LeaderboardDetails.resultIndexArgument)!!
            val viewModel: LeaderboardDetailsViewModel = getViewModel()
            LaunchedEffect(true) {
                viewModel.getResult(resultIndex)
            }
            LeaderboardDetailsScreen(
                uiState = viewModel.uiState,
                onClickBack = { navController.popBackStack() },
            )
        }

        composable(route = Destination.Settings.route) {
            val userName = navigationViewModel.uiState.loggedInUserName!!
            LaunchedEffect(true) {
                settingsViewModel.setEditUserName(userName)
                settingsViewModel.warmupChromeTabs()
            }
            ErrorSnackbar(
                error = settingsViewModel.uiState.snackbarError?.asString(),
                snackbarHostState = snackbarHostState,
                dismissAction = { settingsViewModel.resetSnackbarError() },
            )
            SettingsScreen(
                uiState = settingsViewModel.uiState,
                userName = userName,
                onClickBack = { navController.popBackStack() },
                onClickLogout = {
                    settingsViewModel.logout(onSuccess = { navController.navToLoginPopToRandomPhotoInclusive() })
                },
                onSaveUserName = {
                    settingsViewModel.saveUserName(
                        oldName = userName,
                        newName = it,
                        onSuccess = { navigationViewModel.setLoggedInUserName(it) },
                    )
                },
                onToggleGenerateLocally = {
                    settingsViewModel.setGenerateLocally(it)
                    photoViewModel.startGame(ConnectivityObserver.Status.Available)
                },
                onClickRules = { settingsViewModel.showRules(context) },
                onClickTermsConditions = { settingsViewModel.showTermsConditions(context) },
                onClickPrivacyPolicy = { settingsViewModel.showPrivacyPolicy(context) },
            )
        }
    }
}

@Composable
private fun ErrorSnackbar(
    error: String?,
    snackbarHostState: SnackbarHostState,
    duration: SnackbarDuration = SnackbarDuration.Short,
    dismissAction: () -> Unit = {},
) {
    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(
                message = it,
                withDismissAction = true,
                duration = duration,
            ).also {
                dismissAction()
            }
        }
    }
}
