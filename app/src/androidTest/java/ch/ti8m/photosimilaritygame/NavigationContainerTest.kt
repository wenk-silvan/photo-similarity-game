package ch.wenksi.photosimilaritygame

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import ch.wenksi.photosimilaritygame.datasource.randomphoto.PhotosRemote
import ch.wenksi.photosimilaritygame.di.datasourceModule
import ch.wenksi.photosimilaritygame.di.repositoryModule
import ch.wenksi.photosimilaritygame.di.useCaseModule
import ch.wenksi.photosimilaritygame.di.viewModelModule
import ch.wenksi.photosimilaritygame.mockdata.MockPhotosRemote
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

class NavigationContainerTest {
    private var mockPhotosRemote = MockPhotosRemote()
    private val testModules = module {
        single<PhotosRemote> { mockPhotosRemote }
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val koinTestRule = KoinTestRule(
        modules = listOf(
            datasourceModule,
            repositoryModule,
            useCaseModule,
            viewModelModule,
            testModules
        )
    )

    private lateinit var navController: TestNavHostController

    private fun initializeApp(startDestination: String = Destination.Photo.route) {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavigationContainer(
                navController = navController,
                startDestination = startDestination,
            )
        }
    }

    @Test
    fun randomPhotoScreen_isStartDestination() {
        initializeApp()

        composeTestRule
            .onNodeWithTag(Destination.Photo.route)
            .assertIsDisplayed()
    }

    @Test
    fun randomPhotoScreen_navigate_takePhotoScreen() {
        initializeApp()

        composeTestRule
            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.photo_with_title__take_photo_label))
            .performClick()

        composeTestRule
            .onNodeWithTag(Destination.TakePhoto.route)
            .assertIsDisplayed()
    }

    // randomPhotoScreen_navigate_leaderboardScreen

    // takePhotoScreen_navigate_cameraPhotoScreen

    // cameraPhotoScreen_navigate_randomPhotoScreen

    // cameraPhotoScreen_navigate_takePhotoScreen

    @Test
    fun resultScreen_navigate_randomPhotoScreen_tryAgain() {
        initializeApp(startDestination = Destination.Result.route)

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.result_screen__button_try_again))
            .performClick()

        composeTestRule
            .onNodeWithTag(Destination.Photo.route)
            .assertIsDisplayed()
    }

    @Test
    fun resultScreen_navigate_randomPhotoScreen_newGame() {
        initializeApp(startDestination = Destination.Result.route)

        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.result_screen__button_new_game))
            .performClick()

        composeTestRule
            .onNodeWithTag(Destination.Photo.route)
            .assertIsDisplayed()
    }

    // resultScreen_navigate_leaderboardScreen

    // leaderboardScreen_navigate_leaderboardDetailsScreen

    // leaderboardDetailsScreen_navigateBack
}