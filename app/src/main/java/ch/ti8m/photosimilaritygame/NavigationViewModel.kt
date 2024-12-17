package ch.wenksi.photosimilaritygame

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ch.wenksi.photosimilaritygame.domain.model.Settings

class NavigationViewModel : ViewModel() {

    var uiState: UiState by mutableStateOf(UiState())
        private set

    fun setCameraPhoto(value: Uri) {
        uiState = uiState.copy(cameraPhotoUri = value)
    }

    fun setGivenPhoto(url: String) {
        uiState = uiState.copy(randomPhotoUrl = url)
    }

    fun setLoggedInUserName(value: String) {
        uiState = uiState.copy(loggedInUserName = value)
    }

    data class UiState(
        val loggedInUserName: String? = null,
        val randomPhotoUrl: String? = null,
        val cameraPhotoUri: Uri? = null,
        val settings: Settings? = null,
    )
}

fun NavController.navToCameraPhoto() = navigate(Destination.CameraPhoto.route)

fun NavController.navToLoginPopToLoginInclusive() =
    navigate(Destination.Register.route) {
        popUpTo(Destination.Login.route) {
            inclusive = true
        }
    }

fun NavController.navToLoginPopToRandomPhotoInclusive() =
    navigate(Destination.Login.route) {
        popUpTo(Destination.Photo.route) {
            inclusive = true
        }
    }

fun NavController.navToLoginPopToRegisterInclusive() =
    navigate(Destination.Login.route) {
        popUpTo(Destination.Register.route) {
            inclusive = true
        }
    }

fun NavController.navToPhotoPopToLoginInclusive() =
    navigate(route = Destination.Photo.route) {
        popUpTo(Destination.Login.route) {
            inclusive = true
        }
    }

fun NavController.navToPhotoPopToPhotoInclusive() =
    navigate(route = Destination.Photo.route) {
        popUpTo(Destination.Photo.route) {
            inclusive = true
        }
    }

fun NavController.navToResultPopToPhoto() =
    navigate(Destination.Result.route) {
        popUpTo(Destination.Photo.route)
    }

fun NavController.navToSettings() = navigate(Destination.Settings.route)

fun NavController.navToTakePhoto() = navigate(Destination.TakePhoto.route)

fun NavController.navToLeaderboard() = navigate(Destination.Leaderboard.route)

fun NavController.navToLeaderboardDetails(index: Int) =
    navigate("${Destination.LeaderboardDetails.route}/$index")

fun NavController.navToLeaderboardPopToRandomPhoto() =
    navigate(Destination.Leaderboard.route) {
        popUpTo(Destination.Photo.route)
    }
