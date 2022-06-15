package com.bbc.polly.wordle

class GridUiState(private val gridState: GridViewModel.GridState) {

    fun rows(): List<List<Letter>> {
        val rows = mutableListOf<List<Letter>>()
        var row: MutableList<Letter> = mutableListOf()
        gridState.letters.forEachIndexed { i, letter ->
            if (i % 5 == 0) {
                row = mutableListOf()
                rows.add(row)
            }
            val displayLetter = Letter(i, letter?.toString() ?: " ", true) //TODO figure out if editable
            row.add(displayLetter)
        }
        return rows.toList()
    }

    fun replaceLetter(newLetter: Letter): List<Char?> {
        val newLetters = gridState.letters.toMutableList()
        newLetters[newLetter.position] = newLetter.value.trim().singleOrNull()
        return newLetters.toList()
    }

    data class Letter(val position: Int, val value: String, val editable: Boolean)
}