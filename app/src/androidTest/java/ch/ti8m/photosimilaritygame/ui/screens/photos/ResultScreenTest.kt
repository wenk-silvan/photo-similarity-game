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
import ch.wenksi.photosimilaritygame.di.datasourceModule
import ch.wenksi.photosimilaritygame.di.repositoryModule
import ch.wenksi.photosimilaritygame.di.useCaseModule
import ch.wenksi.photosimilaritygame.di.viewModelModule
import ch.wenksi.photosimilaritygame.mockdata.MockPhotosRemote
import ch.wenksi.photosimilaritygame.ui.screens.result.ResultScreen
import ch.wenksi.photosimilaritygame.ui.screens.result.ResultViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

class ResultScreenTest {

    // region helper fields
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
    // endregion helper fields

    @Before
    fun setUp() {
        composeTestRule.setContent {
            ResultScreen(
                uiState = ResultViewModel.UiState.Success(),
                onClickPlayAgain = {},
                onClickTryAgain = {},
                onClickLeaderboard = {},
            )
        }
    }

    @Test
    fun resultScreen_isDisplayed() {
        composeTestRule
            .onNodeWithTag(Destination.Result.route)
            .assertIsDisplayed()
    }

    @Test
    fun buttonPlayAgain_isDisplayed() {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.result_screen__button_new_game))
            .assertIsDisplayed()
    }
}
