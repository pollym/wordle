package com.bbc.polly.wordle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bbc.polly.wordle.service.SecretWordService
import kotlinx.coroutines.flow.*

class GridViewModel(secretWordService: SecretWordService) : ViewModel() {
    data class GridState(
        val secretWord: String,
        val letters: List<Char?>
    )

    private val gridState = MutableStateFlow(GridState(secretWordService.secretWord, List(25) { null } ))

    val gridUiState = gridState.map { GridUiState(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            GridUiState(gridState.value)
        )

    fun update() {
        gridState.update { gridState -> GridState(gridState.secretWord, gridUiState.value.letters) }
    }

    class Factory(private val serviceContainer: ServiceContainer) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GridViewModel(serviceContainer.secretWordService) as T
        }
    }

}

class GridUiState(gridState: GridViewModel.GridState) {
    val letters = gridState.letters

    fun rows(): List<List<String>> {
        val rows = mutableListOf<List<String>>()
        var row: MutableList<String> = mutableListOf()
        letters.forEachIndexed { i, letter ->
            if (i%5 == 0) {
                row = mutableListOf()
                rows.add(row)
            }
            row.add(letter?.toString() ?: " ")
        }
        return rows.toList()
    }
}