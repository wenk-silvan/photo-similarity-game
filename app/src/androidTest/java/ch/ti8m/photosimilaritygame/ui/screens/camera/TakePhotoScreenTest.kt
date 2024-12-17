package ch.wenksi.photosimilaritygame.ui.screens.camera

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.rule.GrantPermissionRule
import ch.wenksi.photosimilaritygame.Destination
import ch.wenksi.photosimilaritygame.MainActivity
import ch.wenksi.photosimilaritygame.ui.screens.takephoto.TakePhotoScreen
import ch.wenksi.photosimilaritygame.ui.screens.takephoto.TakePhotoViewModel
import ch.wenksi.photosimilaritygame.util.TestTags
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TakePhotoScreenTest {

    // region helper fields
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
    )

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    // endregion helper fields

    @Before
    fun setUp() {
        composeTestRule.setContent {
            TakePhotoScreen(
                uiState = TakePhotoViewModel.UiState(),
                snackbarHostState = SnackbarHostState(),
                onSwitchCamera = {},
                onTakePhoto = {},
            )
        }
    }

    @Test
    fun cameraScreen_isDisplayed() {
        composeTestRule
            .onNodeWithTag(Destination.TakePhoto.route)
            .assertIsDisplayed()
    }
}
