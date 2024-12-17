package ch.wenksi.photosimilaritygame.ui.screens.settings

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.wenksi.photosimilaritygame.R
import ch.wenksi.photosimilaritygame.domain.model.Resource
import ch.wenksi.photosimilaritygame.domain.model.Settings
import ch.wenksi.photosimilaritygame.domain.usecase.GetSettingsUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.LogoutUserUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.UpdateSettingsUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.UpdateUserNameUseCase
import ch.wenksi.photosimilaritygame.ui.UiText
import ch.wenksi.photosimilaritygame.ui.theme.color
import kotlinx.coroutines.launch


private const val RULES_URL =
    "https://github.com/wenk-silvan/app-legal-templates/blob/main/photo-similarity-game/rules.md"
private const val PRIVACY_POLICY_URL =
    "https://github.com/wenk-silvan/app-legal-templates/blob/main/photo-similarity-game/privacy-policy.md"
private const val TERMS_CONDITIONS_URL =
    "https://github.com/wenk-silvan/app-legal-templates/blob/main/photo-similarity-game/terms-conditions.md"

class SettingsViewModel(
    private val context: Context,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateSettingsUseCase: UpdateSettingsUseCase,
    private val updateUserNameUseCase: UpdateUserNameUseCase,
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    var chromeTabsClient: CustomTabsClient? = null
    var chromeTabsSession: CustomTabsSession? = null

    init {
        getSettings()
    }

    fun warmupChromeTabs() {
        val connection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                componentName: ComponentName,
                customTabsClient: CustomTabsClient,
            ) {
                chromeTabsClient = customTabsClient
                chromeTabsSession = chromeTabsClient?.newSession(null)
                val likelyUrls = bundleOf(
                    CustomTabsService.KEY_URL to Uri.parse(PRIVACY_POLICY_URL),
                    CustomTabsService.KEY_URL to Uri.parse(TERMS_CONDITIONS_URL)
                )
                chromeTabsClient?.warmup(0L)
                chromeTabsSession?.mayLaunchUrl(Uri.parse(RULES_URL), likelyUrls, null)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                chromeTabsClient = null
                chromeTabsSession = null
            }
        }
        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", connection)
    }

    fun setGenerateLocally(value: Boolean) {
        val newSettings = uiState.settings.copy(generatePhotoLocally = value)
        updateSettings(newSettings)
    }

    fun setEditUserName(value: String) {
        uiState = uiState.copy(editUserName = value)
    }

    fun saveUserName(
        oldName: String,
        newName: String,
        onSuccess: () -> Unit,
    ) {
        if (uiState.editUserName == "") {
            setError(true)
        } else {
            setError(false)
            viewModelScope.launch {
                updateUserNameUseCase(oldName, newName)
                onSuccess()
            }
        }
    }

    fun logout(onSuccess: () -> Unit, onError: () -> Unit = {}) {
        viewModelScope.launch {
            when (logoutUserUseCase()) {
                is Resource.Success -> onSuccess()
                else -> {
                    setSnackbarError(UiText.StringResource(R.string.settings_logout_failed))
                    onError()
                }
            }
        }
    }

    fun showRules(context: Context) = openChromeTab(context, RULES_URL)

    fun showTermsConditions(context: Context) = openChromeTab(context, TERMS_CONDITIONS_URL)

    fun showPrivacyPolicy(context: Context) = openChromeTab(context, PRIVACY_POLICY_URL)

    private fun openChromeTab(context: Context, url: String) {
        val colorScheme = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(color.light.primary.toArgb()).build()
        CustomTabsIntent.Builder(chromeTabsSession)
            .setDefaultColorSchemeParams(colorScheme)
            .build()
            .launchUrl(context, Uri.parse(url))
    }

    fun resetSnackbarError() = setSnackbarError(null)

    private fun setSnackbarError(value: UiText?) {
        uiState = uiState.copy(snackbarError = value)
    }

    private fun getSettings() {
        viewModelScope.launch {
            when (val resource = getSettingsUseCase()) {
                is Resource.Success -> {
                    uiState = uiState.copy(
                        settings = Settings(
                            generatePhotoLocally = resource.data.generatePhotoLocally,
                        )
                    )
                }
                else -> throw Exception()
            }
        }
    }

    private fun updateSettings(newSettings: Settings) {
        uiState = uiState.copy(settings = newSettings)
        viewModelScope.launch {
            updateSettingsUseCase(newSettings)
        }
    }

    private fun setError(value: Boolean) {
        uiState = uiState.copy(isError = value)
    }

    data class UiState(
        val settings: Settings = Settings(),
        val editUserName: String = "",
        val isError: Boolean = false,
        val snackbarError: UiText? = null,
    )
}
