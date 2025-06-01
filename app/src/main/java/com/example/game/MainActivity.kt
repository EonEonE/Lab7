package com.example.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random
import com.example.game.ui.components.BoardView
import com.example.game.ui.theme.GameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameTheme {
                Game2048()
            }
        }
    }
}

@Composable
fun Game2048() {
    var board by remember { mutableStateOf(generateEmptyBoard()) }
    var gameOver by remember { mutableStateOf(false) }
    var victory by remember { mutableStateOf(false) }

    fun resetGame() {
        board = generateEmptyBoard()
        gameOver = false
        victory = false
    }

    fun updateGameState(newBoard: Array<Array<Int>>) {
        board = newBoard
        gameOver = checkGameOver(board)
        victory = checkVictory(board)
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("2048", fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            BoardView(board)

            Spacer(modifier = Modifier.height(16.dp))

            if (victory) {
                Text("Игра окончена!", fontSize = 24.sp, color = Color.Green)
            } else if (gameOver) {
                Text("Игра окончена!", fontSize = 24.sp, color = Color.Red)
            }

            if (!gameOver && !victory) {
                Row {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            if (!gameOver && !victory) updateGameState(
                                move(
                                    board,
                                    'U'
                                )
                            )
                        }) {
                            Text("Вверх")
                        }

                        Row {
                            Button(onClick = {
                                if (!gameOver && !victory) updateGameState(
                                    move(
                                        board, 'L'
                                    )
                                )
                            }) {
                                Text("Влево")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                if (!gameOver && !victory) updateGameState(
                                    move(
                                        board, 'R'
                                    )
                                )
                            }) {
                                Text("Вправо")
                            }
                        }

                        Button(onClick = {
                            if (!gameOver && !victory) updateGameState(
                                move(
                                    board,
                                    'D'
                                )
                            )
                        }) {
                            Text("Вниз")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = { resetGame() }) {
                Text("Заново")
            }
        }
    }
}

fun checkGameOver(board: Array<Array<Int>>): Boolean {
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            if (board[i][j] == 0) return false
            if (j < 3 && board[i][j] == board[i][j + 1]) return false
            if (i < 3 && board[i][j] == board[i + 1][j]) return false
        }
    }
    return true
}

fun checkVictory(board: Array<Array<Int>>): Boolean {
    return board.any { row -> row.any { it == 2048 } }
}

fun generateEmptyBoard(): Array<Array<Int>> {
    val board = Array(4) { Array(4) { 0 } }
    addRandomTile(board)
    addRandomTile(board)
    return board
}

fun addRandomTile(board: Array<Array<Int>>) {
    val emptyCells = mutableListOf<Pair<Int, Int>>()
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            if (board[i][j] == 0) emptyCells.add(i to j)
        }
    }
    if (emptyCells.isNotEmpty()) {
        val (x, y) = emptyCells.random()
        board[x][y] = if (Random.nextDouble() < 0.9) 2 else 4
    }
}

fun move(board: Array<Array<Int>>, direction: Char): Array<Array<Int>> {
    return when (direction) {
        'L' -> board.map { slideAndMerge(it) }.toTypedArray()
        'R' -> board.map { slideAndMerge(it.reversed().toTypedArray()).reversedArray() }
            .toTypedArray()
        'U' -> {
            val transposed = transpose(board)
            transpose(transposed.map { slideAndMerge(it) }.toTypedArray())
        }
        'D' -> {
            val transposed = transpose(board)
            transpose(transposed.map { slideAndMerge(it.reversed().toTypedArray()).reversedArray() }
                .toTypedArray())
        }

        else -> board
    }.also { addRandomTile(it) }
}


fun slideAndMerge(row: Array<Int>): Array<Int> {
    val filtered = row.filter { it > 0 }.toMutableList()
    for (i in 0 until filtered.size - 1) {
        if (filtered[i] == filtered[i + 1]) {
            filtered[i] *= 2
            filtered[i + 1] = 0
        }
    }
    val merged = filtered.filter { it > 0 }.toTypedArray()
    return merged + Array(4 - merged.size) { 0 }
}

fun transpose(board: Array<Array<Int>>): Array<Array<Int>> {
    val newBoard = Array(4) { Array(4) { 0 } }
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            newBoard[i][j] = board[j][i]
        }
    }
    return newBoard
}

