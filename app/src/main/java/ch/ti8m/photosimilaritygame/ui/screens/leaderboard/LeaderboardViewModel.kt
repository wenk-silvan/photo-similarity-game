package ch.wenksi.photosimilaritygame.ui.screens.leaderboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.wenksi.photosimilaritygame.domain.model.Result
import ch.wenksi.photosimilaritygame.domain.usecase.ClearLeaderboardUseCase
import ch.wenksi.photosimilaritygame.domain.usecase.GetResultsUseCase
import ch.wenksi.photosimilaritygame.domain.model.Resource
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val getResultsUseCase: GetResultsUseCase,
    private val clearLeaderboardUseCase: ClearLeaderboardUseCase,
) : ViewModel() {
    var uiState: UiState by mutableStateOf(UiState.Loading)
        private set

    init {
        getResults()
    }

    fun getResults() {
        viewModelScope.launch {
            updateResults(getResultsUseCase())
        }
    }

    fun clearLeaderboard() {
        viewModelScope.launch {
            clearLeaderboardUseCase()
            updateResults(getResultsUseCase())
        }
    }

    private fun updateResults(results: Resource<List<Result>>) {
        uiState = when (results) {
            is Resource.Success -> UiState.Success(results.data)
            is Resource.ServerError -> UiState.Error
            else -> throw Exception("Unexpected server error while loading random photo url")
        }
    }

    sealed interface UiState {
        object Loading : UiState
        object Error : UiState
        data class Success(
            val results: List<Result> = emptyList(),
        ) : UiState
    }
}
