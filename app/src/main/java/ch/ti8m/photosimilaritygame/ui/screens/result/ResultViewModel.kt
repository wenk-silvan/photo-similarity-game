package ch.wenksi.photosimilaritygame.ui.screens.result

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.wenksi.photosimilaritygame.domain.model.CreateResultRequest
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.usecase.CreateResultUseCase
import ch.wenksi.photosimilaritygame.domain.model.Resource
import kotlinx.coroutines.launch

class ResultViewModel(
    private val createResultUseCase: CreateResultUseCase,
) : ViewModel() {

    var uiState: UiState by mutableStateOf(UiState.Loading)
        private set

    fun resetUiState() {
        uiState = UiState.Loading
    }

    fun createResult(
        randomPhotoUrl: String,
        cameraPhotoUri: Uri,
        userName: String,
    ) {
        viewModelScope.launch {
            val result = createResultUseCase(CreateResultRequest(
                userName = userName,
                randomPhotoUrl = randomPhotoUrl,
                cameraPhotoUri = cameraPhotoUri,
            ))
            uiState = when (result) {
                is Resource.Success -> UiState.Success(result.data)
                else -> throw Exception("Unexpected server error while loading random photo url")
            }
        }
    }

    sealed interface UiState {
        object Loading : UiState
        data class Success(
            val result: Result = Result(
                userName = "",
                similarityScore = 0,
                cameraPhotoLabels = emptyList(),
                cameraPhotoUri = Uri.EMPTY,
                randomPhotoLabels = emptyList(),
                randomPhotoUrl = "",
            ),
        ) : UiState
    }
}
