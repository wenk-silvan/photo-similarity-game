package ch.wenksi.photosimilaritygame.ui.screens.photo

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.wenksi.photosimilaritygame.domain.logic.ConnectivityObserver
import ch.wenksi.photosimilaritygame.domain.usecase.GetRandomPhotoUrlUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.GetSettingsUseCase
import ch.wenksi.photosimilaritygame.domain.model.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PhotoViewModel(
    private val getRandomPhotoUrlUseCase: GetRandomPhotoUrlUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    var uiState: UiState by mutableStateOf(UiState.Loading)
        private set

    init {
        observeConnectivity()
    }

    fun startGame(connectivityStatus: ConnectivityObserver.Status) {
        viewModelScope.launch {
            val resource = getSettingsUseCase()
            if (resource is Resource.Success) {
                val isOffline = connectivityStatus != ConnectivityObserver.Status.Available
                if (resource.data.generatePhotoLocally || isOffline)
                    playOffline()
                else
                    playOnline()
            } else throw Exception("Unexpected server error while loading settings")
        }
    }

    fun setGalleryPhoto(uri: Uri) {
        uiState = UiState.Offline(uri)
    }

    private fun playOffline() {
        uiState = UiState.Offline(Uri.EMPTY)
    }

    private fun playOnline() {
        viewModelScope.launch {
            when (val resource = getRandomPhotoUrlUseCase()) {
                is Resource.Success -> {
                    uiState = UiState.Online(resource.data)
                }
                is Resource.NetworkError -> playOffline()
                else -> throw Exception("Unexpected server error while loading random photo url")
            }
        }
    }

    private fun observeConnectivity() {
        connectivityObserver.observe()
            .onEach {
                if (uiState is UiState.Online && it == ConnectivityObserver.Status.Available) {}
                else startGame(it)
            }
            .launchIn(viewModelScope)
    }

    sealed interface UiState {
        object Loading : UiState
        data class Offline(val galleryPhotoUri: Uri) : UiState
        data class Online(val randomPhotoUrl: String) : UiState
    }
}
