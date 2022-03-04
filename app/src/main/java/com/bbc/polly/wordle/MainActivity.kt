package com.bbc.polly.wordle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bbc.polly.wordle.ui.theme.WordleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordleTheme {
                val gridState = GridState()

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    WordGrid(gridState.lastGuessedWord,
                        gridState.gridLetters,
                        gridState.results,
                        gridState::letterEntered,
                        gridState::guess
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WordGrid(lastGuessedWord: String,
                     gridLetters: List<Char>,
                     results: List<LetterStatus>,
                     letterEntered: (Int, Char) -> Unit,
                     guess: () -> List<LetterStatus>
) {

    Log.d("polly", "recomposing grid. last guessed: $lastGuessedWord")
    Column {
        Row(horizontalArrangement = Arrangement.Center) {
            Text("last guessed word: ")
            Text(lastGuessedWord)
        }
        LazyVerticalGrid(
            cells = GridCells.Fixed(5)
        ) {
            items(results.size) { index ->
                LetterCell(
                    index,
                    gridLetters[index],
                    results[index],
                    letterEntered
                )
            }
        }
        Row(horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = { guess() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Yellow
                )
            ) {
                Text("Guess !")
            }
        }
    }
}

private fun colourOf(letterStatus: LetterStatus): Color =
    when (letterStatus) {
        LetterStatus.WRONG -> Color.Transparent
        LetterStatus.RIGHT_IN_RIGHT_PLACE -> Color.Green
        LetterStatus.RIGHT_IN_WRONG_PLACE -> Color.Yellow
    }

@Composable
private fun LetterCell(
    index: Int,
    letter: Char,
    letterStatus: LetterStatus,
    letterEntered: (Int, Char) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Log.d("polly", "recomposing: letter $index = $letter")

    TextField(
        value = letter.toString(),
        onValueChange = {
            if (it.isNotEmpty()) {
                letterEntered(index, it.uppercase().first())
                focusManager.moveFocus(FocusDirection.Right)
            }
        },
        maxLines = 1,
        textStyle = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 42.sp
        ),
        modifier = Modifier
            .background(colourOf(letterStatus))
            .border(1.dp, Color.Cyan)
            .padding(6.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val gridState = GridState().also {
        it.gridLetters.addAll("ASDFGHJKLQ               ".toList())
    }
    WordleTheme {
        WordGrid(gridState.lastGuessedWord,
            gridState.gridLetters,
            gridState.results,
            gridState::letterEntered,
            gridState::guess
        )
    }
}