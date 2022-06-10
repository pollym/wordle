package com.bbc.polly.wordle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bbc.polly.wordle.service.SecretWordService
import com.bbc.polly.wordle.ui.theme.WordleTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val serviceContainer = (applicationContext as WordleApp).serviceContainer
        setContent {
            WordleTheme {
                WordGrid(viewModel(factory = GridViewModel.Factory(serviceContainer)))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WordGrid(viewModel: GridViewModel) {
    Log.d("polly", "recomposing grid.")
    Column {
            Row(horizontalArrangement = Arrangement.Center) {
                Text("last guessed word: ")
            }
        LazyVerticalGrid(
            cells = GridCells.Fixed(5)
        ) {
            items(5) {
                LetterCell()
            }
        }
        Row(horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {  },
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
private fun LetterCell() {
    TextField(
        value = "",
        onValueChange = {

        },
        maxLines = 1,
        textStyle = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 42.sp
        ),
        modifier = Modifier
            .border(1.dp, Color.Cyan)
            .padding(6.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    WordleTheme {
        WordGrid(GridViewModel(SecretWordService()))
    }
}