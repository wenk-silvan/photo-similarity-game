package ch.wenksi.photosimilaritygame.ui.screens.photos

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.KoinTestRule
import ch.wenksi.photosimilaritygame.MainActivity
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.datasource.randomphoto.PhotosRemote
import ch.wenksi.photosimilaritygame.di.*
import ch.wenksi.photosimilaritygame.mockdata.MockPhotosRemote
import ch.wenksi.photosimilaritygame.ui.screens.photo.PhotoViewModel
import ch.wenksi.photosimilaritygame.ui.screens.photo.RandomPhotoScreen
import ch.wenksi.photosimilaritygame.ui.theme.PhotoSimilarityGameTheme
import ch.wenksi.photosimilaritygame.util.TestTags.PHOTO_WITH_TITLE_RANDOM
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module
import java.net.UnknownHostException
import java.security.InvalidKeyException

class RandomPhotoScreenTest {
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

    @Test
    fun photosScreen_isDisplayed() {
        initializeScreen()
        composeTestRule
            .onNodeWithTag(Destination.Photo.route)
            .assertIsDisplayed()
    }

    @Test
    fun photoWithTitle_randomPhoto_isDisplayed() {
        initializeScreen()
        composeTestRule.onNodeWithTag(PHOTO_WITH_TITLE_RANDOM).assertIsDisplayed()
    }

    @Test
    fun errorScreen_isVisible_networkError() {
        initializeScreenWithRemoteError(UnknownHostException())
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.error_screen__network_error))
            .assertExists()
    }

    @Test
    fun errorScreen_isVisible_serverError() {
        initializeScreenWithRemoteError(InvalidKeyException())
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.error_screen__server_error))
            .assertExists()
    }

    // region helper methods
    private fun initializeScreen() {
        composeTestRule.setContent {
            PhotoSimilarityGameTheme {
                RandomPhotoScreen(
                    userName = "userName",
                    uiState = PhotoViewModel.UiState.Online(""),
                    onClickTakePhoto = {},
                    onClickPick = {},
                    onClickSettings = {},
                    onClickLeaderboard = {},
                )
            }
        }
    }

    private fun initializeScreenWithRemoteError(e: Exception) {
        mockPhotosRemote = MockPhotosRemote(error = e)
        initializeScreen()
    }
    // endregion helper methods
}
