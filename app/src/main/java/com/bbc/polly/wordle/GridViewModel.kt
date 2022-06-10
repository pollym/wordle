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

class GridUiState(private val gridState: GridViewModel.GridState) {

    fun rows(): List<List<Letter>> {
        val rows = mutableListOf<List<Letter>>()
        var row: MutableList<Letter> = mutableListOf()
        gridState.letters.forEachIndexed { i, letter ->
            if (i % 5 == 0) {
                row = mutableListOf()
                rows.add(row)
            }
            val displayLetter = Letter(i, letter?.toString() ?: " ")
            row.add(displayLetter)
        }
        return rows.toList()
    }

    fun replaceLetter(newLetter: Letter): List<Char?> {
        val newLetters = gridState.letters.toMutableList()
        newLetters[newLetter.position] = newLetter.value.trim().singleOrNull()
        return newLetters.toList()
    }

    data class Letter(val position: Int, val value: String)
}