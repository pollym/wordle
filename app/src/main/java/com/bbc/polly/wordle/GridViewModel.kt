package com.bbc.polly.wordle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bbc.polly.wordle.service.SecretWordService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GridViewModel(secretWordService: SecretWordService) : ViewModel() {
    data class GridState(
        val secretWord: String,
        val letters: List<Char?>
    )

    private val gridStateFlow =
        MutableStateFlow(GridState(secretWordService.secretWord, List(25) { null }))

    val gridUiStateFlow = gridStateFlow.map { GridUiState(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            GridUiState(gridStateFlow.value)
        )

    fun updateGrid() {
        //TODO call this on guess!
//        gridState.update { gridState -> GridState(gridState.secretWord, gridUiState.value.gridState.letters) }
    }

    fun updateLetter(newLetter: GridUiState.Letter) {
        gridStateFlow.value = gridStateFlow.value.copy(
            letters = gridUiStateFlow.value.replaceLetter(
                newLetter
            )
        )
    }

    class Factory(private val serviceContainer: ServiceContainer) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GridViewModel(serviceContainer.secretWordService) as T
        }
    }
}