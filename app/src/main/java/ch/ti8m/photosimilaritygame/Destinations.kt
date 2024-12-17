package ch.wenksi.photosimilaritygame

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(val route: String) {

    object Photo : Destination("RandomPhotoRoute")

    object TakePhoto : Destination("TakePhotoRoute")

    object CameraPhoto : Destination("CameraPhotoRoute")

    object Result : Destination("ResultRoute")

    object Leaderboard : Destination("LeaderboardRoute")

    object LeaderboardDetails : Destination("LeaderboardDetailsRoute") {
        const val resultIndexArgument = "resultIndexArgument"
        val routeWithArgs = "${LeaderboardDetails.route}/{${resultIndexArgument}}"
        val arguments = listOf(navArgument(resultIndexArgument) { type = NavType.IntType })
    }

    object Login : Destination("LoginRoute")

    object Register : Destination("RegisterRoute")

    object Settings : Destination("SettingsRoute")

    object Error : Destination("ErrorScreenRoute")
}

