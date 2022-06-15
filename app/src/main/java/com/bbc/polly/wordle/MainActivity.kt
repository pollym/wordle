package com.bbc.polly.wordle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bbc.polly.wordle.ui.theme.WordleTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val serviceContainer = (applicationContext as WordleApp).serviceContainer
        setContent {
            WordleTheme {
                val viewModel: GridViewModel =
                    viewModel(factory = GridViewModel.Factory(serviceContainer))

                WordGrid(viewModel.gridUiStateFlow.collectAsState().value, { viewModel.updateGrid() }, { position, value ->  viewModel.updateLetter(position, value) })
            }
        }
    }
}

@Composable
private fun WordGrid(
    gridUiState: GridUiState,
    updateGrid: () -> Unit,
    updateLetter: (Int, String) -> Unit
) {
    Log.d("polly", "recomposing grid.")
    Column {
        Row(horizontalArrangement = Arrangement.Center) {
            Text("last guessed word: ")
        }
        gridUiState.rows().forEach { row ->
            Row(horizontalArrangement = Arrangement.Center) {
                row.forEach { letter ->
                    Column(
                        Modifier
                            .width(72.dp)
                            .height(72.dp)
                    ) {
                        LetterCell(letter, updateLetter)
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = { updateGrid() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Yellow
                )
            ) {
                Text("Guess !")
            }
        }
    }
}

@Composable
private fun LetterCell(letter: GridUiState.Letter, updateLetter: (Int, String) -> Unit) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = letter.value,
        onValueChange = { newValue ->
            updateLetter(letter.position, newValue)
            if (newValue.isNotBlank()) focusManager.moveFocus(FocusDirection.Next) //TODO pull this logic into UIState class
        },
        maxLines = 1,
        textStyle = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .border(1.dp, Color.Cyan)
            .padding(4.dp)
            .width(64.dp)
            .height(64.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    WordleTheme {
        val gridState = GridViewModel.GridState(
            "HELLO",
            listOf(
                'a',
                's',
                'd',
                'f',
                'g',
                'a',
                's',
                'd',
                'f',
                'g',
                'a',
                's',
                'd',
                'f',
                'g',
                'a',
                's',
                'd',
                'f',
                'g',
                'a',
                's',
                'd',
                'f',
                'g',
            )
        )
        WordGrid(GridUiState(gridState), {}) { _,_ -> }
    }
}