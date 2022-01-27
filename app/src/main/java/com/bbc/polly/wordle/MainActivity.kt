package com.bbc.polly.wordle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bbc.polly.wordle.ui.theme.WordleTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordleTheme {
                val gridState = GridState()

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    WordGrid(gridState.lastGuessedWord,
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
fun WordGrid(lastGuessedWord: String,
             results: List<LetterPosition>,
             letterEntered: (Int, String) -> Unit,
             guess: () -> List<LetterPosition>
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

fun colourOf(letterPosition: LetterPosition): Color =
    when (letterPosition) {
        LetterPosition.WRONG -> Color.Transparent
        LetterPosition.RIGHT_IN_RIGHT_PLACE -> Color.Green
        LetterPosition.RIGHT_IN_WRONG_PLACE -> Color.Yellow
    }

@Composable
fun LetterCell(
    index: Int,
    letterPosition: LetterPosition,
    letterEntered: (Int, String) -> Unit
) {
    var text by remember { mutableStateOf(" ") }
    val focusManager = LocalFocusManager.current

    Log.d("polly", "recomposing letter $index")

    TextField(
        value = text,
        onValueChange = {
            text = it.uppercase()
            letterEntered(index, text)
            focusManager.moveFocus(FocusDirection.Right)
        },
        maxLines = 1,
        textStyle = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 42.sp
        ),
        modifier = Modifier
            .background(colourOf(letterPosition))
            .border(1.dp, Color.Cyan)
            .padding(6.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val gridState = GridState()
    WordleTheme {
        WordGrid(gridState.lastGuessedWord,
            gridState.results,
            gridState::letterEntered,
            gridState::guess
        )
    }
}