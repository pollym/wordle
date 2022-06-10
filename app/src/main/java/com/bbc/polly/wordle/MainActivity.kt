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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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

                WordGrid(viewModel.gridUiState.collectAsState().value) { viewModel.update() }
            }
        }
    }
}

@Composable
private fun WordGrid(gridUiState: GridUiState, update: () -> Unit) {
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
                        LetterCell(letter, update)
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = { update() },
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
private fun LetterCell(letter: String, update: () -> Unit) {
    TextField(
        value = letter,
        onValueChange = {
            update()
        },
        maxLines = 1,
        textStyle = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 36.sp
        ),
        modifier = Modifier
            .border(1.dp, Color.Cyan)
            .padding(6.dp)
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
        WordGrid(GridUiState(gridState)) {}
    }
}