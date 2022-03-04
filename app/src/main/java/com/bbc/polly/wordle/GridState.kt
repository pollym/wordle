package com.bbc.polly.wordle

import android.util.Log
import androidx.compose.runtime.*

enum class LetterStatus { RIGHT_IN_RIGHT_PLACE, RIGHT_IN_WRONG_PLACE, WRONG }

class GridState {

    private val secretWord: CharArray = "PANTS".toCharArray()

    companion object {
        const val NUM_ROWS = 5
        const val NUM_COLS = 5
        const val NUM_LETTERS = NUM_ROWS * NUM_COLS
    }

    private var numGuesses = mutableStateOf(0)
    val gridLetters = List(NUM_LETTERS) { (' ') }.toMutableStateList()
    val results = List(NUM_LETTERS) { LetterStatus.WRONG }.toMutableStateList()

    fun letterEntered(index: Int, letter: Char) {
        gridLetters[index] = letter
    }

    val lastGuessedWord: String
        get() = if (numGuesses.value == 0) ""
            else {
                val startPos = (numGuesses.value - 1) * NUM_COLS
                gridLetters.subList(startPos, startPos+NUM_COLS).toString()
            }

    fun guess(): List<LetterStatus> {
        val rowStart = numGuesses.value * NUM_COLS
        val guess = StringBuilder()
        for (i in rowStart until rowStart+NUM_COLS) {
            guess.append(gridLetters[i])
        }
        val word = guess.toString()
        val lastGuess = guessWord(word)
        Log.d("polly", "last guess $word result $lastGuess")
        for (i in 0 until NUM_COLS) {
            results[rowStart + i] = lastGuess[i]
        }
        return lastGuess
    }

    fun guessWord(word: String): List<LetterStatus> {
        val result = List(NUM_COLS) { i ->
            when {
                word[i] == secretWord[i] -> {
                    LetterStatus.RIGHT_IN_RIGHT_PLACE
                }
                secretWord.contains(word[i]) -> {
                    LetterStatus.RIGHT_IN_WRONG_PLACE
                }
                else -> {
                    LetterStatus.WRONG
                }
            }
        }
        return result
    }

}