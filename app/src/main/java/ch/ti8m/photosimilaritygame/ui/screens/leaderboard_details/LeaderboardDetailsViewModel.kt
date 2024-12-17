package ch.wenksi.photosimilaritygame.ui.screens.leaderboard_details

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.usecase.GetResultUseCase
import ch.wenksi.photosimilaritygame.domain.model.Resource
import kotlinx.coroutines.launch

class LeaderboardDetailsViewModel(
    private val getResultUseCase: GetResultUseCase,
) : ViewModel() {
    var uiState: UiState by mutableStateOf(UiState.Loading)
        private set

    fun getResult(index: Int) {
        viewModelScope.launch {
            uiState = when (val result = getResultUseCase(index)) {
                is Resource.Success -> UiState.Success(result.data)
                is Resource.ServerError -> UiState.Error
                else -> throw Exception("Unexpected server error while loading random photo url")
            }
        }
    }

    sealed interface UiState {
        object Loading : UiState
        object Error : UiState
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
