package com.example.game.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.game.ui.theme.Number1024
import com.example.game.ui.theme.Number128
import com.example.game.ui.theme.Number16
import com.example.game.ui.theme.Number2
import com.example.game.ui.theme.Number2048
import com.example.game.ui.theme.Number256
import com.example.game.ui.theme.Number32
import com.example.game.ui.theme.Number4
import com.example.game.ui.theme.Number512
import com.example.game.ui.theme.Number64
import com.example.game.ui.theme.Number8

@Composable
fun BoardView(board: Array<Array<Int>>) {
    Column {
        for (row in board) {
            Row {
                for (cell in row) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(getTileColor(cell), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (cell > 0) cell.toString() else "", fontSize = 24.sp, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}

fun getTileColor(value: Int): Color {
    return when (value) {
        2 -> Number2
        4 -> Number4
        8 -> Number8
        16 -> Number16
        32 -> Number32
        64 -> Number64
        128 -> Number128
        256 -> Number256
        512 -> Number512
        1024 -> Number1024
        2048 -> Number2048
        else -> Color.LightGray
    }
}