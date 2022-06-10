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
        val guesses: List<List<Char>> = mutableListOf(mutableListOf())
    )
    val gridState = MutableStateFlow(GridState(secretWordService.secretWord))

    val gridUiState = gridState.map { GridUiState(it) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            GridUiState(gridState.value)
        )

    class Factory(private val serviceContainer: ServiceContainer) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GridViewModel(serviceContainer.secretWordService) as T
        }
    }

}

class GridUiState(private val gridState: GridViewModel.GridState) {

}