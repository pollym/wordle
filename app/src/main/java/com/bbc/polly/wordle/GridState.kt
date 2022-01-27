package com.bbc.polly.wordle

import android.util.Log
import androidx.compose.runtime.*

enum class LetterPosition { RIGHT_IN_RIGHT_PLACE, RIGHT_IN_WRONG_PLACE, WRONG }

class GridState {

    private val secretWord: CharArray = "PANTS".toCharArray()

    private var lastIndex by mutableStateOf(0)

    private var grid = List(25) { (' ') }.toMutableStateList()

    var lastGuessedWord by mutableStateOf("hello")

    var results = List(25) { LetterPosition.WRONG }.toMutableStateList()

    fun letterEntered(index: Int, letterString: String) {
        grid[index] = letterString.trim().firstOrNull() ?: ' '
        lastIndex = index
    }

    fun guess(index: Int = lastIndex): List<LetterPosition> {
        val rowStart = (index / 5) * 5
        val guess = StringBuilder()
        for (i in rowStart..rowStart+4) {
            guess.append(grid[i])
        }
        val word = guess.toString()
        lastGuessedWord = word
        val lastGuess = guessWord(word)
//        Log.d("polly", "last guess $word result $lastGuess")
        for (i in 0 until 5) {
            results[rowStart + i] = lastGuess[i]
        }
        return lastGuess
    }

    fun guessWord(word: String): List<LetterPosition> {
        val result = List(5) { i ->
            when {
                word[i] == secretWord[i] -> {
                    LetterPosition.RIGHT_IN_RIGHT_PLACE
                }
                secretWord.contains(word[i]) -> {
                    LetterPosition.RIGHT_IN_WRONG_PLACE
                }
                else -> {
                    LetterPosition.WRONG
                }
            }
        }
        return result
    }

}