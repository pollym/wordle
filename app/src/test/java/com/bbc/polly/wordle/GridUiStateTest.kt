package com.bbc.polly.wordle

import org.junit.Assert.*
import org.junit.Test

class GridUiStateTest {

    @Test
    fun `can make empty grid`() {
        val emptyGrid = createGrid(List(25) { null })
        val expectedState = """
-----
-----
-----
-----
-----
        """.trimIndent()
        assertEquals(convertToGridRows(expectedState), emptyGrid.rows())
    }

    @Test
    fun `can make grid with some letters`() {
        val letters = List(25) { i -> if (i < 5) 'a' else null }
        val grid = createGrid(letters)
        val expectedState = """
aaaaa
-----
-----
-----
-----
        """.trimIndent()
        assertEquals(convertToGridRows(expectedState), grid.rows())
    }

    @Test
    fun `can replace single letter in grid`() {
        val letters = List(25) { i -> if (i < 5) 'a' else null }
        val grid = createGrid(letters)
        val replaced = grid.replaceLetter(GridUiState.Letter(1, "b", true))
        val expectedState = """
abaaa
-----
-----
-----
-----
        """.trimIndent()
        assertEquals(convertToChars(expectedState), replaced)
    }

    private fun createGrid(letters: List<Char?>) =
        GridUiState(GridViewModel.GridState("PANTS", letters))

    /**
     * Helper function which maps a multiline string to a list of chars.
     * Strips out line breaks and whitespace & replaces hyphens with nulls.
     * This makes it handy to write out an expected grid state & assert against it.
     */
    private fun convertToChars(expectedState: String): List<Char?> {
        val letters = mutableListOf<Char?>()
        expectedState.filter { it.isLetter() || it == '-' }.forEach() { letter ->
            letters.add(
                if (letter == '-') null
                else letter
            )
        }
        return letters
    }

    private fun convertToGridRows(expectedState: String): List<List<GridUiState.Letter>> {
        val rows = mutableListOf<List<GridUiState.Letter>>()
        expectedState.lines().forEachIndexed() { i, line ->
            val row: MutableList<GridUiState.Letter> = mutableListOf()
            line.forEachIndexed() { j, letterChar ->
                row.add(GridUiState.Letter(i * 5 + j, mapCharToString(letterChar), true))
            }
            assertEquals(5, row.size)
            rows.add(row)
        }
        assertEquals(5, rows.size)
        return rows
    }

    private fun mapCharToString(letterChar: Char): String {
        val letterValue = when (letterChar) {
            '-' -> " "
            else -> letterChar.toString()
        }
        return letterValue
    }

}