package com.bbc.polly.wordle

import com.bbc.polly.wordle.LetterPosition.*
import junit.framework.Assert.assertEquals
import org.junit.Test

class GridStateTest {

    @Test
    fun typedAsdfg() {
        val gridState = GridState()
        gridState.letterEntered(5, "A")
        gridState.letterEntered(6, "S")
        gridState.letterEntered(7, "D")
        gridState.letterEntered(8, "F")
        gridState.letterEntered(9, "G")
        val result = gridState.guess(7)
        result.assertGuessIs(RIGHT_IN_WRONG_PLACE, RIGHT_IN_WRONG_PLACE, WRONG, WRONG, WRONG)
    }

    @Test
    fun typedPants() {
        val gridState = GridState()
        gridState.letterEntered(0, "P")
        gridState.letterEntered(1, "A")
        gridState.letterEntered(2, "N")
        gridState.letterEntered(3, "T")
        gridState.letterEntered(4, "S")
        val result = gridState.guess(2)
        result.assertGuessIs(RIGHT_IN_RIGHT_PLACE, RIGHT_IN_RIGHT_PLACE, RIGHT_IN_RIGHT_PLACE, RIGHT_IN_RIGHT_PLACE, RIGHT_IN_RIGHT_PLACE)
    }

    @Test
    fun asdfg() {
        val result = GridState().guessWord("ASDFG")
        result.assertGuessIs(RIGHT_IN_WRONG_PLACE, RIGHT_IN_WRONG_PLACE, WRONG, WRONG, WRONG)
    }

    @Test
    fun pants() {
        val result = GridState().guessWord("PANTS")
        result.assertGuessIs(RIGHT_IN_RIGHT_PLACE, RIGHT_IN_RIGHT_PLACE, RIGHT_IN_RIGHT_PLACE, RIGHT_IN_RIGHT_PLACE, RIGHT_IN_RIGHT_PLACE)
    }

    private fun List<LetterPosition>.assertGuessIs(
        expected0: LetterPosition,
        expected1: LetterPosition,
        expected2: LetterPosition,
        expected3: LetterPosition,
        expected4: LetterPosition
    ) {
        assertEquals(expected0, this[0])
        assertEquals(expected1, this[1])
        assertEquals(expected2, this[2])
        assertEquals(expected3, this[3])
        assertEquals(expected4, this[4])
    }

}